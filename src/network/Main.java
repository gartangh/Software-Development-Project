package network;

public class Main {
	public static void main(String[] args) {
		Server server = new Server();
		new Thread(server).start();
	}
}
