package in.maithilart.cart.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CartResponse {

    private UUID cartId;

    private String status;

    private List<CartItemResponse> items;
    
    private BigDecimal itemTotalAmount;
    
    private BigDecimal discountedItemTotalAmount;
    
    private BigDecimal handlingFee;
    
    private BigDecimal deliveryPartnerFee;
    
    private BigDecimal platformFee;
    
    private BigDecimal toPay;

	public UUID getCartId() {
		return cartId;
	}

	public void setCartId(UUID cartId) {
		this.cartId = cartId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<CartItemResponse> getItems() {
		return items;
	}

	public void setItems(List<CartItemResponse> items) {
		this.items = items;
	}

	public BigDecimal getItemTotalAmount() {
		return itemTotalAmount;
	}

	public void setItemTotalAmount(BigDecimal itemTotalAmount) {
		this.itemTotalAmount = itemTotalAmount;
	}

	public BigDecimal getDiscountedItemTotalAmount() {
		return discountedItemTotalAmount;
	}

	public void setDiscountedItemTotalAmount(BigDecimal discountedItemTotalAmount) {
		this.discountedItemTotalAmount = discountedItemTotalAmount;
	}

	public BigDecimal getHandlingFee() {
		return handlingFee;
	}

	public void setHandlingFee(BigDecimal handlingFee) {
		this.handlingFee = handlingFee;
	}

	public BigDecimal getDeliveryPartnerFee() {
		return deliveryPartnerFee;
	}

	public void setDeliveryPartnerFee(BigDecimal deliveryPartnerFee) {
		this.deliveryPartnerFee = deliveryPartnerFee;
	}

	public BigDecimal getPlatformFee() {
		return platformFee;
	}

	public void setPlatformFee(BigDecimal platformFee) {
		this.platformFee = platformFee;
	}

	public BigDecimal getToPay() {
		return toPay;
	}

	public void setToPay(BigDecimal toPay) {
		this.toPay = toPay;
	}
    
    
}
