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
import javafx.scene.paint.Color;
import quiz.model.Quiz;
import quiz.util.ClientAnswerEvent;

public class Server extends EventPublisher{
	
	public static class ServerHandler implements EventListener{ // TODO: add handling of all events 
	
		@Override
		public void handleEvent(Event e){
			ArrayList<Integer> destinations = new ArrayList<>();
			switch(e.getType()) {
			case "CLIENT_VOTE":
				ClientVoteEvent clientVote = (ClientVoteEvent) e;
				Quiz quiz0 = ServerContext.getContext().getQuiz(clientVote.getQuizID());
				quiz0.addVote(clientVote.getUserID(), clientVote.getTeamID(), clientVote.getVote());
				ServerVoteEvent serverVote = new ServerVoteEvent(clientVote.getUserID(), clientVote.getTeamID(), clientVote.getQuizID(), clientVote.getVote());
				// TO DO: add recipients for vote (teamMembers)
				Server.getServer().publishEvent(serverVote);
				System.out.println("Event received and handled: " + e.getType());
				break;
			
			case "CLIENT_ANSWER":
				
				ClientAnswerEvent clientAnswer = (ClientAnswerEvent) e;
				Quiz quiz1 = ServerContext.getContext().getQuiz(clientAnswer.getQuizID());
				quiz1.addAnswer( clientAnswer.getTeamID(), clientAnswer.getQuestionID(), clientAnswer.getAnswer());
				
				// TODO: search correct answer for question
				
				ServerAnswerEvent serverAnswer = new ServerAnswerEvent(clientAnswer.getTeamID(), clientAnswer.getQuestionID(), clientAnswer.getAnswer(), 3);
				// TO DO: add recipients for answer
				Server.getServer().publishEvent(serverAnswer);
				System.out.println("Event received and handled: " + e.getType());
				break;
                    
            case "SERVER_CLIENT_CREATE":
				ClientCreateEvent clientCreate = (ClientCreateEvent) e;
				int userID = ServerContext.getContext().addUser(clientCreate.getUsername(), clientCreate.getPassword());
				ServerContext.getContext().getNetwork().getUserIDConnectionIDMap().put(userID, clientCreate.getConnectionID());
				ServerReturnUserIDEvent returnIDEvent = new ServerReturnUserIDEvent(userID, clientCreate.getConnectionID());
				returnIDEvent.addRecipient(userID);
				Server.getServer().publishEvent(returnIDEvent);
				System.out.println("Event received and handled: " + e.getType());
				break;
				
			case "CLIENT_CHAT":
				ChatMessage chatMessage = (ChatMessage) e;
				// TO DO: Add all userID's for the chat
				for(Map.Entry<Integer, Integer> entry : ServerContext.getContext().getNetwork().getUserIDConnectionIDMap().entrySet())
					if(entry.getKey() != chatMessage.getUserID())
						destinations.add(entry.getKey());
				
				chatMessage.setType("SERVER_CHAT");
				chatMessage.addRecipients(destinations);
				Server.getServer().publishEvent(chatMessage);
				System.out.println("Event received and handled: " + e.getType());
                break;
				
			default:
				System.out.println("Event received but left unhandled: " + e.getType());
				break;
			}
		}
	}

	private final static Server server = new Server();
	private static ServerHandler serverHandler = new ServerHandler();
	
	private Server() {
	}
	
	public static void main(String[] args) {
		Network network = new Network(1025, "SERVER");
		ServerContext.getContext().setNetwork(network);
		EventBroker.getEventBroker().addEventListener(serverHandler);
		EventBroker.getEventBroker().addEventListener(network);
		EventBroker.getEventBroker().start();
		
		int andreID = ServerContext.getContext().addUser("André", "");
		int quizID = ServerContext.getContext().addQuiz(8, 4, 1, 20, andreID);
		ServerContext.getContext().addTeam(quizID, "André en de boys", Color.BLUE, andreID);
	}
	
	public static Server getServer() {
		return server;
	}

}
