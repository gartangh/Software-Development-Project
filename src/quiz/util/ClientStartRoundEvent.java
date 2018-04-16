package quiz.util;

@SuppressWarnings("serial")
public class ClientStartRoundEvent extends QuizzerEvent {
	
	public ClientStartRoundEvent(){
		super();
		this.type = "CLIENT_START_ROUND";
	}
}
