package quiz.view;

import javafx.application.Platform;
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
import quiz.model.JoinTeamModel;
import quiz.model.Team;
import quiz.model.TeamNameID;
import chat.ChatPanel;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientChangeTeamEvent;
import eventbroker.clientevent.ClientHostReadyEvent;
import eventbroker.clientevent.ClientCreateTeamEvent;
import eventbroker.serverevent.ServerChangeTeamEvent;
import eventbroker.serverevent.ServerCreateTeamEvent;
import eventbroker.serverevent.ServerQuizNewPlayerEvent;
import eventbroker.serverevent.ServerStartQuizEvent;

public class JoinTeamController extends EventPublisher {

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

	private JoinTeamModel quizRoomModel = new JoinTeamModel();
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
		eventBroker.addEventListener(ServerCreateTeamEvent.EVENTTYPE, newTeamHandler);
		eventBroker.addEventListener(ServerChangeTeamEvent.EVENTTYPE, changeTeamHandler);
		eventBroker.addEventListener(ServerStartQuizEvent.EVENTTYPE, startQuizHandler);
		eventBroker.addEventListener(ServerQuizNewPlayerEvent.EVENTTYPE, quizNewPlayerHandler);

		NameColumn.setCellValueFactory(cellData -> cellData.getValue().getTeamname());
		teamTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showTeamDetails(newValue));
		showTeamDetails(null);

		CaptainLabel.textProperty().bind(quizRoomModel.getCaptainname());
		TeamnameLabel.textProperty().bind(quizRoomModel.getTeamname());
		circle.fillProperty().bind(quizRoomModel.getColor());
		teammemberslist.itemsProperty().bind(quizRoomModel.getMembers());

		// ChatPanel (ChatModel and ChatController) are created
		ChatPanel chatPanel = ChatPanel.createChatPanel();
		mPlaceholder.getChildren().add(chatPanel.getContent());
	}

	public void showTeamDetails(TeamNameID team) {
		if (team != null)
			quizRoomModel.updateTeamDetail(team.getTeamID());
		else {
			quizRoomModel = new JoinTeamModel();
			quizRoomModel.updateTeams();
		}
	}

	@FXML
	private void handleCreateTeam() {
		Context context = Context.getContext();
		int userID = context.getUser().getUserID();
		int quizID = context.getQuiz().getQuizID();
		int hostID = context.getQuiz().getHostID();
		int teamID = context.getTeamID();
		int captainID = -1;
		if (teamID != -1)
			captainID = context.getQuiz().getTeamMap().get(teamID).getCaptainID();

		if (hostID == userID) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("You can't create a team!");
					alert.setContentText("You're the host, click ready when you want to start the quiz.");
					alert.showAndWait();
				}
			});

			return;
		} else if (context.getQuiz().getAmountOfTeams() >= context.getQuiz().getTeams()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("You can't create a team!");
					alert.setContentText("The maximum amount of teams is already reached.");
					alert.showAndWait();
				}
			});

			return;
		} else if (captainID == userID) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("You can't create a team!");
					alert.setContentText("You're already captain of an existing team.");
					alert.showAndWait();
				}
			});

			return;
		}

		// You can create a team
		ClientCreateTeamEvent cNTE = new ClientCreateTeamEvent(quizID, "", Color.TRANSPARENT);
		if (main.showCreateTeamScene(cNTE))
			publishEvent(cNTE);
	}

	@FXML
	private void handleReady() {
		Context context = Context.getContext();
		int userID = context.getUser().getUserID();
		int quizID = context.getQuiz().getQuizID();
		int hostID = context.getQuiz().getHostID();
		int teamID = context.getTeamID();

		if (hostID != userID) {
			if (teamID != -1) {
				EventBroker.getEventBroker().removeEventListener(newTeamHandler);
				EventBroker.getEventBroker().removeEventListener(changeTeamHandler);
				EventBroker.getEventBroker().removeEventListener(startQuizHandler);
				EventBroker.getEventBroker().removeEventListener(quizNewPlayerHandler);

				main.showWaitRoundScene();
			} else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warning");
						alert.setHeaderText("You can't be ready when you're not part of a team!");
						alert.setContentText("First join or create a team, then click ready.");
						alert.showAndWait();
					}
				});
			}

			return;
		}

		ClientHostReadyEvent cHRE = new ClientHostReadyEvent(quizID, userID);
		publishEvent(cHRE);
	}

	@FXML
	private void handleJoin() {
		Context context = Context.getContext();
		int userID = context.getUser().getUserID();
		int quizID = context.getQuiz().getQuizID();
		int hostID = context.getQuiz().getHostID();
		int teamID = context.getTeamID();
		int captainID = -1;
		if (teamID != -1)
			captainID = context.getQuiz().getTeamMap().get(teamID).getCaptainID();

		TeamNameID selectedTeam = teamTable.getSelectionModel().getSelectedItem();

		if (hostID == userID) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("You couldn't join a team!");
					alert.setContentText(
							"The quizmaster can't join a team, click ready when you want to start the quiz.");
					alert.showAndWait();
				}
			});

			return;
		} else if (selectedTeam == null) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("You couldn't join a team!");
					alert.setContentText("You didn't select a team, please select a team and try again.");
					alert.showAndWait();
				}
			});

			return;
		} else if (selectedTeam.getTeamID() != teamID) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("You couldn't join the team!");
					alert.setContentText("You're already in this team.");
					alert.showAndWait();
				}
			});

			return;
		} else if (captainID != userID) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("You couldn't join a team!");
					alert.setContentText("You are a captain, you can't join another team.");
					alert.showAndWait();
				}
			});

			return;
		}

		ClientChangeTeamEvent cCTE = new ClientChangeTeamEvent(quizID, selectedTeam.getTeamID(), teamID, userID);
		publishEvent(cCTE);
	}

	@FXML
	private void hadleBack() {
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.removeEventListener(newTeamHandler);
		eventBroker.removeEventListener(changeTeamHandler);
		eventBroker.removeEventListener(startQuizHandler);
		eventBroker.removeEventListener(quizNewPlayerHandler);

		main.showJoinQuizScene();
	}

	// Inner classes
	private class NewTeamHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerCreateTeamEvent sNTE = (ServerCreateTeamEvent) event;

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
				Team newteam = context.getQuiz().getTeamMap().get(newteamID);
				Team oldteam = context.getQuiz().getTeamMap().get(oldteamID);

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

				main.showCreateRoundScene();
			} else if (context.getTeamID() != -1) {
				context.getQuiz().setRunning(true);

				main.showWaitRoundScene();
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
