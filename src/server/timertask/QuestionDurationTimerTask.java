package server.timertask;

import java.util.ArrayList;
import java.util.TimerTask;

import eventbroker.EventPublisher;
import eventbroker.serverevent.ServerQuestionTimeEvent;
import eventbroker.serverevent.ServerVoteAnswerEvent;
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
			ServerQuestionTimeEvent sQTE = new ServerQuestionTimeEvent(questionID, seconds, MAX_DURATION);
			
			ArrayList<Integer> receivers = new ArrayList<>();
			receivers.addAll(ServerContext.getContext().getUsersFromQuiz(quizID));
			receivers.add(ServerContext.getContext().getQuiz(quizID).getHostID());
			sQTE.addRecipients(receivers);
			
			if(seconds == MAX_DURATION) {
				ServerContext.getContext().getQuiz(quizID).fillWrongAnswers(questionID);
			}
			
			eventPublisher.publishEvent(sQTE);
		}
		else {
			this.cancel();
		}
	}
	
}
