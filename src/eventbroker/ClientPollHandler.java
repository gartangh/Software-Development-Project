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
		
		System.out.println("received Poll");
		
		ClientPollAnswerEvent cPAE = new ClientPollAnswerEvent(sPUE.getPollID());
		this.publishEvent(cPAE);
	}
	
	public static void activateClientPollHandler() {
		System.out.println("tried to activate");
		if(!activated) {
			EventBroker.getEventBroker().addEventListener(ServerPollUserEvent.EVENTTYPE, clientPollHandler);
			activated = true;
			System.out.println("did it");
		}
	}
	
	public static void disableClientPollHandler() {
		System.out.println("tried to disable");
		if(activated) {
			EventBroker.getEventBroker().removeEventListener(clientPollHandler);
			activated = false;
			System.out.println("did it");
		}
	}
	
	public static ClientPollHandler getClientPollHandler() {
		return clientPollHandler;
	}
}
