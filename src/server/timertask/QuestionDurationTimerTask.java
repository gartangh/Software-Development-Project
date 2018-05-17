package server.timertask;

import java.util.TimerTask;

import eventbroker.EventPublisher;
import eventbroker.timerevent.DurationTimerEvent;

public class QuestionDurationTimerTask extends TimerTask {

	public static final int MAX_DURATION = 30;

	private int quizID;
	private int questionID;
	private int seconds = 0;
	private EventPublisher eventPublisher = new EventPublisher();

	public QuestionDurationTimerTask(int quizID, int questionID) {
		this.quizID = quizID;
		this.questionID = questionID;
	}

	@Override
	public void run() {
		seconds++;
		if (seconds <= MAX_DURATION) {
			DurationTimerEvent tDE = new DurationTimerEvent(seconds,quizID, questionID);
			eventPublisher.publishEvent(tDE);
		}
		else
			this.cancel();
	}

}
