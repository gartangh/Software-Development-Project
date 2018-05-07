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
import main.MainContext;
import main.Main;
import quiz.model.JoinTeamModel;
import quiz.model.Team;
import quiz.model.TeamNameID;
import chat.ChatPanel;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientCaptainReadyEvent;
import eventbroker.clientevent.ClientChangeTeamEvent;
import eventbroker.clientevent.ClientHostReadyEvent;
import eventbroker.clientevent.ClientPlayerReadyEvent;
import eventbroker.serverevent.ServerChangeTeamEvent;
import eventbroker.serverevent.ServerNewTeamEvent;
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
	private NewTeamHandler newTeamHandler = new NewTeamHandler();
	private ChangeTeamHandler changeTeamHandler = new ChangeTeamHandler();
	private StartQuizHandler startQuizHandler = new StartQuizHandler();
	private QuizNewPlayerHandler quizNewPlayerHandler = new QuizNewPlayerHandler();

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
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerNewTeamEvent.EVENTTYPE, newTeamHandler);
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
		MainContext context = MainContext.getContext();
		int userID = context.getUser().getUserID();
		int hostID = context.getQuiz().getHostID();
		Team team = context.getTeam();
		int captainID = -1;
		if (team != null)
			captainID = team.getCaptainID();

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
		} else if (context.getQuiz().getAmountOfTeams() >= context.getQuiz().getTeams()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("You can't create a team!");
					alert.setContentText("The maximum amount of teams in this quiz is already reached.");
					alert.showAndWait();
				}
			});

			return;
		}

		// You can create a team
		main.showCreateTeamScene();
	}

	@FXML
	private void handleReady() {
		MainContext context = MainContext.getContext();
		int userID = context.getUser().getUserID();
		int quizID = context.getQuiz().getQuizID();
		int hostID = context.getQuiz().getHostID();
		Team team = context.getTeam();

		if (team == null) {
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

			return;
		}

		// The EventListeners should not yet be deleted!

		if (hostID == userID) {
			// The host is ready
			// TODO Only press ready as a host, when there are at least
			// Quiz.MINTEAMS teams ready
			ClientHostReadyEvent cHRE = new ClientHostReadyEvent(quizID);
			publishEvent(cHRE);

			main.showCreateRoundScene();
		} else if (team.getCaptainID() == userID) {
			// The captain is ready
			// TODO Only press ready as a captain, when there are at least
			// Quiz.MINPLAYERS - 1 players (other than the captain) ready
			ClientCaptainReadyEvent cCRE = new ClientCaptainReadyEvent(quizID, team.getTeamID());
			publishEvent(cCRE);

			main.showWaitRoundScene();
		} else {
			// A player is ready
			ClientPlayerReadyEvent cPRE = new ClientPlayerReadyEvent(quizID, team.getTeamID());
			publishEvent(cPRE);

			main.showWaitRoundScene();
		}

	}

	@FXML
	private void handleJoin() {
		MainContext context = MainContext.getContext();
		int userID = context.getUser().getUserID();
		int quizID = context.getQuiz().getQuizID();
		int hostID = context.getQuiz().getHostID();
		Team team = context.getTeam();
		int captainID = -1;
		if (team != null)
			captainID = team.getCaptainID();

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
		} else if (team != null && selectedTeam.getTeamID() == team.getTeamID()) {
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
		} else if (captainID == userID) {
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
		} else if (context.getQuiz().getTeamMap().get(selectedTeam.getTeamID()).getPlayerMap().size() >= context
				.getQuiz().getTeamMap().get(selectedTeam.getTeamID()).getPlayers()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setHeaderText("You couldn't join a team!");
					alert.setContentText("The maximum amount of players in this team is reached.");
					alert.showAndWait();
				}
			});
		}

		ClientChangeTeamEvent cCTE;
		if (team == null)
			cCTE = new ClientChangeTeamEvent(quizID, selectedTeam.getTeamID(), -1);
		else
			cCTE = new ClientChangeTeamEvent(quizID, selectedTeam.getTeamID(), team.getTeamID());
		publishEvent(cCTE);
	}

	@FXML
	private void hadleBack() {
		MainContext.getContext().setQuiz(null);

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
			ServerNewTeamEvent sNTE = (ServerNewTeamEvent) event;

			int quizID = sNTE.getQuizID();
			int teamID = sNTE.getTeamID();
			String teamname = sNTE.getTeamname();
			Color color = sNTE.getColor();
			int captainID = sNTE.getCaptainID();
			String captainname = sNTE.getCaptainname();
			int players = sNTE.getPlayers();

			MainContext context = MainContext.getContext();
			// Extra check
			if (quizID == context.getQuiz().getQuizID()) {
				Team newTeam = Team.createTeam(quizID, teamID, teamname, color, captainID, captainname, players);
				// TableView gets updated by itself by bindings
				context.getQuiz().addTeam(newTeam);
				context.getQuiz().removeUnassignedPlayer(newTeam.getCaptainID());

				// I am the captain, change Team in context
				if (newTeam.getCaptainID() == context.getUser().getUserID())
					context.setTeam(newTeam);

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

			MainContext context = MainContext.getContext();
			if (quizID == context.getQuiz().getQuizID()) {
				Team newTeam = context.getQuiz().getTeamMap().get(newteamID);
				Team oldTeam = context.getQuiz().getTeamMap().get(oldteamID);

				if (newTeam != null) {
					newTeam.addPlayer(userID, userName);

					if (context.getUser().getUserID() == userID)
						context.setTeam(newTeam);

					quizRoomModel.updateTeamDetail(newTeam.getTeamID());
				}

				if (oldTeam != null)
					oldTeam.removePlayer(userID);
				else
					// Remove player from the unassigned players list
					context.getQuiz().removeUnassignedPlayer(userID);
			}
		}

	}

	private class StartQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerStartQuizEvent sSQE = (ServerStartQuizEvent) event;

			MainContext context = MainContext.getContext();
			context.getQuiz().clearUnassignedPlayers();

			// Only now the EventListeners can be deleted!
			EventBroker.getEventBroker().removeEventListener(newTeamHandler);
			EventBroker.getEventBroker().removeEventListener(changeTeamHandler);
			EventBroker.getEventBroker().removeEventListener(startQuizHandler);
			EventBroker.getEventBroker().removeEventListener(quizNewPlayerHandler);

			// All unassigned players
			if (context.getTeam() == null) {
				context.setQuiz(null);

				main.showJoinQuizScene();

				return;
			}

			// The host
			if (context.getQuiz().getHostID() == context.getUser().getUserID()) {
				context.getQuiz().setRunning(true);

				main.showCreateRoundScene();

				return;
			}

			// Other users (normal players)
			context.getQuiz().setRunning(true);

			main.showWaitRoundScene();
		}

	}

	private class QuizNewPlayerHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerQuizNewPlayerEvent sQNPE = (ServerQuizNewPlayerEvent) event;

			int userID = sQNPE.getUserID();
			String username = sQNPE.getUsername();

			MainContext.getContext().getQuiz().addUnassignedPlayer(userID, username);
		}

	}

}
