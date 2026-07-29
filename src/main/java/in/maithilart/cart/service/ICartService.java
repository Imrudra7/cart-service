package in.maithilart.cart.service;

import in.maithilart.cart.dto.AddCartItemRequest;
import in.maithilart.cart.dto.CartResponse;
import in.maithilart.cart.dto.UpdateCartItemRequest;

public interface ICartService {

	CartResponse getCart(String userId);

	CartResponse updateCart(UpdateCartItemRequest request, String userId);

	CartResponse clearCart(String userId);

	CartResponse deleteItem(UpdateCartItemRequest request, String userId);

	CartResponse addToCart(AddCartItemRequest request, String userId);

}
