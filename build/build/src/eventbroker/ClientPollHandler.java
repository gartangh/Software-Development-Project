package eventbroker;

import eventbroker.clientevent.ClientPollAnswerEvent;
import eventbroker.serverevent.ServerPollUserEvent;

public class ClientPollHandler extends EventPublisher implements EventListener {
	
	private final static ClientPollHandler clientPollHandler = new ClientPollHandler(); // Singleton
	private static boolean activated;
	
	private ClientPollHandler() {
		activated = false;
	}

	@Override
	public void handleEvent(Event event) {
		ServerPollUserEvent sPUE = (ServerPollUserEvent) event;
		
		ClientPollAnswerEvent cPAE = new ClientPollAnswerEvent(sPUE.getPollID());
		this.publishEvent(cPAE);
	}
	
	public static void activateClientPollHandler() {
		if(!activated) {
			EventBroker.getEventBroker().addEventListener(ServerPollUserEvent.EVENTTYPE, clientPollHandler);
			activated = true;
		}
	}
	
	public static void disableClientPollHandler() {
		if(activated) {
			EventBroker.getEventBroker().removeEventListener(clientPollHandler);
			activated = false;
		}
	}
	
	public static ClientPollHandler getClientPollHandler() {
		return clientPollHandler;
	}
}
