package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientEndQuizEvent extends ClientQuizzerEvent {
	
	public final static String EVENTTYPE = "CLIENT_END_QUIZ";
	
	public ClientEndQuizEvent() {
		super.type = EVENTTYPE;
	}

}
