package eventbroker.timerevent;

import eventbroker.Event;

@SuppressWarnings("serial")
public class IPTimerEvent extends Event {

	public final static String EVENTTYPE = "SERVER_TIMER_IP";

	private int quizID;
	private int questionID;
	private int pixelSize;

	// Constructor
	public IPTimerEvent(int pixelSize, int quizID, int questionID) {
		super.type = EVENTTYPE;
		this.pixelSize = pixelSize;
		this.quizID = quizID;
		this.questionID = questionID;
	}

	// Getters
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
