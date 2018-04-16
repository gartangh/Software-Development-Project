package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import chat.ChatMessage;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.scene.paint.Color;
import quiz.util.ChangeTeamEvent;
import quiz.util.NewTeamEvent;
import quiz.model.Team;
import network.Network;
import quiz.model.MCQuestion;
import quiz.util.ClientCreateAccountEvent;
import quiz.util.ClientCreateQuizEvent;
import quiz.util.ClientGetQuizzesEvent;
import quiz.util.ClientVoteEvent;
import quiz.util.QuizzerEvent;
import user.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import quiz.model.Quiz;
import quiz.model.ScoreboardTeam;
import quiz.util.ClientAnswerEvent;
import quiz.util.ClientNewQuestionEvent;
import quiz.util.ClientVoteEvent;
import quiz.util.Difficulty;
import quiz.util.Theme;

public class Server extends EventPublisher {

	// Inner class
	// TODO: Add handling of all events
	static class ServerHandler implements EventListener {

		@Override
		public void handleEvent(Event e) {
			ArrayList<Integer> destinations = new ArrayList<>();
			Boolean handled = false;
			switch (e.getType()) {
			case "CLIENT_CREATE_ACCOUNT":
				ClientCreateAccountEvent cCAE = (ClientCreateAccountEvent) e;
				int userID = ServerContext.getContext().addUser(cCAE.getUserName(),"");
				ServerContext.getContext().getNetwork().getUserIDConnectionIDMap().put(userID, cCAE.getConnectionID());
				ServerReturnUserIDEvent sRUIDE = new ServerReturnUserIDEvent(userID, cCAE.getConnectionID());
				sRUIDE.addRecipient(userID);
				server.publishEvent(sRUIDE);
				handled = true;
				break;

			case "CLIENT_CREATE_QUIZ":
				ClientCreateQuizEvent cCQE = (ClientCreateQuizEvent) e;
				Quiz quiz = cCQE.getQuiz();
				ServerContext.getContext().getQuizMap().put(quiz.getQuizID(), quiz);
				handled = true;
				break;

			case "CLIENT_VOTE":
				handleClientVoteEvent((ClientVoteEvent) e);
				handled = true;
				break;

			case "CLIENT_ANSWER":
				
				handleClientAnswerEvent((ClientAnswerEvent) e);
				handled = true;
				break;
				
			case "CLIENT_NEW_QUESTION":
				
				handleClientNewQuestionEvent((ClientNewQuestionEvent) e);
				handled = true;
				break;

			case "CLIENT_CHAT":
				ChatMessage chatMessage = (ChatMessage) e;

				// TODO: Add all usernames for the chat

				for (Map.Entry<Integer, Integer> entry : ServerContext.getContext().getNetwork()
						.getUserIDConnectionIDMap().entrySet())
					if (entry.getKey() != chatMessage.getUserID())
						destinations.add(entry.getKey());

				chatMessage.setType("SERVER_CHAT");
				chatMessage.addRecipients(destinations);
				server.publishEvent(chatMessage);
				handled = true;
				break;
			case "CLIENT_SCOREBOARDDATA":
				QuizzerEvent askForScoreboardData = (QuizzerEvent) e;

				ServerScoreboardDataEvent scoreboardData = new ServerScoreboardDataEvent(
						askForScoreboardData.getQuizID());
				scoreboardData.addRecipient(askForScoreboardData.getUserID());
				// Testing code for Scoreboard
				/*
				 * ArrayList<Integer> list = getTeams();
				 * ServerScoreboardDataEvent scoreboardData = new
				 * ServerScoreboardDataEvent(list.get(0));
				 * scoreboardData.removeAllRecipients(); for(Map.Entry<Integer,
				 * Integer> entry : ServerContext.getContext().getNetwork().
				 * getUserIDConnectionIDMap().entrySet()) {
				 * if(!(list.contains(entry.getKey())))
				 * scoreboardData.addRecipient(entry.getKey()); }
				 */
				server.publishEvent(scoreboardData);
				handled = true;
				break;

			case "CLIENT_GET_QUIZZES":
				ClientGetQuizzesEvent cGQE = (ClientGetQuizzesEvent) e;
				ServerGetQuizzesEvent sGQE = new ServerGetQuizzesEvent();
				sGQE.addRecipient(cGQE.getUserID());
				server.publishEvent(sGQE);
				handled = true;
				break;
			}
            case "CLIENT_NEW_TEAM":
				NewTeamEvent newteamevent=(NewTeamEvent) e;
				Team newteam=ServerContext.getContext().addTeam(newteamevent.getQuizID(),newteamevent.getTeamName(),newteamevent.getColor(),newteamevent.getUserID());
				if (newteam != null){
					ServerNewTeamEvent serverNewTeamEvent=new ServerNewTeamEvent(newteamevent.getQuizID(),newteam.getID(),newteam.getName(),newteam.getColor(),newteam.getCaptainID(),newteam.getTeamMembers().get(newteam.getCaptainID()));
					Server.getServer().publishEvent(serverNewTeamEvent);
				}
				break;
			case "CLIENT_CHANGE_TEAM":
				ChangeTeamEvent cte=(ChangeTeamEvent) e;
				String userName=ServerContext.getContext().changeTeam(cte.getQuizID(),cte.getNewTeamID(),cte.getUserID(),'a');
				ServerContext.getContext().changeTeam(cte.getQuizID(),cte.getOldTeamID(),cte.getUserID(),'d');
				if (userName!=null){
					ServerChangeTeamEvent serverChangeTeamEvent=new ServerChangeTeamEvent(cte.getQuizID(),cte.getNewTeamID(),cte.getOldTeamID(),cte.getUserID(),userName);
					Server.getServer().publishEvent(serverChangeTeamEvent);
				}
				break;
				//TODO oldteam (check for null) and newteam modifien
			
			if(handled) System.out.println("Event received and handled: "+e.getType());
			else System.out.println("Event received but left unhandled: "+e.getType());
		}
			
