package eventbroker.timerevent;

import eventbroker.Event;

@SuppressWarnings("serial")
public class DurationTimerEvent extends Event {

	public final static String EVENTTYPE = "SERVER_TIMER_DURATION";

	private int seconds;
	private int quizID;
	private int questionID;

	// Constructors
	public DurationTimerEvent(int seconds, int quizID, int questionID) {
		super.type = EVENTTYPE;
		this.seconds = seconds;
		this.quizID = quizID;
		this.questionID = questionID;
	}

	public int getSeconds() {
		return seconds;
	}

	public int getQuizID() {
		return quizID;
	}

	public int getQuestionID() {
		return questionID;
	}

}
