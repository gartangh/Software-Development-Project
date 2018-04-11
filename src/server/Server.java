package server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import network.Network;

public class Server {

	public static void main(String[] args) {
		try {
			System.out.println(InetAddress.getLocalHost());
			
			Network network = new Network(1025);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
