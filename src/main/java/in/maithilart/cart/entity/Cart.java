package in.maithilart.cart.entity;

import java.io.Serializable;
import java.time.Instant;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart", schema = "cart")
public class Cart implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "cart_id", nullable = false, updatable = false)
	private UUID cartId;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "status", nullable = false, length = 30)
	private String status;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	public Cart() {
	}

	@PrePersist
	public void prePersist() {

		if (cartId == null) {
			cartId = UUID.randomUUID();
		}

		createdAt = Instant.now();
		updatedAt = Instant.now();
	}

	@PreUpdate
	public void preUpdate() {

		updatedAt = Instant.now();
	}

	public UUID getCartId() {
		return cartId;
	}

	public void setCartId(UUID cartId) {
		this.cartId = cartId;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}
}
