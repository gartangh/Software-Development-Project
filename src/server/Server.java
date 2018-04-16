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
import quiz.model.MCQuestion;
import quiz.model.Quiz;
import quiz.util.ClientAnswerEvent;
import quiz.util.ClientNewQuestionEvent;
import quiz.util.ClientVoteEvent;
import quiz.util.Difficulty;
import quiz.util.Theme;

public class Server extends EventPublisher{
	
	public static class ServerHandler implements EventListener{ // TODO: add handling of all events 
		public void handleEvent(Event e){
			boolean handled = false;
			switch(e.getType()) {
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
			}
			
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
		try {
			System.out.println(InetAddress.getLocalHost());
			Network network = new Network(1025);
			EventBroker.getEventBroker().addEventListener(network);
			EventBroker.getEventBroker().addEventListener(serverHandler);
			EventBroker.getEventBroker().start();
			
			int andreID = ServerContext.getContext().addUser("André", "");
			int quizID = ServerContext.getContext().addQuiz(8, 4, 1, 4, andreID);
			ServerContext.getContext().addTeam(quizID, "André en de boys", Color.BLUE, andreID);
			ServerContext.getContext().loadData();
			ServerContext.getContext().getQuiz(quizID).addRound(Difficulty.EASY, Theme.CULTURE);
			
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
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static Server getServer() {
		return server;
	}

}
