package server.timertask;

import java.util.ArrayList;
import java.util.Map;
import java.util.TimerTask;

import eventbroker.EventPublisher;
import eventbroker.serverevent.ServerQuestionTimeEvent;
import eventbroker.serverevent.ServerVoteAnswerEvent;
import eventbroker.timerevent.DurationTimerEvent;
import quiz.model.IPQuestion;
import quiz.model.MCQuestion;
import quiz.model.Team;
import quiz.util.RoundType;
import server.ServerContext;

public class QuestionDurationTimerTask extends TimerTask {

	public static final int MAX_DURATION = 30;

	private int quizID;
	private int questionID;
	private int seconds = 0;
	private EventPublisher eventPublisher = new EventPublisher();

	public QuestionDurationTimerTask(int quizID, int questionID) {
		super();
		this.quizID = quizID;
		this.questionID = questionID;
	}

	@Override
	public void run() {
		seconds++;
		if(seconds <= MAX_DURATION) {
			DurationTimerEvent tDE = new DurationTimerEvent(seconds,quizID, questionID);
			eventPublisher.publishEvent(tDE);
		}
		else {
			this.cancel();
		}
	}

}
