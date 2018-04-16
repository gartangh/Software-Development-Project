package quiz.util;

@SuppressWarnings("serial")
public class ClientNewQuestionEvent extends QuizzerEvent{
		
	public ClientNewQuestionEvent() {
		super();
		this.type = "CLIENT_NEW_QUESTION";
		this.message = "";
	}
}