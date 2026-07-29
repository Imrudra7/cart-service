package in.maithilart.cart.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import in.maithilart.cart.client.ProductClient;
import in.maithilart.cart.client.dto.ProductResponse;
import in.maithilart.cart.client.dto.ProductVariantResponse;
import in.maithilart.cart.dto.AddCartItemRequest;
import in.maithilart.cart.dto.CartItemResponse;
import in.maithilart.cart.dto.CartResponse;
import in.maithilart.cart.dto.UpdateCartItemRequest;
import in.maithilart.cart.entity.Cart;
import in.maithilart.cart.entity.CartItem;
import in.maithilart.cart.repository.CartItemRepository;
import in.maithilart.cart.repository.CartRepository;
import in.maithilart.cart.service.ICartService;
import in.maithilart.common.constants.MaithilConstants;
import in.maithilart.common.exception.MaithilException;
import jakarta.transaction.Transactional;

@Service
public class CartService implements ICartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductClient productClient;

	public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
			ProductClient productClient) {
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.productClient = productClient;
	}

	@Override
	public CartResponse getCart(String userId) {

		Cart cart = cartRepository.findByUserIdAndStatus(UUID.fromString(userId), MaithilConstants.CART_STATUS_ACTIVE)
				.orElseThrow(
						() -> new MaithilException(MaithilConstants.FAILED_STATUS, MaithilConstants.CART_NOT_FOUND));

		List<CartItem> items = cartItemRepository.findByCartIdOrderByCreatedAtAsc(cart.getCartId());

		CartResponse response = new CartResponse();

		response.setCartId(cart.getCartId());
		response.setStatus(cart.getStatus());

		if (items.isEmpty()) {

			response.setItems(Collections.emptyList());

			response.setItemTotalAmount(BigDecimal.ZERO);
			response.setDiscountedItemTotalAmount(BigDecimal.ZERO);
			response.setHandlingFee(BigDecimal.ZERO);
			response.setPlatformFee(BigDecimal.ZERO);
			response.setDeliveryPartnerFee(BigDecimal.ZERO);
			response.setToPay(BigDecimal.ZERO);

			return response;
		}

		List<UUID> productIds = items.stream().map(CartItem::getProductId).distinct().toList();

		List<ProductResponse> products = productClient.getProducts(productIds);

		Map<UUID, ProductResponse> productMap = products.stream()
				.collect(Collectors.toMap(ProductResponse::getId, Function.identity()));

		List<CartItemResponse> itemResponses = new ArrayList<>();

		BigDecimal itemTotal = BigDecimal.ZERO;

		BigDecimal discountedTotal = BigDecimal.ZERO;

		System.out.println("Cart Items Count = " + items.size());

		for (CartItem cartItem : items) {

			System.out.println(cartItem.getCartItemId() + " -> " + cartItem.getQuantity());

			ProductResponse product = productMap.get(cartItem.getProductId());

			if (product == null) {
				continue;
			}

			ProductVariantResponse variant = product.getVariants().stream()
					.filter(v -> v.getId().equals(cartItem.getVariantId())).findFirst()
					.orElseThrow(() -> new MaithilException(MaithilConstants.FAILED_STATUS, "Variant not found"));

			CartItemResponse itemResponse = new CartItemResponse();

			itemResponse.setProductName(product.getName());

			itemResponse.setImageUrl(product.getImageUrl());

			itemResponse.setVariantLabel(variant.getVariantAttributes().values().stream().map(String::valueOf)
					.collect(Collectors.joining(" / ")));

			itemResponse.setCartItemId(cartItem.getCartItemId());

			itemResponse.setProductId(cartItem.getProductId());

			itemResponse.setVariantId(cartItem.getVariantId());

			itemResponse.setQuantity(cartItem.getQuantity());

			BigDecimal effectivePrice = variant.getSalePrice() != null ? variant.getSalePrice() : variant.getPrice();

			itemResponse.setUnitPrice(effectivePrice);

			itemResponse.setDiscountedPrice(variant.getSalePrice());

			BigDecimal value = variant.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

			BigDecimal discountedValue = variant.getSalePrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

			itemResponse.setValue(value);

			Map<String, Object> attrs = variant.getVariantAttributes();

			if (attrs != null) {

				Object packSize = attrs.get("packSize");

				Object uom = attrs.get("uom");

				if (packSize != null) {

					itemResponse.setPackSize(new BigDecimal(packSize.toString()));
				}

				if (uom != null) {

					itemResponse.setUom(uom.toString());
				}
			}

			itemTotal = itemTotal.add(value);

			discountedTotal = discountedTotal.add(discountedValue);

			itemResponses.add(itemResponse);
		}

		BigDecimal handlingFee = BigDecimal.valueOf(20);

		BigDecimal platformFee = BigDecimal.valueOf(5);

		BigDecimal deliveryFee = BigDecimal.valueOf(40);

		BigDecimal toPay = discountedTotal.add(handlingFee).add(platformFee).add(deliveryFee);

		response.setItems(itemResponses);

		response.setItemTotalAmount(itemTotal);

		response.setDiscountedItemTotalAmount(discountedTotal);

		response.setHandlingFee(handlingFee);

		response.setPlatformFee(platformFee);

		response.setDeliveryPartnerFee(deliveryFee);

		response.setToPay(toPay);

		return response;
	}

	@Override
	public CartResponse updateCart(UpdateCartItemRequest request, String userId) {

		if (null == request) {
			throw new MaithilException(MaithilConstants.FAILED_STATUS, MaithilConstants.ITEM_UPDATE_FAIL_MESSAGE);
		}

		String method = request.getMethod();

		if (null == method || method.isBlank()) {
			throw new MaithilException(MaithilConstants.FAILED_STATUS, MaithilConstants.ITEM_UPDATE_FAIL_MESSAGE);
		}
		return MaithilConstants.CART_INCREASE_ITEM.equals(method) ? increaseCartQuantity(request, userId)
				: (MaithilConstants.CART_DECREASE_ITEM.equals(method) ? decreaseCartQuantity(request, userId) : null);
	}

	private CartResponse decreaseCartQuantity(UpdateCartItemRequest request, String userId) {

		CartItem item = cartItemRepository.findById(request.getCartItemId()).orElseThrow(
				() -> new MaithilException(MaithilConstants.FAILED_STATUS, MaithilConstants.CART_ITEM_NOT_FOUND));

		if (item.getQuantity() <= 1) {

			cartItemRepository.delete(item);

		} else {

			item.setQuantity(item.getQuantity() - 1);

			cartItemRepository.save(item);
		}

		return getCart(userId);
	}

	private CartResponse increaseCartQuantity(UpdateCartItemRequest request, String userId) {

		CartItem item = cartItemRepository.findById(request.getCartItemId()).orElseThrow(
				() -> new MaithilException(MaithilConstants.FAILED_STATUS, MaithilConstants.CART_ITEM_NOT_FOUND));

		System.out.println("Before : " + item.getQuantity());

		item.setQuantity(item.getQuantity() + 1);

		System.out.println("After : " + item.getQuantity());

		cartItemRepository.save(item);

		return getCart(userId);
	}

	@Transactional
	@Override
	public CartResponse clearCart(String userId) {

		Cart cart = cartRepository.findByUserIdAndStatus(UUID.fromString(userId), MaithilConstants.CART_STATUS_ACTIVE)
				.orElseThrow(
						() -> new MaithilException(MaithilConstants.FAILED_STATUS, MaithilConstants.CART_NOT_FOUND));

		cartItemRepository.deleteByCartId(cart.getCartId());

		return getCart(userId);
	}

	@Override
	public CartResponse deleteItem(UpdateCartItemRequest request, String userId) {

		CartItem item = cartItemRepository.findById(request.getCartItemId()).orElseThrow(
				() -> new MaithilException(MaithilConstants.FAILED_STATUS, MaithilConstants.CART_ITEM_NOT_FOUND));

		cartItemRepository.delete(item);

		return getCart(userId);
	}

	@Override
	public CartResponse addToCart(AddCartItemRequest request, String userId) {

		Cart cart = cartRepository.findByUserIdAndStatus(UUID.fromString(userId), MaithilConstants.CART_STATUS_ACTIVE)
				.orElseGet(() -> createCart(userId));

		Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductIdAndVariantIdOrderByCreatedAtAsc(cart.getCartId(),
				request.getProductId(), request.getVariantId());

		if (existingItem.isPresent()) {

			CartItem item = existingItem.get();

			item.setQuantity(item.getQuantity() + request.getQuantity());

			cartItemRepository.save(item);

		} else {

			CartItem item = new CartItem();

			item.setCartId(cart.getCartId());
			item.setProductId(request.getProductId());

			item.setVariantId(request.getVariantId());

			item.setQuantity(request.getQuantity());

			cartItemRepository.save(item);
		}

		return getCart(userId);
	}

	private Cart createCart(String userId) {

		Cart cart = new Cart();

		cart.setUserId(UUID.fromString(userId));
		cart.setStatus(MaithilConstants.CART_STATUS_ACTIVE);

		return cartRepository.save(cart);
	}

}
