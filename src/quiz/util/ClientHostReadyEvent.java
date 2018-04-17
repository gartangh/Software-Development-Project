package quiz.util;

public class ClientHostReadyEvent extends UserEvent {
	int quizID;
	int userID;

	public ClientHostReadyEvent(int quizID,int userID){
		super();
		this.quizID=quizID;
		this.userID=userID;
		this.type = "CLIENT_HOST_READY";
	}

	public int getQuizID() {
		return quizID;
	}

	public void setQuizID(int quizID) {
		this.quizID = quizID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
}
