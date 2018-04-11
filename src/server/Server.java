package server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import network.Network;
import quiz.util.ClientVoteEvent;
import quiz.util.NewTeamEvent;

public class Server extends EventPublisher{

	public class ServerHandler implements EventListener{ // TODO: add handling of all events
		public void handleEvent(Event e){
			switch(e.getType()) {
			case "CLIENT_VOTE":
				ClientVoteEvent clientVote = (ClientVoteEvent) e;
				//ServerContext.getContext()
			case "CLIENT_NEW_TEAM":
				NewTeamEvent newteamevent=(NewTeamEvent) e;
				ServerContext.getContext().addTeam(newteamevent.getQuizID(),newteamevent.getTeamName(),newteamevent.getColor(),newteamevent.getUserID());
			}

		}
	}

	private ServerHandler serverHandler=new ServerHandler();

	public static void main(String[] args) {
		try {
			System.out.println(InetAddress.getLocalHost());
			Network network = new Network(1025);
			EventBroker.getEventBroker().addEventListener(network);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}