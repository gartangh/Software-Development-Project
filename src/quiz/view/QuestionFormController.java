package quiz.view;

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
import main.Context;
import quiz.model.Quiz;
import quiz.model.VoteModel;
import quiz.util.ClientVoteEvent;
import server.Server;
import server.ServerContext;
import server.ServerVoteEvent;

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
	private VoteModel voteModel;
	
	public class QuestionFormEventHandler implements EventListener{ // TODO: add handling of events 
		public void handleEvent(Event e){
			switch(e.getType()) {
			case "SERVER_VOTE":
				ServerVoteEvent serverVote = (ServerVoteEvent) e;
				
				Quiz quiz = Context.getContext().getQuiz();
				quiz.addVote(serverVote.getUserID(), serverVote.getTeamID(), serverVote.getVote());
				Context.getContext().setQuiz(quiz);
				voteModel.updateModel(quiz, serverVote.getTeamID());
				
				System.out.println("Event received and handled: " + e.getType());
				break;
			default:
				System.out.println("Event received but left unhandled: " + e.getType());
				break;
			}
		}
	}
	
	public QuestionFormController() {
		this.voteModel = new VoteModel();
	}
	
	private QuestionFormEventHandler eventHandler;
	
	public void initialize() {
		eventHandler = new QuestionFormEventHandler();
		EventBroker.getEventBroker().addEventListener(eventHandler);
		
		voteProgressA.progressProperty().bind(voteModel.getProgressPropertyA());
		voteProgressB.progressProperty().bind(voteModel.getProgressPropertyB());
		voteProgressC.progressProperty().bind(voteModel.getProgressPropertyC());
		voteProgressD.progressProperty().bind(voteModel.getProgressPropertyD());
		
		percentageA.textProperty().bind(voteModel.getPercentagePropertyA());
		percentageB.textProperty().bind(voteModel.getPercentagePropertyB());
		percentageC.textProperty().bind(voteModel.getPercentagePropertyC());
		percentageD.textProperty().bind(voteModel.getPercentagePropertyD());
		numberOfVotes.textProperty().bind(voteModel.getNumberOfVotesProperty());
		
		voteModel.updateModel(Context.getContext().getQuiz(), Context.getContext().getTeamID());
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
			ClientVoteEvent cve = new ClientVoteEvent(vote);
			this.publishEvent(cve);
		}
	}
	

	
	
}
