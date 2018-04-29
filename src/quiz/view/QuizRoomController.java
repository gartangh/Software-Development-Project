package quiz.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import main.Context;
import main.Main;
import quiz.model.QuizRoomModel;
import quiz.model.Team;
import quiz.model.TeamNameID;
import user.User;

import chat.ChatPanel;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientChangeTeamEvent;
import eventbroker.clientevent.ClientHostReadyEvent;
import eventbroker.clientevent.ClientNewTeamEvent;
import eventbroker.serverevent.ServerChangeTeamEvent;
import eventbroker.serverevent.ServerNewTeamEvent;
import eventbroker.serverevent.ServerQuizNewPlayerEvent;
import eventbroker.serverevent.ServerStartQuizEvent;

public class QuizRoomController extends EventPublisher {
	@FXML
	private TableView<TeamNameID> teamTable;
	@FXML
	private TableColumn<TeamNameID, String> NameColumn;
	@FXML
	private Label TeamnameLabel;
	@FXML
	private Label CaptainLabel;
	@FXML
	private ListView<String> teammemberslist;
	@FXML
	private Circle circle;
	@FXML
	private AnchorPane mPlaceholder;

	private QuizRoomModel quizRoomModel = new QuizRoomModel();
	private NewTeamHandler newTeamHandler;
	private ChangeTeamHandler changeTeamHandler;
	private StartQuizHandler startQuizHandler;
	private QuizNewPlayerHandler quizNewPlayerHandler;

	// Reference to the main application
	private Main main;

	public void setMain(Main main) {
		this.main = main;

		quizRoomModel.updateTeams();
		teamTable.setItems(quizRoomModel.getTeams());
	}

	// Methods
	@FXML
	private void initialize() {
		newTeamHandler = new NewTeamHandler();
		changeTeamHandler = new ChangeTeamHandler();
		startQuizHandler = new StartQuizHandler();
		quizNewPlayerHandler = new QuizNewPlayerHandler();

		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerNewTeamEvent.EVENTTYPE, newTeamHandler);
		eventBroker.addEventListener(ServerChangeTeamEvent.EVENTTYPE, changeTeamHandler);
		eventBroker.addEventListener(ServerStartQuizEvent.EVENTTYPE, startQuizHandler);
		eventBroker.addEventListener(ServerQuizNewPlayerEvent.EVENTTYPE, quizNewPlayerHandler);

