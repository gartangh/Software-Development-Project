package quiz.view;

import chat.ChatPanel;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientAnswerEvent;
import eventbroker.clientevent.ClientNewQuestionEvent;
import eventbroker.clientevent.ClientVoteEvent;
import eventbroker.serverevent.ServerVoteAnswerEvent;
import eventbroker.serverevent.ServerEndQuizEvent;
import eventbroker.serverevent.ServerNewMCQuestionEvent;
import eventbroker.serverevent.ServerNewRoundEvent;
import eventbroker.serverevent.ServerNotAllAnsweredEvent;
import eventbroker.serverevent.ServerVoteEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import main.MainContext;
import main.Main;
import quiz.model.AnswerVoteModel;
import quiz.model.MCQuestion;

public class QuestionController extends EventPublisher {

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
	@FXML
	private AnchorPane mPlaceholder;

	private AnswerVoteModel answerVoteModel = new AnswerVoteModel();
	private VoteHandler voteHandler = new VoteHandler();
	private VoteAnswerHandler voteAnwserHandler = new VoteAnswerHandler();
	private NewMCQuestionHandler newMCQuestionHandler = new NewMCQuestionHandler();
	private NotAllAnsweredHandler notAllAnsweredHandler = new NotAllAnsweredHandler();
	private NewRoundHandler newRoundHandler = new NewRoundHandler();
	private EndQuizHandler endQuizHandler = new EndQuizHandler();

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}

	// Methods
	@FXML
	public void initialize() {
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerVoteEvent.EVENTTYPE, voteHandler);
		eventBroker.addEventListener(ServerVoteAnswerEvent.EVENTTYPE, voteAnwserHandler);
		eventBroker.addEventListener(ServerNewMCQuestionEvent.EVENTTYPE, newMCQuestionHandler);
		eventBroker.addEventListener(ServerNotAllAnsweredEvent.EVENTTYPE, notAllAnsweredHandler);
		eventBroker.addEventListener(ServerNewRoundEvent.EVENTTYPE, newRoundHandler);
		eventBroker.addEventListener(ServerEndQuizEvent.EVENTTYPE, endQuizHandler);

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

		answerVoteModel.updateQuestion();
		answerVoteModel.updateVotes(MainContext.getContext().getTeam().getTeamID());

		// ChatPanel (ChatModel and ChatController) are created
		ChatPanel chatPanel = ChatPanel.createChatPanel();
		mPlaceholder.getChildren().add(chatPanel.getContent());
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
		int vote = getChecked();
		if (vote >= 0) {
			ClientVoteEvent cVE = new ClientVoteEvent(vote);
			publishEvent(cVE);
		}
	}

	@FXML
	private void handleAnswer() {
		MainContext context = MainContext.getContext();
		int answer = getChecked();
		if (context.getTeam().getCaptainID() != context.getUser().getUserID()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.initOwner(main.getPrimaryStage());
					alert.setTitle("Warning");
					alert.setHeaderText("You can't submit an answer!");
					alert.setContentText("You must be the captain of the team in order to submit the answer.");
					alert.showAndWait();
				}
			});
		} else if (answer < 0) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("You can't submit an empty answer!");
					alert.setContentText("Please select a valid answer and try again.");
					alert.showAndWait();
				}
			});
		} else {
			ClientAnswerEvent cAE = new ClientAnswerEvent(MainContext.getContext().getQuestion().getQuestionID(),
					answer);
			publishEvent(cAE);
			handleCheck(-1);
		}
	}

	@FXML
	private void handleNext() {
		MainContext context = MainContext.getContext();
		if (context.getTeam().getCaptainID() != context.getUser().getUserID()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("You can't go to the next question!");
					alert.setContentText(
							"You must be the captain of the team in order to proceed to the next question.");
					alert.showAndWait();
				}
			});
		} else {
			ClientNewQuestionEvent cNQE = new ClientNewQuestionEvent();
			publishEvent(cNQE);
		}
	}

	// Inner classes
	private class VoteHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerVoteEvent sVE = (ServerVoteEvent) event;

			int userID = sVE.getUserID();
			int teamID = sVE.getTeamID();
			int vote = sVE.getVote();

			MainContext.getContext().getQuiz().addVote(userID, teamID, vote);
			answerVoteModel.updateVotes(teamID);
		}

	}

	private class VoteAnswerHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerVoteAnswerEvent sVAE = (ServerVoteAnswerEvent) event;

			int answer = sVAE.getAnswer();
			int correctAnswer = sVAE.getCorrectAnswer();

			answerVoteModel.updateAnswer(answer, correctAnswer);
		}

	}

	private class NewMCQuestionHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerNewMCQuestionEvent sNMCQE = (ServerNewMCQuestionEvent) event;

			int questionID = sNMCQE.getQuestionID();
			String question = sNMCQE.getQuestion();
			String[] answers = sNMCQE.getAnswers();

			MCQuestion q = new MCQuestion(questionID, question, answers);
			MainContext.getContext().setQuestion(q);
			answerVoteModel.updateQuestion();
			answerVoteModel.updateVotes(MainContext.getContext().getTeam().getTeamID());
		}

	}

	private class NotAllAnsweredHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("You can't go to the next question!");
					alert.setContentText("Not all teams have answered this question.");
					alert.showAndWait();
				}
			});
		}

	}

	private class NewRoundHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerNewRoundEvent sNRE = (ServerNewRoundEvent) event;

			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(voteHandler);
			eventBroker.removeEventListener(voteAnwserHandler);
			eventBroker.removeEventListener(newMCQuestionHandler);
			eventBroker.removeEventListener(notAllAnsweredHandler);
			eventBroker.removeEventListener(newRoundHandler);
			eventBroker.removeEventListener(endQuizHandler);

			main.showWaitRoundScene();
		}

	}

	private class EndQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerEndQuizEvent sEQE = (ServerEndQuizEvent) event;

			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(voteHandler);
			eventBroker.removeEventListener(voteAnwserHandler);
			eventBroker.removeEventListener(newMCQuestionHandler);
			eventBroker.removeEventListener(notAllAnsweredHandler);
			eventBroker.removeEventListener(newRoundHandler);
			eventBroker.removeEventListener(endQuizHandler);

			main.showScoreboardScene();
		}

	}

}
