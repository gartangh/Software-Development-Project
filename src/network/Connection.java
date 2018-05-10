package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map.Entry;

import eventbroker.Event;
import eventbroker.clientevent.ClientCreateAccountEvent;
import eventbroker.clientevent.ClientLogInEvent;
import server.Server;

public class Connection {

	private int connectionID;
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private Network network;

	// Package local would be safer
	public Connection(Socket socket, Network network) {
		this.socket = socket;

		try {
			this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
			this.objectOutputStream.flush();
			this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
		} catch (IOException e) {
			System.err.println("IOException");
		}

		this.network = network;
	}

	// Package local would be safer
	public void send(Event event) {
		try {
			synchronized (this) {
				objectOutputStream.writeObject(event);
				objectOutputStream.flush();
			}
		} catch (IOException e1) {
			System.err.println("IOException");
		}
	}

	// Package local would be safer
	public void receive() {
		new Thread(new ReceiverThread()).start();
	}

	protected void close() {
		synchronized (this) {
			try {
				objectInputStream.close();
				objectOutputStream.close();
				socket.close();
			} catch (SocketException e) {
				System.err.println("SocketException");
			} catch (IOException e) {
				System.err.println("IOException");
			}
		}

		System.out.println("Connection closed");
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

	// Internal class
	private class ReceiverThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				synchronized (this) {
					try {
						Event event = (Event) objectInputStream.readObject();

						String type = event.getType();

						if (type.equals(ClientCreateAccountEvent.EVENTTYPE)) {
							ClientCreateAccountEvent cCAE = (ClientCreateAccountEvent) event;

							cCAE.setConnectionID(connectionID);

							network.publishEvent(cCAE);
						} else if (type.equals(ClientLogInEvent.EVENTTYPE)) {
							ClientLogInEvent cLIE = (ClientLogInEvent) event;

							cLIE.setConnectionID(connectionID);

							network.publishEvent(cLIE);
						} else
							network.publishEvent(event);
					} catch (SocketException e) {
						System.err.println("SocketException");

						if (network.getType() == Network.SERVERTYPE) {
							for (Entry<Integer, Integer> entry : network.getUserIDConnectionIDMap().entrySet())
								if (entry.getValue() == connectionID)
									Server.onConnectionLost(entry.getKey());
						} else if (network.getType() == Network.CLIENTTYPE)
							network.getMainApp().onConnectionLost();
						
						break;
					} catch (ClassNotFoundException e) {
						System.err.println("ClassNotFoundException");
					} catch (IOException e) {
						System.err.println("IOException");
						break;
					}
				}
			}

			System.out.println("ReceiverThread terminated");
		}

	}
}
