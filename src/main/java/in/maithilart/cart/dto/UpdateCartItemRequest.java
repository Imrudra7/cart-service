package in.maithilart.cart.dto;

import java.util.UUID;

public class UpdateCartItemRequest {

	private UUID cartId;
	private UUID cartItemId;
	private String method; // Increase -- Decrease 

	public UUID getCartId() {
		return cartId;
	}

	public void setCartId(UUID cartId) {
		this.cartId = cartId;
	}

	public UUID getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(UUID cartItemId) {
		this.cartItemId = cartItemId;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
