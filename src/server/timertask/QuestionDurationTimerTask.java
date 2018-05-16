package server.timertask;

import java.util.ArrayList;
import java.util.Map;
import java.util.TimerTask;

import eventbroker.EventPublisher;
import eventbroker.serverevent.ServerQuestionTimeEvent;
import eventbroker.serverevent.ServerVoteAnswerEvent;
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
			ServerQuestionTimeEvent sQTE = new ServerQuestionTimeEvent(questionID, seconds, MAX_DURATION);
			
			ArrayList<Integer> receivers = new ArrayList<>();
			ArrayList <Integer> users = ServerContext.getContext().getUsersFromQuiz(quizID);
			if(users != null) {
				receivers.addAll(ServerContext.getContext().getUsersFromQuiz(quizID));
				receivers.add(ServerContext.getContext().getQuiz(quizID).getHostID());
				sQTE.addRecipients(receivers);
			}
			else {
				this.cancel();
			}
			
			if(seconds == MAX_DURATION) {
				ArrayList<Integer> unansweredTeams = ServerContext.getContext().getQuiz(quizID).fillWrongAnswers(questionID);
				ArrayList<Integer> unansweredUsers = new ArrayList<Integer>();
				
				Map<Integer, Team> teamMap = ServerContext.getContext().getQuiz(quizID).getTeamMap();
				int qType = ServerContext.getContext().getQuestionTypeMap().get(this.questionID);
				int correctAnswer;
				
				if(qType == RoundType.IP.ordinal()) {
					IPQuestion ipQ = (IPQuestion) ServerContext.getContext().getQuestion(this.questionID);
					correctAnswer = ipQ.getCorrectAnswer();
				}
				else {
					MCQuestion mcQ = (MCQuestion) ServerContext.getContext().getQuestion(this.questionID);
					correctAnswer = mcQ.getCorrectAnswer();
				}
				
				for(int teamID : unansweredTeams) {
					unansweredUsers.clear();
					unansweredUsers.addAll(teamMap.get(teamID).getPlayerMap().keySet());
					ServerVoteAnswerEvent sVAE =new ServerVoteAnswerEvent(teamID, this.questionID, -1, correctAnswer, 0);
					sVAE.addRecipients(unansweredUsers);
					this.eventPublisher.publishEvent(sVAE);
				}
				
			}
			
			eventPublisher.publishEvent(sQTE);
		}
		else {
			this.cancel();
		}
	}
	
}
