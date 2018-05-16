package eventbroker.serverevent;

import java.util.Map.Entry;

import quiz.model.Quiz;
import quiz.model.Team;

@SuppressWarnings("serial")
public class ServerJoinQuizEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_JOIN_QUIZ";

	private Quiz quiz;

	// Constructor
	public ServerJoinQuizEvent(Quiz q) {
		super.type = EVENTTYPE;
		//It has to be like this or the object can't be serializiable again, java takes old value!!!!!!
		this.quiz = Quiz.createQuiz(q.getQuizID(),q.getQuizname(),q.getRounds(),q.getPlayers(),q.getHostID(),q.getHostID(),q.getHostname());

		for (Entry<Integer,String> entry : q.getUnassignedPlayers().entrySet()){
			 quiz.addUnassignedPlayer(entry.getKey(),entry.getValue());
		}

		for (Entry<Integer,Team> oldTeam : q.getTeamMap().entrySet()){
			Team team=new Team(oldTeam.getValue());
			for (Entry<Integer,String> user: oldTeam.getValue().getPlayerMap().entrySet())
				team.getPlayerMap().put(user.getKey(),user.getValue());
			quiz.getTeamMap().put(oldTeam.getKey(),team);
		}

	}

	// Getter
	public Quiz getQuiz() {
		return quiz;
	}

}
