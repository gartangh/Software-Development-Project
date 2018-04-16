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
import quiz.model.AnswerVoteModel;
import quiz.util.ClientAnswerEvent;
import quiz.util.ClientVoteEvent;
import server.ServerAnswerEvent;
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

	private AnswerVoteModel answerVoteModel;
	private QuestionFormEventHandler eventHandler;

	// Constructor
	public QuestionFormController() {
		this.answerVoteModel = new AnswerVoteModel();
	}

	public void initialize() {
		eventHandler = new QuestionFormEventHandler();
		EventBroker.getEventBroker().addEventListener(eventHandler);

		voteProgressA.progressProperty().bind(answerVoteModel.getProgressPropertyA());
		voteProgressB.progressProperty().bind(answerVoteModel.getProgressPropertyB());
		voteProgressC.progressProperty().bind(answerVoteModel.getProgressPropertyC());
		voteProgressD.progressProperty().bind(answerVoteModel.getProgressPropertyD());

		percentageA.textProperty().bind(answerVoteModel.getPercentagePropertyA());
		percentageB.textProperty().bind(answerVoteModel.getPercentagePropertyB());
		percentageC.textProperty().bind(answerVoteModel.getPercentagePropertyC());
		percentageD.textProperty().bind(answerVoteModel.getPercentagePropertyD());
		numberOfVotes.textProperty().bind(answerVoteModel.getNumberOfVotesProperty());

		answerA.textFillProperty().bind(answerVoteModel.getPaintPropertyA());
		answerB.textFillProperty().bind(answerVoteModel.getPaintPropertyB());
		answerC.textFillProperty().bind(answerVoteModel.getPaintPropertyC());
		answerD.textFillProperty().bind(answerVoteModel.getPaintPropertyD());

		voteButton.visibleProperty().bind(answerVoteModel.getVoteVisibilityProperty());
		confirmButton.visibleProperty().bind(answerVoteModel.getConfirmVisibilityProperty());

		answerVoteModel.updateVotes(Context.getContext().getTeam().getTeamID());
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
		if (answer >= 0) {
			// ClientAnswerEvent cae = new
			// ClientAnswerEvent(Context.getContext().getQuestion().getQuestionID(),
			// Context.getContext().getTeamID(), answer);
			ClientAnswerEvent cae = new ClientAnswerEvent(1, 1, answer); // Testing
																			// purposes
			this.publishEvent(cae);
		}
	}

	// Inner class
	public class QuestionFormEventHandler implements EventListener {

		// TODO: add handling of events

		@Override
		public void handleEvent(Event e) {
			switch (e.getType()) {
			case "SERVER_VOTE":
				ServerVoteEvent serverVote = (ServerVoteEvent) e;

				Quiz quiz0 = Context.getContext().getQuiz();
				quiz0.addVote(serverVote.getUserID(), serverVote.getTeamID(), serverVote.getVote());
				answerVoteModel.updateVotes(serverVote.getTeamID());

				System.out.println("Event received and handled: " + e.getType());
				break;
			case "SERVER_ANSWER":
				ServerAnswerEvent serverAnswer = (ServerAnswerEvent) e;

				Quiz quiz1 = Context.getContext().getQuiz();
				quiz1.addAnswer(serverAnswer.getTeamID(), serverAnswer.getQuestionID(), serverAnswer.getAnswer());
				answerVoteModel.updateAnswer(serverAnswer.getAnswer(), serverAnswer.getCorrectAnswer());

				System.out.println("Event received and handled: " + e.getType());
				break;
			default:
				System.out.println("Event received but left unhandled: " + e.getType());
				break;
			}
		}
	}
}
