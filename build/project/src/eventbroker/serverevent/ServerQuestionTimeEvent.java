package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerQuestionTimeEvent extends ServerEvent {
	public static final String EVENTTYPE = "SERVER_QUESTION_TIME";
	
	private int questionID;
	private int maxTime;
	private int currentTime;
	
	public ServerQuestionTimeEvent(int questionID, int currentTime, int maxTime) {
		super.type = EVENTTYPE;
		this.questionID = questionID;
		this.currentTime = currentTime;
		this.maxTime = maxTime;
	}
	
	public int getQuestionID() {
		return this.questionID;
	}
	
	public int getCurrentTime() {
		return this.currentTime;
	}
	
	public int getMaxTime() {
		return this.maxTime;
	}
}
