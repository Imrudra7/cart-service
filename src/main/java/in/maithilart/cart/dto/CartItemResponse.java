package in.maithilart.cart.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CartItemResponse {

    private UUID cartItemId;

    private UUID productId;

    private UUID variantId;
    
    private String productName;

    private String imageUrl;

    private String variantLabel;


    private Integer quantity;
    
    private BigDecimal packSize; // 1, 500

    private String uom; // piece , ml
    
    private BigDecimal unitPrice;
    
    private BigDecimal discountedPrice;
    
    private BigDecimal value;

	public UUID getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(UUID cartItemId) {
		this.cartItemId = cartItemId;
	}

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
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getVariantLabel() {
		return variantLabel;
	}

	public void setVariantLabel(String variantLabel) {
		this.variantLabel = variantLabel;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPackSize() {
		return packSize;
	}

	public void setPackSize(BigDecimal packSize) {
		this.packSize = packSize;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(BigDecimal discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	} 
    
    
}
