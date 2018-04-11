package quiz.view;

import chat.ChatMessage;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.TextFlow;
import quiz.util.ClientVoteEvent;

public class QuestionFormController extends EventPublisher {
	
	@FXML
	private Label questionTitel;
	@FXML
	private TextFlow questionText;
	@FXML
	private CheckBox checkA;
	@FXML
	private CheckBox checkB;
	@FXML
	private CheckBox checkC;
	@FXML
	private CheckBox checkD;	
	@FXML
	private Label answerA;
	@FXML
	private Label answerB;
	@FXML
	private Label answerC;
	@FXML
	private Label answerD;
	@FXML
	private Button voteButton;
	@FXML
	private Button confirmButton;
	@FXML
	private Label percentageA;
	@FXML
	private Label percentageB;
	@FXML
	private Label percentageC;
	@FXML
	private Label percentageD;
	@FXML
	private Label numberOfVotes;
	@FXML
	private ProgressBar voteProgressA;
	@FXML
	private ProgressBar voteProgressB;
	@FXML
	private ProgressBar voteProgressC;
	@FXML
	private ProgressBar voteProgressD;
	
	public class QuestionFormEventHandler implements EventListener{ // TODO: add handling of events 
		public void handleEvent(Event e){
			switch(e.getType()) {
			default:
				break;
			}
		}
	}
	
	private QuestionFormEventHandler eventHandler;
	
	
	public void initialize() {
	}
	
	private void handleCheck(int answer) {
		if(answer != 0) checkA.setSelected(false);
		if(answer != 1) checkB.setSelected(false);
		if(answer != 2) checkC.setSelected(false);
		if(answer != 3) checkD.setSelected(false);
	}
	
	@FXML
	private void handleCheckA() {
		handleCheck(0);
	}
	@FXML
	private void handleCheckB() {
		handleCheck(1);
	}
	
	@FXML
	private void handleCheckC() {
		handleCheck(2);
	}
	
	@FXML
	private void handleCheckD() {
		handleCheck(3);
	}
	
	private int getChecked() {
		if(checkA.isSelected()) return 0;
		else if(checkB.isSelected()) return 1;
		else if(checkC.isSelected()) return 2;
		else if(checkD.isSelected()) return 3;
		else return -1;
	}
	
	@FXML
	private void handleVote() {
		int vote = this.getChecked();
		if(vote >= 0) {
			ClientVoteEvent v = new ClientVoteEvent(vote);
			this.publishEvent(v);
		}
	}
	

	
	
}
