package server;

import java.util.ArrayList;
import java.util.Map;

import chat.ChatMessage;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import network.Network;
import quiz.util.ClientCreateEvent;
import quiz.util.ClientVoteEvent;
import quiz.util.QuizzerEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import quiz.model.Quiz;
import quiz.model.ScoreboardTeam;
import quiz.util.ClientAnswerEvent;

public class Server extends EventPublisher {

	// Singleton
	private final static Server server = new Server();
	private final static ServerHandler serverHandler = new ServerHandler();

	public static void main(String[] args) {
		Network network = new Network(1025, "SERVER");
		ServerContext.getContext().setNetwork(network);

		EventBroker.getEventBroker().addEventListener(serverHandler);
		EventBroker.getEventBroker().addEventListener(network);
		EventBroker.getEventBroker().start();
	}

	// Inner class
	// TODO: Add handling of all events
	static class ServerHandler implements EventListener {

		@Override
		public void handleEvent(Event e) {
			ArrayList<String> destinations = new ArrayList<>();

			switch (e.getType()) {
			case "CLIENT_VOTE":
				ClientVoteEvent clientVote = (ClientVoteEvent) e;
				Quiz quiz0 = ServerContext.getContext().getQuiz(clientVote.getQuizname());
				quiz0.addVote(clientVote.getUsername(), clientVote.getTeamname(), clientVote.getVote());
				ServerVoteEvent serverVote = new ServerVoteEvent(clientVote.getUsername(), clientVote.getTeamname(),
						clientVote.getVote());

				// TODO: Add recipients for vote (teamMembers)

				server.publishEvent(serverVote);
				System.out.println("Event received and handled: " + e.getType());
				break;
			case "CLIENT_ANSWER":
				ClientAnswerEvent clientAnswer = (ClientAnswerEvent) e;
				Quiz quiz1 = ServerContext.getContext().getQuiz(clientAnswer.getQuizname());
				quiz1.addAnswer(clientAnswer.getTeamname(), clientAnswer.getQuestionID(), clientAnswer.getAnswer());

				// TODO: Search correct answer for question

				ServerAnswerEvent serverAnswer = new ServerAnswerEvent(clientAnswer.getTeamname(),
						clientAnswer.getQuestionID(), clientAnswer.getAnswer(), 3);

				// TODO: Add recipients for answer

				server.publishEvent(serverAnswer);
				System.out.println("Event received and handled: " + e.getType());
				break;
			case "SERVER_CLIENT_CREATE":
				ClientCreateEvent clientCreate = (ClientCreateEvent) e;
				ServerContext.getContext().addUser(clientCreate.getUser());
				ServerContext.getContext().getNetwork().getUserIDConnectionIDMap().put(clientCreate.getUsername(),
						clientCreate.getConnectionID());
				ServerReturnUserIDEvent returnIDEvent = new ServerReturnUserIDEvent(clientCreate.getUsername(),
						clientCreate.getConnectionID());
				returnIDEvent.addRecipient(clientCreate.getUsername());
				server.publishEvent(returnIDEvent);
				System.out.println("Event received and handled: " + e.getType());
				break;
			case "CLIENT_CHAT":
				ChatMessage chatMessage = (ChatMessage) e;

				// TODO: Add all usernames for the chat

				for (Map.Entry<String, Integer> entry : ServerContext.getContext().getNetwork()
						.getUserIDConnectionIDMap().entrySet())
					if (entry.getKey() != chatMessage.getUsername())
						destinations.add(entry.getKey());

				chatMessage.setType("SERVER_CHAT");
				chatMessage.addRecipients(destinations);
				server.publishEvent(chatMessage);
				System.out.println("Event received and handled: " + e.getType());
				break;
			default:
				System.out.println("Event received but left unhandled: " + e.getType());
				break;
          
          case "CLIENT_SCOREBOARDDATA":
					QuizzerEvent askForScoreboardData = (QuizzerEvent) e;
					
					ServerScoreboardDataEvent scoreboardData = new ServerScoreboardDataEvent(askForScoreboardData.getQuizID());
					// Testing code for Scoreboard
					/*ArrayList<Integer> list = getTeams();
					ServerScoreboardDataEvent scoreboardData = new ServerScoreboardDataEvent(list.get(0));
					scoreboardData.removeAllRecipients();
					for(Map.Entry<Integer, Integer> entry : ServerContext.getContext().getNetwork().getUserIDConnectionIDMap().entrySet()) {
						if(!(list.contains(entry.getKey())))
							scoreboardData.addRecipient(entry.getKey());
					}*/
					Server.getServer().publishEvent(scoreboardData);
					break;
					
				default:
					System.out.println("Event received but left unhandled: " + e.getType());
					break;
			}
		}
	}
	
	/*
	 * TEST
	 */
	private static ArrayList<Integer> getTeams() {
		ArrayList<Integer> test = new ArrayList<>();
		int userID1 = ServerContext.getContext().addUser("test1", "test1");
		int quizID = ServerContext.getContext().addQuiz(5, 2, 1, 5, userID1);
		int userID2 = ServerContext.getContext().addUser("test2", "test2");
		int userID3 = ServerContext.getContext().addUser("test3", "test3");
		int teamID1 = ServerContext.getContext().addTeam(quizID, "chill", Color.GREEN, userID2);
		int teamID2 = ServerContext.getContext().addTeam(quizID, "whieoe", Color.BLACK, userID3);
		ServerContext.getContext().getQuizMap().get(quizID).getTeams().get(teamID1).setQuizScore(100);
		ServerContext.getContext().getQuizMap().get(quizID).getTeams().get(teamID2).setTeamID(0);
		ServerContext.getContext().getQuizMap().get(quizID).getTeams().get(0).setQuizScore(200);
		
		test.add(quizID);
		test.add(userID1);
		test.add(userID2);
		test.add(userID3);
		return test;
	}


}
