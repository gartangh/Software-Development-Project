package server;

@SuppressWarnings("serial")
public class ServerEndQuizEvent extends ServerEvent{

	public ServerEndQuizEvent() {
		this.type = "SERVER_END_QUIZ";
	}
}
