package server;

import java.util.concurrent.TimeUnit;

import eventbroker.EventBroker;

public class Main {
	public static void main(String[] args) {
		Server server = new Server();
		new Thread(server).start();
	}
}
