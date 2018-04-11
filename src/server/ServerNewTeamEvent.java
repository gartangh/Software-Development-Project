package server;

import quiz.model.Team;

public class ServerNewTeamEvent extends ServerEvent{
	protected int quizID;
	protected Team team;

	public ServerNewTeamEvent(int quizID,Team team) {
		super();
		this.quizID=quizID;
		this.team=team;
		this.type = "SERVER_NEW_TEAM";
	}

	public int getQuizID(){
		return quizID;
	}

	public Team getTeam(){
		return team;
	}
}
