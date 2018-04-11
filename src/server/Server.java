package server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.scene.paint.Color;
import network.Network;
import quiz.util.ClientVoteEvent;
import quiz.util.NewTeamEvent;
import quiz.model.Team;

public class Server extends EventPublisher{

	public static class ServerHandler implements EventListener{ // TODO: add handling of all events


		public void handleEvent(Event e){
			System.out.println("in_handle_event");
			switch(e.getType()) {
			case "CLIENT_VOTE":
				ClientVoteEvent clientVote = (ClientVoteEvent) e;
				//ServerContext.getContext()
			case "CLIENT_NEW_TEAM":
				NewTeamEvent newteamevent=(NewTeamEvent) e;
				Team newteam=ServerContext.getContext().addTeam(newteamevent.getQuizID(),newteamevent.getTeamName(),newteamevent.getColor(),newteamevent.getUserID());
				if (newteam != null){
					ServerNewTeamEvent serverNewTeamEvent=new ServerNewTeamEvent(newteamevent.getQuizID(),newteam.getID(),newteam.getName(),newteam.getColor(),newteam.getCaptainID(),newteam.getTeamMembers().get(newteam.getCaptainID()));
					Server.getServer().publishEvent(serverNewTeamEvent);
				}
			}

			}
		}

	private static Server server=new Server();
	private static ServerHandler serverHandler=new ServerHandler();

	private  Server(){

	}

	public static Server getServer(){
		return server;
	}

	public static void main(String[] args) {
		try {
			EventBroker.getEventBroker().start();
			System.out.println(InetAddress.getLocalHost());
			Network network = new Network(1025);
			EventBroker.getEventBroker().addEventListener(network);
			EventBroker.getEventBroker().addEventListener(serverHandler);
			ServerContext.getContext().addQuizwithQuizID(1);
			ServerContext.getContext().addUserwithUserID(1);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}