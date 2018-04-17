package quiz.util;

public class ClientJoinQuizEvent extends UserEvent{
	int userID;
	int quizID;
	String userName;

	public ClientJoinQuizEvent(int userID,int quizID,String userName){
		super();
		this.userID=userID;
		this.quizID=quizID;
		this.userName=userName;
		this.type="CLIENT_JOIN_QUIZ";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserID() {
		return userID;
	}

	public int getQuizID() {
		return quizID;
	}

	public void setQuizID(int quizID) {
		this.quizID = quizID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

}
