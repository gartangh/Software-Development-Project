package eventbroker;

public class EventPublisher {

	public void publishEvent(Event event) {
		EventBroker.getEventBroker().addEvent(this, event);
	}

}
