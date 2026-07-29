package in.maithilart.cart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.maithilart.cart.dto.AddCartItemRequest;
import in.maithilart.cart.dto.CartResponse;
import in.maithilart.cart.dto.UpdateCartItemRequest;
import in.maithilart.cart.service.ICartService;
import in.maithilart.cart.util.CartCurrentUserProvider;
import in.maithilart.common.constants.MaithilConstants;
import in.maithilart.common.dto.MaithilResponse;
import in.maithilart.common.security.MaithilPrincipal;

@RequestMapping("/cart/api")
@RestController
public class CartController {

	private final ICartService cartService;
	private final CartCurrentUserProvider currentUserProvider;

	public CartController(ICartService cartService, CartCurrentUserProvider currentUserProvider) {
		this.cartService = cartService;
		this.currentUserProvider = currentUserProvider;
	}

	@GetMapping
	public ResponseEntity<MaithilResponse<CartResponse>> getCart() {

		MaithilPrincipal principal = currentUserProvider.getCurrentUser();
		CartResponse response = cartService.getCart(principal.getUserId());

		return ResponseEntity.ok(MaithilResponse.success(MaithilConstants.CART_FETCHED,
				MaithilConstants.CART_FETCHED_MESSAGE, response));
	}

	@PostMapping
	public ResponseEntity<MaithilResponse<CartResponse>> addToCart(@RequestBody AddCartItemRequest request) {
		MaithilPrincipal principal = currentUserProvider.getCurrentUser();
		CartResponse response = cartService.addToCart(request, principal.getUserId());

		return ResponseEntity.ok(
				MaithilResponse.success(MaithilConstants.ITEM_ADDED, MaithilConstants.ITEM_ADDED_MESSAGE, response));
	}

	@PatchMapping
	public ResponseEntity<MaithilResponse<CartResponse>> updateCart(@RequestBody UpdateCartItemRequest request) {
		MaithilPrincipal principal = currentUserProvider.getCurrentUser();
		CartResponse response = cartService.updateCart(request, principal.getUserId());

		return ResponseEntity.ok(MaithilResponse.success(MaithilConstants.ITEM_UPDATED,
				MaithilConstants.ITEM_UPDATED_MESSAGE, response));
	}

	@DeleteMapping("/item")
	public ResponseEntity<MaithilResponse<CartResponse>> deleteItem(@RequestBody UpdateCartItemRequest request) {
		MaithilPrincipal principal = currentUserProvider.getCurrentUser();
		CartResponse response = cartService.deleteItem(request, principal.getUserId());

		return ResponseEntity.ok(MaithilResponse.success(MaithilConstants.ITEM_REMOVED,
				MaithilConstants.ITEM_REMOVED_MESSAGE, response));
	}

	@DeleteMapping("/all")
	public ResponseEntity<MaithilResponse<CartResponse>> clearCart() {
		MaithilPrincipal principal = currentUserProvider.getCurrentUser();
		CartResponse response = cartService.clearCart(principal.getUserId());

		return ResponseEntity.ok(MaithilResponse.success(MaithilConstants.CART_CLEARED_SUCCESS,
				MaithilConstants.CART_CLEARED_MESSAGE, response));
	}

	@GetMapping("/internal/{userId}")
	public ResponseEntity<MaithilResponse<CartResponse>> checkoutCart(@PathVariable String userId) {

		CartResponse response = cartService.getCart(userId);

		return ResponseEntity.ok(MaithilResponse.success(MaithilConstants.CART_FETCHED,
				MaithilConstants.CART_FETCHED_MESSAGE, response));
	}

	@DeleteMapping("/internal/all/{userId}")
	public ResponseEntity<MaithilResponse<CartResponse>> clearCart(@PathVariable String userId) {

		CartResponse response = cartService.clearCart(userId);

		return ResponseEntity.ok(MaithilResponse.success(MaithilConstants.CART_CLEARED_SUCCESS,
				MaithilConstants.CART_CLEARED_MESSAGE, response));
	}
}