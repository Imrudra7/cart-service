package in.maithilart.cart.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.maithilart.cart.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

	Optional<Cart> findByUserIdAndStatus(UUID userId, String status);

	boolean existsByUserIdAndStatus(UUID userId, String status);
}