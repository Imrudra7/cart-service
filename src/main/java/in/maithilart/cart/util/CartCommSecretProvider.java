package in.maithilart.cart.util;

import org.springframework.beans.factory.annotation.Value;

import in.maithilart.common.context.provider.CommunicatorSecretProvider;

public class CartCommSecretProvider implements CommunicatorSecretProvider {

	@Value("${communicator.secret}")
	private String secret;

	@Override
	public String getCommunicatorSecret() {
		return secret == null ? "CART-COMM_SECRET" : secret;
	}

}
