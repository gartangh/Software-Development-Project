package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.scene.paint.Color;
import network.Network;
import quiz.model.Quiz;
import quiz.util.ClientAnswerEvent;
import quiz.util.ClientVoteEvent;

public class Server extends EventPublisher{
	
	public static class ServerHandler implements EventListener{ // TODO: add handling of all events 
		public void handleEvent(Event e){
			switch(e.getType()) {
			case "CLIENT_VOTE":
				
				ClientVoteEvent clientVote = (ClientVoteEvent) e;
				
				Quiz quiz0 = ServerContext.getContext().getQuiz(clientVote.getQuizID());
				quiz0.addVote(clientVote.getUserID(), clientVote.getTeamID(), clientVote.getVote());
				
				ServerVoteEvent serverVote = new ServerVoteEvent(clientVote.getUserID(), clientVote.getTeamID(), clientVote.getQuizID(), clientVote.getVote());
				Server.getServer().publishEvent(serverVote);
				
				System.out.println("Event received and handled: " + e.getType());
				break;
			
			case "CLIENT_ANSWER":
				
				ClientAnswerEvent clientAnswer = (ClientAnswerEvent) e;
				
				Quiz quiz1 = ServerContext.getContext().getQuiz(clientAnswer.getQuizID());
				quiz1.addAnswer( clientAnswer.getTeamID(), clientAnswer.getQuestionID(), clientAnswer.getAnswer());
				
				// TODO: search correct answer for question
				
				ServerAnswerEvent serverAnswer = new ServerAnswerEvent(clientAnswer.getTeamID(), clientAnswer.getQuestionID(), clientAnswer.getAnswer(), 3);
				Server.getServer().publishEvent(serverAnswer);
				
				String question = "Wat is de hoofdstand van het grootste land ter wereld?";
				String [] answers = {"Moskou", "Washington D.C.", "Brussel", "Peking"};
				int[] permutation = {1,2,3,4};
				ServerNewQuestionEvent sNQ = new ServerNewQuestionEvent(1, question, answers, permutation);
				
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Server.getServer().publishEvent(sNQ);
				
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
			ServerContext.getContext().loadData();
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static Server getServer() {
		return server;
	}

}
