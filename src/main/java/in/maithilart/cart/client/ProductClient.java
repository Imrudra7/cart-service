package in.maithilart.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import in.maithilart.cart.client.dto.ProductResponse;
import in.maithilart.cart.config.FeignConfig;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "product-service", url = "${product.service.url}", configuration = FeignConfig.class)
public interface ProductClient {

	@PostMapping("/product/api/internal/bulk")
	List<ProductResponse> getProducts(@RequestBody List<UUID> productIds);
	
	@GetMapping("/product/api/internal/{id}")
	List<ProductResponse> getProductById(@PathVariable UUID id);
}