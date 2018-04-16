package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import chat.ChatMessage;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import javafx.application.Platform;

public class Server implements Runnable {
	
	// Server Attributes
	private final int serverPort = 1234;
	EventBroker eventBroker = EventBroker.getEventBroker();
	handler handlemachine = new handler();
	private static Network network;
	// Constructor
	public Server() {
		eventBroker.start();
	}

	public void run() {
		network = new Network(serverPort);
		EventBroker.getEventBroker().addEventListener(ChatMessage.TYPE_CHAT, handlemachine);
	}
	
	public static Network getNetwork() {
		return network;
	}

	private class handler implements EventListener {

		@Override
		public void handleEvent(Event e) {
			if (e instanceof ChatMessage) {
				System.out.println("juij");
			}
		}
	}
}
