package server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.scene.paint.Color;
import network.Network;
import quiz.model.Quiz;
import quiz.util.ClientVoteEvent;

public class Server extends EventPublisher{
	
	public static class ServerHandler implements EventListener{ // TODO: add handling of all events 
		public void handleEvent(Event e){
			switch(e.getType()) {
			case "CLIENT_VOTE":
				ClientVoteEvent clientVote = (ClientVoteEvent) e;
				
				Quiz quiz = ServerContext.getContext().getQuiz(clientVote.getQuizID());
				quiz.addVote(clientVote.getUserID(), clientVote.getTeamID(), clientVote.getVote());
				ServerContext.getContext().updateQuiz(quiz);
				
				ServerVoteEvent serverVote = new ServerVoteEvent(clientVote.getUserID(), clientVote.getTeamID(), clientVote.getQuizID(), clientVote.getVote());
				Server.getServer().publishEvent(serverVote);
				
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
		try {
			System.out.println(InetAddress.getLocalHost());
			Network network = new Network(1025);
			EventBroker.getEventBroker().addEventListener(network);
			EventBroker.getEventBroker().addEventListener(serverHandler);
			EventBroker.getEventBroker().start();
			
			int andreID = ServerContext.getContext().addUser("André", "");
			int quizID = ServerContext.getContext().addQuiz(8, 4, 1, 20, andreID);
			ServerContext.getContext().addTeam(quizID, "André en de boys", Color.BLUE, andreID);
			
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static Server getServer() {
		return server;
	}

}
