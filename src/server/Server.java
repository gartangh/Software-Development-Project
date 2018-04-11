package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
			switch(e.getType()) {
			case "CLIENT_VOTE":
				ClientVoteEvent clientVote = (ClientVoteEvent) e;
				
				//ServerContext.getContext()
				break;
				
			case "CLIENT_CREATE":
				ClientCreateEvent clientCreate = (ClientCreateEvent) e;
				int newID = ServerContext.getContext().addUser(clientCreate.getUsername(), clientCreate.getPassword());
				ServerContext.getContext().addConnection(newID, clientCreate.getConnection());
				// Send userID back to user with GIVE_ID event or unneccessary?
				break;
				
			case "CLIENT_CHAT":
				ChatMessage chatMessage = (ChatMessage) e;
				System.out.println(chatMessage.getMessage());
				chatMessage.setType("SERVER_CHAT");
				for(Map.Entry<Integer, Connection> entry : ServerContext.getContext().getConnectionMap().entrySet()) {
					// TO DO: Check if this is correct!
					// Only send over network if entry.getValue() != connection
					// If not an option: if entry.getKey() != ChatMessage.getUserID() but you need to send back ID in CLIENT_CREATE for that
					// else:
					if(!(ServerContext.getContext().getUserMap().get(entry.getKey()).getUsername().equals(chatMessage.getSender())))
						ServerContext.getContext().getNetwork().handleEvent(e);
					}
			}
		}
	}

	@Override
	public void run() {
		try {
			EventBroker.getEventBroker().start();
			System.out.println(InetAddress.getLocalHost());
			Network network = new Network(1025);
			ServerContext.getContext().setNetwork(network);
			EventBroker.getEventBroker().addEventListener(serverHandler);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