		public void handleClientVoteEvent(ClientVoteEvent cVE){	
			Quiz quiz = ServerContext.getContext().getQuiz(cVE.getQuizID());
			quiz.addVote(cVE.getUserID(), cVE.getTeamID(), cVE.getVote());
			
			ServerVoteEvent sVE = new ServerVoteEvent(cVE.getUserID(), cVE.getTeamID(), cVE.getQuizID(), cVE.getVote());
			Server.getServer().publishEvent(sVE);			
		}
		
		public void handleClientAnswerEvent(ClientAnswerEvent cAE){	
			Quiz quiz = ServerContext.getContext().getQuiz(cAE.getQuizID());
			quiz.addAnswer(cAE.getTeamID(), cAE.getQuestionID(), cAE.getAnswer());
			
			MCQuestion mCQ = (MCQuestion) ServerContext.getContext().getQuestion(cAE.getQuestionID());
			ServerAnswerEvent serverAnswer = new ServerAnswerEvent(cAE.getTeamID(), cAE.getQuestionID(), cAE.getAnswer(), mCQ.getCorrectAnswer());
			Server.getServer().publishEvent(serverAnswer);
		}
		
		public void handleClientNewQuestionEvent(ClientNewQuestionEvent cNQE){
			Quiz quiz = ServerContext.getContext().getQuiz(cNQE.getQuizID());
			MCQuestion nQ = (MCQuestion) ServerContext.getContext().getQuestion(quiz.getRound().getNextQuestion());
			int[] permutatie = {1,2,3,4};
			
			ServerNewQuestionEvent sNQE = new ServerNewQuestionEvent(nQ.getQuestionID(), nQ.getQuestion(), nQ.getAnswers(), permutatie);
			Server.getServer().publishEvent(sNQE);
		}
	}
	
	private final static Server server = new Server();
	private static ServerHandler serverHandler = new ServerHandler();
	
	private Server() {
	}
	
	public static void main(String[] args) {
		Network network = new Network(1025, "SERVER");
		ServerContext.getContext().setNetwork(network);
		EventBroker.getEventBroker().addEventListener(network);
		EventBroker.getEventBroker().addEventListener(serverHandler);
		EventBroker.getEventBroker().start();
		
		int andreID = ServerContext.getContext().addUser("André", "");
		int quizID = ServerContext.getContext().addQuiz("Testquiz", 8, 4, 1, 4, andreID);
		ServerContext.getContext().addTeam(quizID, "André en de boys", Color.BLUE, andreID);
		ServerContext.getContext().loadData();
		ServerContext.getContext().getQuiz(quizID).addRound(Difficulty.EASY, Theme.CULTURE);
        
        /*EventBroker.getEventBroker().start(); // Hannes Trash Test
        System.out.println(InetAddress.getLocalHost());
        Network network = new Network(1025);
        EventBroker.getEventBroker().addEventListener(network);
        EventBroker.getEventBroker().addEventListener(serverHandler);
        ServerContext.getContext().addQuizwithQuizID(1);
        ServerContext.getContext().addUserwithUserID(1);*/
		
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		
		MCQuestion nQ = (MCQuestion) ServerContext.getContext().getQuestion(ServerContext.getContext().getQuiz(quizID).getRound().getNextQuestion());
		int[] permutatie = {1,2,3,4};
		ServerNewQuestionEvent sNQ = new ServerNewQuestionEvent(nQ.getQuestionID(), nQ.getQuestion(), nQ.getAnswers(), permutatie);
		Server.getServer().publishEvent(sNQ);
	}
	
	public static Server getServer() {
		return server;
	}

	/*
	 * TEST
	 */
	/*
	 * private static ArrayList<Integer> getTeams() { ArrayList<Integer> test =
	 * new ArrayList<>(); int userID1 =
	 * ServerContext.getContext().addUser("test1", "test1")); int quizID =
	 * ServerContext.getContext().addQuiz(5, 2, 1, 5, userID1); int userID2 =
	 * ServerContext.getContext().addUser("test2", "test2"); int userID3 =
	 * ServerContext.getContext().addUser("test3", "test3"); int teamID1 =
	 * ServerContext.getContext().addTeam(quizID, "chill", Color.GREEN,
	 * userID2); int teamID2 = ServerContext.getContext().addTeam(quizID,
	 * "whieoe", Color.BLACK, userID3);
	 * ServerContext.getContext().getQuizMap().get(quizID).getTeams().get(
	 * teamID1).setQuizScore(100);
	 * ServerContext.getContext().getQuizMap().get(quizID).getTeams().get(
	 * teamID2).setTeamID(0);
	 * ServerContext.getContext().getQuizMap().get(quizID).getTeams().get(0).
	 * setQuizScore(200);
	 * 
	 * test.add(quizID); test.add(userID1); test.add(userID2);
	 * test.add(userID3); return test; }
	 */

}