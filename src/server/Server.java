package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import chat.ChatMessage;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import network.Connection;
import network.Network;
import quiz.util.ClientCreateEvent;
import quiz.util.ClientVoteEvent;

public class Server extends EventPublisher implements Runnable{

	ServerHandler serverHandler = new ServerHandler();
	
	private class ServerHandler implements EventListener{ // TODO: add handling of all events 
		public void handleEvent(Event e){
			ArrayList<Integer> destinations = new ArrayList<>();
			switch(e.getType()) {
			case "CLIENT_VOTE":
				ClientVoteEvent clientVote = (ClientVoteEvent) e;
				
				//ServerContext.getContext()
				break;

			case "CLIENT_CREATE":
				ClientCreateEvent clientCreate = (ClientCreateEvent) e;
				destinations.add(clientCreate.getConnectionID());
				e.setType("SERVER_CREATE");
				ServerContext.getContext().getNetwork().handleEvent(e, destinations);
				break;
				
			case "CLIENT_CHAT":
				ChatMessage chatMessage = (ChatMessage) e;
				// TO DO: Add all userID's for the chat
				for(Map.Entry<Integer, Integer> entry : ServerContext.getContext().getNetwork().getUserIDConnectionIDMap().entrySet())
					if(entry.getKey() != chatMessage.getUserID())
						destinations.add(entry.getKey());
				ServerContext.getContext().getNetwork().handleEvent(e, destinations);
			}
		}

		@Override
		public void handleEvent(Event e, ArrayList<Integer> destinations) {}
	}

	@Override
	public void run() {
		try {
			EventBroker.getEventBroker().start();
			System.out.println(InetAddress.getLocalHost());
			Network network = new Network(1025, "SERVER");
			ServerContext.getContext().setNetwork(network);
			EventBroker.getEventBroker().addEventListener(serverHandler);
			EventBroker.getEventBroker().addEventListener(network);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
