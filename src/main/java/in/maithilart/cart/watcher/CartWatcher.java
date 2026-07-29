package in.maithilart.cart.watcher;

import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import in.maithilart.cart.service.ICartService;
import in.maithilart.common.constants.MaithilConstants;
import in.maithilart.common.context.provider.MicroserviceNameProvider;
import in.maithilart.common.dto.DeliveryRecord;
import in.maithilart.common.event.poller.MaithilEventPoller;
import in.maithilart.common.event.util.Messenger;
import in.maithilart.common.exception.MaithilException;

@Service
public class CartWatcher {

	private final MaithilEventPoller eventPoller;
	private final MicroserviceNameProvider microserviceNameProvider;
	private final Messenger messenger;
	private final ICartService cartService;

	public CartWatcher(MaithilEventPoller eventPoller, Messenger messenger,
			MicroserviceNameProvider microserviceNameProvider, ICartService cartService) {
		this.eventPoller = eventPoller;
		this.microserviceNameProvider = microserviceNameProvider;
		this.messenger = messenger;
		this.cartService = cartService;
	}

	@Scheduled(fixedDelay = 3000)
	public void poll() {
		System.out.println(microserviceNameProvider.getMicroservicename() + " Watching 👀⌚⌚⌚");
		List<DeliveryRecord> deliveries = eventPoller
				.pollPendingDeliveries(microserviceNameProvider.getMicroservicename(), 50);
		if (null != deliveries && !deliveries.isEmpty()) {
			System.out.println("👀👀👁️👁️Event Found: " + deliveries);
		}
		for (DeliveryRecord delivery : deliveries) {

			try {
				process(delivery);
				eventPoller.markSuccess(delivery.getDeliveryId());

			} catch (Exception ex) {
				eventPoller.markFailed(delivery, ex.getMessage(),ex);
			}

		}
	}

	private void process(DeliveryRecord delivery) {

		switch (delivery.getEventType()) {

		case MaithilConstants.PAYMENT_SUCCESS -> handlePaymentSuccess(delivery);

		case MaithilConstants.ORDER_CANCELLED -> handleOrderCancelled(delivery);

		case MaithilConstants.ORDER_CREATED -> handleOrderCreated(delivery);

		default -> throw new IllegalArgumentException("Unsupported Event : " + delivery.getEventType());
		}
	}

	private Object handleOrderCreated(DeliveryRecord delivery) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object handleOrderCancelled(DeliveryRecord delivery) {
		// TODO Auto-generated method stub
		return null;
	}

	private void handlePaymentSuccess(DeliveryRecord delivery) {

		String payload = delivery.getPayload();

		Map<String, Object> data = messenger.unpack(payload);

		String userId = (String) data.get("userId");
		try {
			cartService.clearCart(userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MaithilException(MaithilConstants.FAILED_STATUS, MaithilConstants.CART_CLEAR_FAIL_MESSAGE);

		}
		System.out.println("Cart with UserId:"+userId+" got cleared!!");
	}
}