		NameColumn.setCellValueFactory(cellData -> cellData.getValue().getTeamName());
		teamTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showTeamDetails(newValue));
		showTeamDetails(null);

		CaptainLabel.textProperty().bind(quizRoomModel.getCaptainName());
		TeamnameLabel.textProperty().bind(quizRoomModel.getTeamName());
		circle.fillProperty().bind(quizRoomModel.getTeamColor());
		teammemberslist.itemsProperty().bind(quizRoomModel.getTeamMembers());

		// ChatPanel (ChatModel and ChatController) are created
		ChatPanel chatPanel = ChatPanel.createChatPanel();
		mPlaceholder.getChildren().add(chatPanel.getContent());
	}

	public void showTeamDetails(TeamNameID team) {
		if (team != null)
			quizRoomModel.updateTeamDetail(team.getTeamID());
		else {
			quizRoomModel = new QuizRoomModel();
			quizRoomModel.updateTeams();
		}
	}

	@FXML
	private void handleNewTeam() {
		String errorMessage = "No error";
		if (Context.getContext().getQuiz().getHostID() != Context.getContext().getUser().getUserID()) {
			if (Context.getContext().getQuiz().getAmountOfTeams() < Context.getContext().getQuiz()
					.getMaxAmountOfTeams()) {
				User currUser = Context.getContext().getUser();
				int currTeamID = Context.getContext().getTeamID();
				int currCaptainID;

				if (currTeamID != -1)
					currCaptainID = Context.getContext().getQuiz().getTeams().get(currTeamID).getCaptainID();
				else
					currCaptainID = -1;

				if (currCaptainID != currUser.getUserID()) {
					ClientNewTeamEvent teamevent = new ClientNewTeamEvent(Context.getContext().getQuiz().getQuizID(),
							"", Color.TRANSPARENT);
					boolean okClicked = main.showNewTeam(teamevent);
					if (okClicked) {
						publishEvent(teamevent);
						System.out.println(teamevent.getTeamname());
					}
				} else
					errorMessage = "You can't create a new team, because you are already a captain of an existing team";
			} else
				errorMessage = "The maximum of teams is already reached";
		} else
			errorMessage = "You can't create a team if you are the quizmaster, click ready when you want to start the quiz";

		if (errorMessage != "No error") {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("New Team error");
			alert.setHeaderText("You can't create a new team");
			alert.setContentText(errorMessage);

			alert.showAndWait();
		}
	}

	@FXML
	private void handleReady() {
		Context context = Context.getContext();
		if (context.getQuiz().getHostID() == context.getUser().getUserID()) {
			ClientHostReadyEvent cHRE = new ClientHostReadyEvent(context.getQuiz().getQuizID(),
					context.getUser().getUserID());

			publishEvent(cHRE);
		} else {
			if (Context.getContext().getTeamID() != -1) {
				EventBroker.getEventBroker().removeEventListener(newTeamHandler);
				EventBroker.getEventBroker().removeEventListener(changeTeamHandler);
				EventBroker.getEventBroker().removeEventListener(startQuizHandler);
				EventBroker.getEventBroker().removeEventListener(quizNewPlayerHandler);

				main.showWaitRound();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(main.getPrimaryStage());
				alert.setTitle("No team Error");
				alert.setHeaderText("You can't be ready");
				alert.setContentText("First join or create a team, then click ready");

				alert.showAndWait();
			}
		}
	}

	@FXML
	private void handleJoin() {
		String errorMessage = "";
		Context context = Context.getContext();
		if (context.getQuiz().getHostID() != context.getUser().getUserID()) {
			TeamNameID selectedTeam = teamTable.getSelectionModel().getSelectedItem();
			if (selectedTeam != null) {
				User currUser = context.getUser();
				int currTeamID = context.getTeamID();

				int currCaptainID;
				if (currTeamID != -1)
					currCaptainID = Context.getContext().getQuiz().getTeams().get(currTeamID).getCaptainID();
				else
					currCaptainID = -1;

				if (selectedTeam.getTeamID() != Context.getContext().getTeamID()) {
					if (currCaptainID != currUser.getUserID()) {
						ClientChangeTeamEvent cCTE = new ClientChangeTeamEvent(
								Context.getContext().getQuiz().getQuizID(), selectedTeam.getTeamID(), currTeamID,
								currUser.getUserID());

						publishEvent(cCTE);
					} else
						errorMessage = "You are a captain, you can't join another team.";
				} else
					errorMessage = "You are already in this team.";
			} else
				errorMessage = "You didn't select a team, please select a team before you click the join button.";
		} else
			errorMessage = "You can't join a team if you are the quizmaster, click ready when you want to start the quiz.";

		if (errorMessage != "") {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("Joining error!");
			alert.setHeaderText("You couldn't join a team.");
			alert.setContentText(errorMessage);

			alert.showAndWait();
		}
	}

	private class NewTeamHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerNewTeamEvent sNTE = (ServerNewTeamEvent) event;

			int quizID = sNTE.getQuizID();
			int teamID = sNTE.getTeamID();
			String teamname = sNTE.getTeamname();
			Color color = sNTE.getColor();
			int captainID = sNTE.getCaptainID();
			String captainname = sNTE.getCaptainname();

			Context context = Context.getContext();
			// Extra check
			if (quizID == context.getQuiz().getQuizID()) {
				Team newTeam = new Team(teamID, teamname, color, captainID, captainname);
				// TableView gets updated by itself by bindings
				context.getQuiz().addTeam(newTeam);
				context.getQuiz().removeUnassignedPlayer(newTeam.getCaptainID());

				// I am the captain, change Team in context
				if (newTeam.getCaptainID() == context.getUser().getUserID())
					context.setTeamID(newTeam.getTeamID());

				quizRoomModel.updateTeams();
			}
		}

	}

	private class ChangeTeamHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerChangeTeamEvent sCTE = (ServerChangeTeamEvent) event;

			int quizID = sCTE.getQuizID();
			int newteamID = sCTE.getNewTeamID();
			int oldteamID = sCTE.getOldTeamID();
			int userID = sCTE.getUserID();
			String userName = sCTE.getUserName();

			Context context = Context.getContext();
			if (quizID == context.getQuiz().getQuizID()) {
				Team newteam = context.getQuiz().getTeams().get(newteamID);
				Team oldteam = context.getQuiz().getTeams().get(oldteamID);

				if (newteam != null) {
					// Should always happen
					newteam.addPlayer(userID, userName);
					if (context.getUser().getUserID() == userID)
						context.setTeamID(newteam.getTeamID());

					quizRoomModel.updateTeamDetail(newteam.getTeamID());
				}

				if (oldteam != null)
					oldteam.removePlayer(userID);
				else
					// remove player from the unassigned players list
					context.getQuiz().removeUnassignedPlayer(userID);
			}
		}

	}

	private class StartQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerStartQuizEvent sSQE = (ServerStartQuizEvent) event;

			Context context = Context.getContext();
			context.getQuiz().clearUnassignedPlayers();

			EventBroker.getEventBroker().removeEventListener(newTeamHandler);
			EventBroker.getEventBroker().removeEventListener(changeTeamHandler);
			EventBroker.getEventBroker().removeEventListener(startQuizHandler);
			EventBroker.getEventBroker().removeEventListener(quizNewPlayerHandler);

			if (context.getQuiz().getHostID() == context.getUser().getUserID()) {
				context.getQuiz().setRunning(true);
				main.showCreateRound();
			} else if (context.getTeamID() != -1) {
				context.getQuiz().setRunning(true);
				main.showWaitRound();
			} else {
				context.setQuiz(null);
				main.showJoinQuizScene();
			}
		}

	}

	private class QuizNewPlayerHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerQuizNewPlayerEvent sQNPE = (ServerQuizNewPlayerEvent) event;

			int userID = sQNPE.getUserID();
			String username = sQNPE.getUsername();

			Context.getContext().getQuiz().addUnassignedPlayer(userID, username);
		}

	}

}
