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
import javafx.scene.text.Text;
import main.Context;
import quiz.model.Quiz;
import quiz.model.AnswerVoteModel;
import quiz.model.MCQuestion;
import quiz.util.ClientAnswerEvent;
import quiz.util.ClientNewQuestionEvent;
import quiz.util.ClientVoteEvent;
import server.ServerAnswerEvent;
import server.ServerContext;
import server.ServerNewQuestionEvent;
import server.ServerVoteEvent;

public class QuestionFormController extends EventPublisher {

	@FXML
	private Label questionTitle;
	@FXML
	private Text questionText;
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
	private Button nextButton;
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
	
	private AnswerVoteModel answerVoteModel;
	private QuestionFormEventHandler eventHandler;

	
	public class QuestionFormEventHandler implements EventListener{ // TODO: add handling of events 
		public void handleEvent(Event e){
			switch(e.getType()) {
			case "SERVER_VOTE":
				
				ServerVoteEvent serverVote = (ServerVoteEvent) e;
				
				Context.getContext().getQuiz().addVote(serverVote.getUserID(), serverVote.getTeamID(), serverVote.getVote());
				answerVoteModel.updateVotes(serverVote.getTeamID());
				
				System.out.println("Event received and handled: " + e.getType());
				break;
				
			case "SERVER_ANSWER":
				
				ServerAnswerEvent serverAnswer = (ServerAnswerEvent) e;
				
				Context.getContext().getQuiz().addAnswer(serverAnswer.getTeamID(), serverAnswer.getQuestionID(), serverAnswer.getAnswer());
				answerVoteModel.updateAnswer(serverAnswer.getAnswer(), serverAnswer.getCorrectAnswer());
				
				System.out.println("Event received and handled: " + e.getType());
				break;
				
			case "SERVER_NEW_QUESTION":
				
				ServerNewQuestionEvent sNQE = (ServerNewQuestionEvent) e;
				MCQuestion q = new MCQuestion(sNQE.getQuestionID(), sNQE.getQuestion(), sNQE.getAnswers());
				Context.getContext().setQuestion(q);
				answerVoteModel.updateQuestion();
				answerVoteModel.updateVotes(Context.getContext().getTeamID());
				
				System.out.println("Event received and handled: " + e.getType());
				break;
				
			default:
				System.out.println("Event received but left unhandled: " + e.getType());
				break;
			}
		}
	}
	
	public QuestionFormController() {
		this.answerVoteModel = new AnswerVoteModel();
	}

	public void initialize() {
		eventHandler = new QuestionFormEventHandler();
		EventBroker.getEventBroker().addEventListener(eventHandler);
		
		questionTitle.textProperty().bind(answerVoteModel.getQuestionTitleProperty());
		questionText.textProperty().bind(answerVoteModel.getQuestionTextProperty());
		
		answerA.textProperty().bind(answerVoteModel.getAnswerPropertyA());
		answerB.textProperty().bind(answerVoteModel.getAnswerPropertyB());
		answerC.textProperty().bind(answerVoteModel.getAnswerPropertyC());
		answerD.textProperty().bind(answerVoteModel.getAnswerPropertyD());
		answerA.textFillProperty().bind(answerVoteModel.getPaintPropertyA());
		answerB.textFillProperty().bind(answerVoteModel.getPaintPropertyB());
		answerC.textFillProperty().bind(answerVoteModel.getPaintPropertyC());
		answerD.textFillProperty().bind(answerVoteModel.getPaintPropertyD());
		
		voteProgressA.progressProperty().bind(answerVoteModel.getProgressPropertyA());
		voteProgressB.progressProperty().bind(answerVoteModel.getProgressPropertyB());
		voteProgressC.progressProperty().bind(answerVoteModel.getProgressPropertyC());
		voteProgressD.progressProperty().bind(answerVoteModel.getProgressPropertyD());

		percentageA.textProperty().bind(answerVoteModel.getPercentagePropertyA());
		percentageB.textProperty().bind(answerVoteModel.getPercentagePropertyB());
		percentageC.textProperty().bind(answerVoteModel.getPercentagePropertyC());
		percentageD.textProperty().bind(answerVoteModel.getPercentagePropertyD());
		numberOfVotes.textProperty().bind(answerVoteModel.getNumberOfVotesProperty());
		
		voteButton.disableProperty().bind(answerVoteModel.getVoteDisableProperty());
		confirmButton.disableProperty().bind(answerVoteModel.getConfirmDisableProperty());
		nextButton.disableProperty().bind(answerVoteModel.getNextDisableProperty());
		
		answerVoteModel.updateVotes(Context.getContext().getTeamID());
	}

	private void handleCheck(int answer) {
		if (answer != 0)
			checkA.setSelected(false);
		if (answer != 1)
			checkB.setSelected(false);
		if (answer != 2)
			checkC.setSelected(false);
		if (answer != 3)
			checkD.setSelected(false);
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
		if (checkA.isSelected())
			return 0;
		else if (checkB.isSelected())
			return 1;
		else if (checkC.isSelected())
			return 2;
		else if (checkD.isSelected())
			return 3;
		else
			return -1;
	}

	@FXML
	private void handleVote() {
		int vote = this.getChecked();
		if (vote >= 0) {
			ClientVoteEvent cve = new ClientVoteEvent(vote);
			this.publishEvent(cve);
		}
	}

	@FXML
	private void handleAnswer() {
		int answer = this.getChecked();
		if(answer >= 0) {
			ClientAnswerEvent cae = new ClientAnswerEvent(Context.getContext().getQuestion().getQuestionID(), answer);
			this.publishEvent(cae);
			handleCheck(-1);
		}
	}	
	
	@FXML
	private void handleNext() {
		ClientNewQuestionEvent cnqe = new ClientNewQuestionEvent();
		this.publishEvent(cnqe);
	}
}
