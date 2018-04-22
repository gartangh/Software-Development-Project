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
import user.model.User;

import java.io.IOException;

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
import eventbroker.serverevent.ServerQuizNewPlayer;

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

	private QuizroomHandler quizroomhandler = new QuizroomHandler();
	private QuizRoomModel quizRoomModel = new QuizRoomModel();

	// Reference to the main application
	private Main main;

	public void setMain(Main main) {
		this.main = main;
		quizRoomModel.updateTeams();
		teamTable.setItems(quizRoomModel.getTeams());
	}

	public QuizRoomController() {
		// Empty default constructor
	}

	// Inner class
	public class QuizroomHandler implements EventListener {
		public void handleEvent(Event event) {
			switch (event.getType()) {
			case "SERVER_NEW_TEAM":
				ServerNewTeamEvent newTeamEvent = (ServerNewTeamEvent) event;
				if (newTeamEvent.getQuizID() == Context.getContext().getQuiz().getQuizID()) {// extra
																								// controle
					Team newTeam = new Team(newTeamEvent.getTeamID(), newTeamEvent.getTeamName(),
							newTeamEvent.getColor(), newTeamEvent.getCaptainID(), newTeamEvent.getCaptainName());
					// TableView gets updated by itself by bindings
					Context.getContext().getQuiz().addTeam(newTeam);
					Context.getContext().getQuiz().removeUnassignedPlayer(newTeam.getCaptainID());

					// I am the captain, change Team in context
					if (newTeam.getCaptainID() == Context.getContext().getUser().getUserID()) {
						Context.getContext().setTeamID(newTeam.getTeamID());
					}
					quizRoomModel.updateTeams();
				}
				break;

			case "SERVER_CHANGE_TEAM":
				ServerChangeTeamEvent cte = (ServerChangeTeamEvent) event;
				if (cte.getQuizID() == Context.getContext().getQuiz().getQuizID()) {
					int newteamID = cte.getNewTeamID();
					int oldteamID = cte.getOldTeamID();
					int userID = cte.getUserID();
					String userName = cte.getUserName();
					Team newteam = null;
					Team oldteam = null;

					newteam = Context.getContext().getQuiz().getTeams().get(newteamID);
					oldteam = Context.getContext().getQuiz().getTeams().get(oldteamID);

					if (newteam != null) {// should always happen
						newteam.addPlayer(userID, userName);
						if (Context.getContext().getUser().getUserID() == userID) {
							Context.getContext().setTeamID(newteam.getTeamID());
						}
						quizRoomModel.updateTeamDetail(newteam.getTeamID());
					}
					if (oldteam != null) {
						oldteam.removePlayer(userID);
					} else {// remove player from the unassignedlist
						Context.getContext().getQuiz().removeUnassignedPlayer(userID);
					}
				}
				break;

			case "SERVER_START_QUIZ":
				EventBroker.getEventBroker().removeEventListener(quizroomhandler);
				Context.getContext().getQuiz().clearUnassignedPlayers();
				if (Context.getContext().getQuiz().getQuizmaster() == Context.getContext().getUser().getUserID()) {

					main.showCreateRound();
					Context.getContext().getQuiz().setRunning(true);
				} else if (Context.getContext().getTeamID() != -1) {
					main.showWaitRound();
					Context.getContext().getQuiz().setRunning(true);
				} else {
					Context.getContext().setQuiz(null);
					main.showJoinQuizScene();
				}
				break;
			case "SERVER_QUIZ_NEW_PLAYER":
				ServerQuizNewPlayer sQNP = (ServerQuizNewPlayer) event;
				Context.getContext().getQuiz().addUnassignedPlayer(sQNP.getUserID(), sQNP.getUsername());

			default:
				System.out.println("Event received but left unhandled: " + event.getType() + "in quizroom");
			}
		}
	}

	@FXML
	private void initialize() {
		EventBroker.getEventBroker().addEventListener(quizroomhandler);

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
		if (team != null) {
			quizRoomModel.updateTeamDetail(team.getTeamID());
		} else {
			quizRoomModel = new QuizRoomModel();
			quizRoomModel.updateTeams();
		}
	}

	@FXML
	private void handleNewTeam() throws IOException {
		String errorMessage = "No error";
		if (Context.getContext().getQuiz().getQuizmaster() != Context.getContext().getUser().getUserID()) {
			if (Context.getContext().getQuiz().getAmountOfTeams() < Context.getContext().getQuiz()
					.getMaxAmountOfTeams()) {
				User currUser = Context.getContext().getUser();
				int currTeamID = Context.getContext().getTeamID();
				int currCaptainID;

				if (currTeamID != -1) {
					currCaptainID = Context.getContext().getQuiz().getTeams().get(currTeamID).getCaptainID();
				} else {
					currCaptainID = -1;
				}

				if (currCaptainID != currUser.getUserID()) {
					ClientNewTeamEvent teamevent = new ClientNewTeamEvent(Context.getContext().getQuiz().getQuizID(),
							"", Color.TRANSPARENT);
					boolean okClicked = main.showNewTeam(teamevent);
					if (okClicked) {
						publishEvent(teamevent);
						System.out.println(teamevent.getTeamName());
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
		if (Context.getContext().getQuiz().getQuizmaster() == Context.getContext().getUser().getUserID()) {
			ClientHostReadyEvent e = new ClientHostReadyEvent(Context.getContext().getQuiz().getQuizID(),
					Context.getContext().getUser().getUserID());
			publishEvent(e);

		} else {
			if (Context.getContext().getTeamID() != -1) {
				EventBroker.getEventBroker().removeEventListener(quizroomhandler);
				main.showWaitRound();
			}

			else {
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
		if (Context.getContext().getQuiz().getQuizmaster() != Context.getContext().getUser().getUserID()) {
			TeamNameID selectedTeam = teamTable.getSelectionModel().getSelectedItem();
			if (selectedTeam != null) {
				User currUser = Context.getContext().getUser();
				int currTeamID = Context.getContext().getTeamID();
				int currCaptainID;

				if (currTeamID != -1) {
					currCaptainID = Context.getContext().getQuiz().getTeams().get(currTeamID).getCaptainID();
				} else {
					currCaptainID = -1;
				}

				if (selectedTeam.getTeamID() != Context.getContext().getTeamID()) {
					if (currCaptainID != currUser.getUserID()) {
						ClientChangeTeamEvent changeTeamEvent = new ClientChangeTeamEvent(
								Context.getContext().getQuiz().getQuizID(), selectedTeam.getTeamID(), currTeamID,
								currUser.getUserID());
						publishEvent(changeTeamEvent);
						// TODO: Error handling
					} else
						errorMessage = "You are a captain, you can't join another team";
				} else
					errorMessage = "You are already in this team";
			} else
				errorMessage = "You didn't select a team, please select a team before you click the join button";
		} else
			errorMessage = "You can't join a team if you are the quizmaster, click ready when you want to start the quiz";

		if (errorMessage != "") {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("Joining error");
			alert.setHeaderText("You couldn't join a team");
			alert.setContentText(errorMessage);

			alert.showAndWait();
		}
	}
}
