package quiz.view;

import java.awt.image.BufferedImage;

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
import eventbroker.serverevent.ServerNewIPQuestionEvent;
import eventbroker.serverevent.ServerNewMCQuestionEvent;
import eventbroker.serverevent.ServerNewPixelSizeEvent;
import eventbroker.serverevent.ServerNewRoundEvent;
import eventbroker.serverevent.ServerNotAllAnsweredEvent;
import eventbroker.serverevent.ServerVoteEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Context;
import main.Main;
import quiz.model.AnswerVoteModel;
import quiz.model.IPQuestion;
import quiz.model.MCQuestion;
import quiz.model.Question;
import quiz.util.RoundType;

public class QuestionController extends EventPublisher {

	@FXML
	private VBox leftVBox;
	@FXML
	private Label questionTitle;
	@FXML
	private Text questionText;
	@FXML
	private ImageView imageView;
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
	private VoteHandler voteHandler;
	private VoteAnwserHandler voteAnwserHandler;
	private NewMCQuestionHandler newMCQuestionHandler;
	private NewIPQuestionHandler newIPQuestionHandler;
	private NewPixelSizeHandler newPixelSizeHandler;
	private NotAllAnsweredHandler notAllAnsweredHandler;
	private NewRoundHandler newRoundHandler;
	private EndQuizHandler endQuizHandler;

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}

	// Methods
	@FXML
	public void initialize() {
		voteHandler = new VoteHandler();
		voteAnwserHandler = new VoteAnwserHandler();
		newMCQuestionHandler = new NewMCQuestionHandler();
		newIPQuestionHandler = new NewIPQuestionHandler();
		newPixelSizeHandler = new NewPixelSizeHandler();
		notAllAnsweredHandler = new NotAllAnsweredHandler();
		newRoundHandler = new NewRoundHandler();
		endQuizHandler = new EndQuizHandler();

		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerVoteEvent.EVENTTYPE, voteHandler);
		eventBroker.addEventListener(ServerVoteAnswerEvent.EVENTTYPE, voteAnwserHandler);
		eventBroker.addEventListener(ServerNewMCQuestionEvent.EVENTTYPE, newMCQuestionHandler);
		eventBroker.addEventListener(ServerNewIPQuestionEvent.EVENTTYPE, newIPQuestionHandler);
		eventBroker.addEventListener(ServerNewPixelSizeEvent.EVENTTYPE, newPixelSizeHandler);
		eventBroker.addEventListener(ServerNotAllAnsweredEvent.EVENTTYPE, notAllAnsweredHandler);
		eventBroker.addEventListener(ServerNewRoundEvent.EVENTTYPE, newRoundHandler);
		eventBroker.addEventListener(ServerEndQuizEvent.EVENTTYPE, endQuizHandler);

		questionTitle.textProperty().bind(answerVoteModel.getQuestionTitleProperty());
		questionText.textProperty().bind(answerVoteModel.getQuestionTextProperty());
		imageView.imageProperty().bind(answerVoteModel.getImageProperty());

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

		//answerVoteModel.updateVotes(Context.getContext().getTeamID());
		
		// ChatPanel (ChatModel and ChatController) are created
		ChatPanel chatPanel = ChatPanel.createChatPanel();
		mPlaceholder.getChildren().add(chatPanel.getContent());
	}
	
	public void setRoundType(RoundType roundType) {
		answerVoteModel.setRoundType(roundType);
		switch(roundType) {
		case IP:
			leftVBox.getChildren().remove(1);
			imageView.imageProperty().bind(answerVoteModel.getImageProperty());
			break;
		case MC:
			leftVBox.getChildren().remove(2);
			questionText.textProperty().bind(answerVoteModel.getQuestionTextProperty());
			break;
		}
		//answerVoteModel.updateQuestion();
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
			ClientVoteEvent cVE = new ClientVoteEvent(vote);
			publishEvent(cVE);
		}
	}

	@FXML
	private void handleAnswer() {
		Context context = Context.getContext();
		int answer = this.getChecked();
		if (context.getQuiz().getTeamMap().get(context.getTeamID()).getCaptainID() != Context.getContext().getUser()
				.getUserID()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("QuizForm Error");
			alert.setHeaderText("You can't submit an answer");
			alert.setContentText("You are not the captain, so you can't submit an answer.");

			alert.showAndWait();
		} else if (answer < 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("QuizForm Error");
			alert.setHeaderText("You can't submit an empty answer");
			alert.setContentText("Please select an answer to submit.");

			alert.showAndWait();
		} else {
			RoundType roundType = context.getRoundType();
			ClientAnswerEvent cAE = null;
			switch(roundType) {
			case IP:
				IPQuestion ipQ = (IPQuestion) Context.getContext().getQuestion();
				cAE = new ClientAnswerEvent(ipQ.getQuestionID(), ipQ.getPixelSize(), answer);
				break;
			case MC:
				cAE = new ClientAnswerEvent(Context.getContext().getQuestion().getQuestionID(), answer);
				break;
			}
			 
			publishEvent(cAE);
			handleCheck(-1);
		}
	}

	@FXML
	private void handleNext() {
		if (Context.getContext().getQuiz().getTeamMap().get(Context.getContext().getTeamID()).getCaptainID() != Context
				.getContext().getUser().getUserID()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("QuizForm Error");
			alert.setHeaderText("You can't go to the next question");
			alert.setContentText("You are not a team captain, so you can't proceed to the next question.");

			alert.showAndWait();
		} else {
			ClientNewQuestionEvent cnqe = new ClientNewQuestionEvent();
			this.publishEvent(cnqe);
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

			Context.getContext().getQuiz().addVote(userID, teamID, vote);
			answerVoteModel.updateVotes(teamID);
		}

	}

	private class VoteAnwserHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerVoteAnswerEvent sVAE = (ServerVoteAnswerEvent) event;

			int answer = sVAE.getAnswer();
			int correctAnswer = sVAE.getCorrectAnswer();
			Context.getContext().setAnswered(true);
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
			Context.getContext().setQuestion(q);
			Context.getContext().setRoundType(RoundType.MC);
			Context.getContext().setAnswered(false);
			answerVoteModel.updateQuestion();
			answerVoteModel.updateVotes(Context.getContext().getTeamID());
		}
	}
	
	private class NewIPQuestionHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerNewIPQuestionEvent sNIPQE = (ServerNewIPQuestionEvent) event;

			int questionID = sNIPQE.getQuestionID();
			BufferedImage bufImage = sNIPQE.getImage();
			int pixelSize = sNIPQE.getPixelSize();
			String[] answers = sNIPQE.getAnswers();

			IPQuestion q = new IPQuestion(questionID, bufImage, true, answers);
			q.setPixelSize(pixelSize);
			Context.getContext().setQuestion(q);
			Context.getContext().setRoundType(RoundType.IP);
			Context.getContext().setAnswered(false);
			answerVoteModel.updateQuestion();
			answerVoteModel.updateVotes(Context.getContext().getTeamID());
		}
	}
	
	private class NewPixelSizeHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			System.out.println("Receiver ServerNewPixelSizeEvent");
			ServerNewPixelSizeEvent sNPSE = (ServerNewPixelSizeEvent) event;

			int questionID = sNPSE.getQuestionID();
			int pixelSize = sNPSE.getPixelSize();

			if(Context.getContext().isAnswered() == false) {
				IPQuestion q = (IPQuestion) Context.getContext().getQuestion();
				if(q.getQuestionID() == questionID) {
					q.setPixelSize(pixelSize);
				}
				
				answerVoteModel.updateImage();
			}
		}
	}

	private class NotAllAnsweredHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			// TODO: Alert
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
			eventBroker.removeEventListener(newIPQuestionHandler);
			eventBroker.removeEventListener(newPixelSizeHandler);
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
			eventBroker.removeEventListener(newIPQuestionHandler);
			eventBroker.removeEventListener(newPixelSizeHandler);
			eventBroker.removeEventListener(notAllAnsweredHandler);
			eventBroker.removeEventListener(newRoundHandler);
			eventBroker.removeEventListener(endQuizHandler);

			main.showScoreboardScene();
		}

	}

}
