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
import quiz.model.*;
import quiz.util.ChangeTeamEvent;
import quiz.util.ClientHostReadyEvent;
import quiz.util.NewTeamEvent;
import main.*;
import user.model.*;
import server.*;

import java.io.IOException;

import chat.ChatPanel;
import eventbroker.*;

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

	public class QuizroomHandler implements EventListener {
		public void handleEvent(Event event) {
			switch (event.getType()) {
			case "SERVER_NEW_TEAM":
				ServerNewTeamEvent newTeamEvent = (ServerNewTeamEvent) event;
				if (newTeamEvent.getQuizID() == Context.getContext().getQuiz().getQuizID()) {// extra
																								// controle
					Team newTeam = new Team(newTeamEvent.getTeamID(), newTeamEvent.getTeamName(),
							newTeamEvent.getColor(), newTeamEvent.getCaptainID(), newTeamEvent.getCaptainName());
					Context.getContext().getQuiz().addTeam(newTeam);// TAbleview
																	// vanzelf
																	// geupdatet
																	// via
																	// bindings
					Context.getContext().getQuiz().removeUnassignedPlayer(newTeam.getCaptainID());
					if (newTeam.getCaptainID() == Context.getContext().getUser().getID()) {// i
																							// am
																							// the
																							// captain,change
																							// Team
																							// in
																							// context
						Context.getContext().setTeamID(newTeam.getTeamID());
					}
					quizRoomModel.updateTeams();
					quizRoomModel.updateTeamDetail(newTeam.getTeamID());
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
						if (Context.getContext().getUser().getID() == userID) {
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
				if (Context.getContext().getQuiz().getQuizmaster() == Context.getContext().getUser().getUserID())
					main.showCreateRound();
				else
					main.showWaitRound();
				break;

			default:
				System.out.println("Event received but left unhandled: " + event.getType() + "in quizroom");
			}
		}
	}

	public QuizRoomController(Quiz quiz) {
		// this.quiz=quiz;
		// via serverContext rechtstreeks? wordt nu meegegeven via argument maar
		// hoeft niet
	}

	public void addListener() {
		EventBroker.getEventBroker().addEventListener(quizroomhandler);
	}

	@FXML
	private void initialize() {
		NameColumn.setCellValueFactory(cellData -> cellData.getValue().getTeamName());
		teamTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showTeamDetails(newValue));
		showTeamDetails(null);

		CaptainLabel.textProperty().bind(quizRoomModel.getCaptainName());
		TeamnameLabel.textProperty().bind(quizRoomModel.getTeamName());
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

				if (currCaptainID != currUser.getID()) {
					NewTeamEvent teamevent = new NewTeamEvent(Context.getContext().getQuiz().getQuizID(), "",
							Color.TRANSPARENT);
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
			main.showWaitRound();
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
					if (currCaptainID != currUser.getID()) {
						ChangeTeamEvent changeTeamEvent = new ChangeTeamEvent(
								Context.getContext().getQuiz().getQuizID(), selectedTeam.getTeamID(), currTeamID,
								currUser.getID());
						publishEvent(changeTeamEvent);
						// TODO error handling,
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
