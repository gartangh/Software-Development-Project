package server;

import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import network.Network;
import quiz.model.Quiz;
import quiz.model.Team;
import user.model.Host;
import user.model.User;

public class ServerContext {

	// Singleton
	private static ServerContext context = new ServerContext();

	private Map<Integer, User> userMap = new HashMap<Integer, User>();
	private Map<Integer, Quiz> quizMap = new HashMap<Integer, Quiz>();
	private Network network;

	// Constructors
	private ServerContext() {
		// Empty default constructor
	}

	public static ServerContext getContext() {
		return context;
	}

	public void addUser(String username, String password) {
		int newID;
		do {
			newID = (int) (Math.random() * Integer.MAX_VALUE);
		} while(userMap.containsKey(newID));
		User newUser = new User(newID, username, password);
		userMap.put(newID, newUser);
	}

	public void addQuiz(int maxAmountOfTeams, int maxAmountOfPlayersPerTeam, int maxAmountOfRounds, int maxAmountOfQuestionsPerRound, int hostID) {
		int newID;
		do {
			newID = (int) (Math.random() * Integer.MAX_VALUE);
		} while(quizMap.containsKey(newID));

		Quiz newQuiz = new Quiz(newID, maxAmountOfTeams, maxAmountOfPlayersPerTeam, maxAmountOfRounds, maxAmountOfQuestionsPerRound, hostID);
		quizMap.put(newID, newQuiz);
	}

	//testing code
	public void addQuizwithQuizID(int quizID){
		Quiz quiz=new Quiz(1,5,5,5,5,20);
		quizMap.put(quizID,quiz);
		Team team1 = new Team(1,"Deborah leemans",Color.rgb(0,0,255),2,"james",quiz.getMaxAmountOfPlayersPerTeam());
		Team team2 = new Team(2,"Team2",Color.rgb(255,0,0),4,"Precious",quiz.getMaxAmountOfPlayersPerTeam());
		quiz.addTeam(team1);
		quiz.addTeam(team2);
	}

	public void addUserwithUserID(int userID){
		userMap.put(userID,new User(1,"hannes","1234"));
	}

	public Team addTeam(int quizID, String teamName, Color color, int captainID) {
		if(quizMap.containsKey(quizID) && userMap.containsKey(captainID)) {
			Quiz q = quizMap.get(quizID);

			int newID;
			boolean unique;
			do {
				unique=true;
				newID = (int) (Math.random() * Integer.MAX_VALUE);
				for(Team t : q.getTeams().values()) {
					if(t.getID() == newID) unique = false;
				}
			} while(!unique);

			//Team team = new Team(newID, teamName, color, captainID, userMap.get(captainID).getUsername());
			Team team = new Team(newID, teamName, color, captainID, userMap.get(captainID).getUsername());
			team.setMaxAmountOfPlayers(q.getMaxAmountOfPlayersPerTeam());
			q.addTeam(team);
			quizMap.put(quizID, q);
			return team;
		}
		else return null;
	}

	public String changeTeam(int quizID,int teamID,int userID, char type){//returns username for serverEventHandler
		if (quizMap.containsKey(quizID) && userMap.containsKey(userID) && teamID!=-1){//teamID==-1: nothing to delete
			Quiz quiz=quizMap.get(quizID);
			User user=userMap.get(userID);
			Team team=null;
			boolean foundTeam=false;
			int i=0;

			while (!foundTeam && i<quiz.getTeams().size()){
				team=quiz.getTeams().get(i);
				if (team.getID()==teamID){
					foundTeam=true;
				}
				i++;
			}

			if (foundTeam){
				if (type=='a'){//add
					team.addTeamMember(user.getID(),user.getUsername());
					return user.getUsername();
				}
				else if (type=='d'){//Delete
					team.removeTeamMember(user.getID(),user.getUsername());
					return user.getUsername();
				}
			}
		} //team, quiz or user not found
		return null;

	}
}