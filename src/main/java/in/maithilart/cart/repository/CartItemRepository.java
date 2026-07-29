package in.maithilart.cart.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.maithilart.cart.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

	List<CartItem> findByCartId(UUID cartId);
	
	List<CartItem> findByCartIdOrderByCreatedAtAsc(UUID cartId);
	
	Optional<CartItem> findByCartIdAndProductIdAndVariantId(UUID cartId, UUID productId, UUID variantId);

	void deleteByCartId(UUID cartId);

	void deleteByCartIdAndProductIdAndVariantId(UUID cartId, UUID productId, UUID variantId);

	Optional<CartItem> findByCartIdAndProductIdAndVariantIdOrderByCreatedAtAsc(UUID cartId, UUID productId,
			UUID variantId);
}