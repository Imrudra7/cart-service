package in.maithilart.cart.dto;

import java.util.UUID;

public class AddCartItemRequest {

    private UUID productId;

    private UUID variantId;

    private Integer quantity;

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public UUID getVariantId() {
		return variantId;
	}

	public void setVariantId(UUID variantId) {
		this.variantId = variantId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
    
    
    
}