package quiz.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ClientGetQuizzesEvent extends UserEvent implements Serializable{

	public ClientGetQuizzesEvent() {
		this.type = "CLIENT_GET_QUIZZES";
	}

}
