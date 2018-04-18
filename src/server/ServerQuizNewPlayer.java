package server;

public class ServerQuizNewPlayer extends ServerEvent{
	int userID;
	String userName;

	public ServerQuizNewPlayer(int userID, String userName) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.type="SERVER_QUIZ_NEW_PLAYER";
	}
	
	public int getUserID() {
		return userID;
	}

	public String getUserName() {
		return userName;
	}
	
}
