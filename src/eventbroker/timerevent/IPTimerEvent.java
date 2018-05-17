package eventbroker.timerevent;

import eventbroker.Event;
import eventbroker.serverevent.ServerEvent;

public class IPTimerEvent extends Event {

	public final static String EVENTTYPE = "SERVER_TIMER_IP";

	private int quizID;
	private int questionID;
	private int pixelSize;

	// Constructors
	public IPTimerEvent(int pixelSize, int quizID, int questionID) {
		super.type = EVENTTYPE;
		this.pixelSize = pixelSize;
		this.quizID = quizID;
		this.questionID = questionID;
	}

	public int getQuizID() {
		return quizID;
	}

	public int getQuestionID() {
		return questionID;
	}

	public int getPixelSize() {
		return pixelSize;
	}

}
