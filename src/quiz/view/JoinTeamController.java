package quiz.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
import quiz.model.Quiz;
import quiz.model.Team;
import quiz.model.TeamNameID;
import quiz.model.User;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Map.Entry;

import chat.ChatPanel;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientChangeTeamEvent;
import eventbroker.clientevent.ClientHostReadyEvent;
import eventbroker.clientevent.ClientCreateTeamEvent;
import eventbroker.clientevent.ClientDeleteTeamEvent;
import eventbroker.serverevent.ServerChangeTeamEvent;
import eventbroker.serverevent.ServerCreateTeamEvent;
import eventbroker.serverevent.ServerDeleteTeamEvent;
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
	private QuizDeleteTeamHandler quizDeleteTeamHandler;
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
		quizDeleteTeamHandler=new QuizDeleteTeamHandler();

		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerCreateTeamEvent.EVENTTYPE, newTeamHandler);
		eventBroker.addEventListener(ServerChangeTeamEvent.EVENTTYPE, changeTeamHandler);
		eventBroker.addEventListener(ServerStartQuizEvent.EVENTTYPE, startQuizHandler);
		eventBroker.addEventListener(ServerQuizNewPlayerEvent.EVENTTYPE, quizNewPlayerHandler);
		eventBroker.addEventListener(ServerDeleteTeamEvent.EVENTTYPE, quizDeleteTeamHandler);

		NameColumn.setCellValueFactory(cellData -> cellData.getValue().getTeamname());
		teamTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showTeamDetails(newValue));
		quizRoomModel = new JoinTeamModel();
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
			quizRoomModel.updateTeams();
		}
	}

	@FXML
	private void handleCreateTeam() {
		Context context = Context.getContext();
		String errorMessage = "";
		if (context.getQuiz().getHostID() != context.getUser().getUserID()) {
			if (context.getQuiz().getAmountOfTeams() < context.getQuiz().getTeams()) {
				User currUser = context.getUser();
				int currTeamID = context.getTeamID();
				int currCaptainID;

				if (currTeamID != -1)
					currCaptainID = context.getQuiz().getTeamMap().get(currTeamID).getCaptainID();
				else
					currCaptainID = -1;

				if (currCaptainID != currUser.getUserID()) {
					ClientCreateTeamEvent cNTE = new ClientCreateTeamEvent(context.getQuiz().getQuizID(), "",
							Color.TRANSPARENT,context.getTeamID());

					boolean okClicked = main.showCreateTeamScene(cNTE);
					if (okClicked)
						publishEvent(cNTE);
				} else
					errorMessage = "You can't create a new team, because you are already a captain of an existing team";
			} else
				errorMessage = "The maximum of teams is already reached";
		} else
			errorMessage = "You can't create a team if you are the quizmaster, click ready when you want to start the quiz";

		if (errorMessage != "") {
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

				main.showWaitRoundScene();
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
					currCaptainID = context.getQuiz().getTeamMap().get(currTeamID).getCaptainID();
				else
					currCaptainID = -1;

				if (selectedTeam.getTeamID() != context.getTeamID()) {
					if (currCaptainID != currUser.getUserID()) {
						ClientChangeTeamEvent cCTE = new ClientChangeTeamEvent(context.getQuiz().getQuizID(),
								selectedTeam.getTeamID(), currTeamID, currUser.getUserID());
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

	@FXML
	private void hadleBack() {
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.removeEventListener(newTeamHandler);
		eventBroker.removeEventListener(changeTeamHandler);
		eventBroker.removeEventListener(startQuizHandler);
		eventBroker.removeEventListener(quizNewPlayerHandler);
		//TODO handle players uit of quiz, delete the right teams... what if host leaves the quiz?
		main.showJoinQuizScene();
	}

	@FXML
	private void handleDeleteTeam(){
		String errorMessage = "";
		Context context = Context.getContext();
		if (context.getQuiz().getHostID() != context.getUser().getUserID()){
			TeamNameID selectedTeam = teamTable.getSelectionModel().getSelectedItem();
			if (selectedTeam !=null){
				if (context.getUser().getUserID()==context.getQuiz().getTeamMap().get(selectedTeam.getTeamID()).getCaptainID()){
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirmation Dialog");
					alert.setHeaderText("You are going to delete this team");
					alert.setContentText("Are you sure you want to delete this team?");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK){
						ClientDeleteTeamEvent cDTE=new ClientDeleteTeamEvent(selectedTeam.getTeamID(),context.getQuiz().getQuizID());
						publishEvent(cDTE);
					}
				}
				else {
					errorMessage="You can't delete a team if you're not the captain!";
				}
			}
			else {
				errorMessage="Please select a team if you want to delete it. You can only delete a team if you are the captain!";
			}
		}
		else {
			errorMessage = "You are the quizmaster, you can't delete teams. Only captains can delete teams!";
		}

		if (errorMessage != "") {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("Delete team error!");
			alert.setHeaderText("You can't delete this team");
			alert.setContentText(errorMessage);
			alert.showAndWait();
		}

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

				if (newteam != null) { //if the new team is a created team, changeteamevent to remove player from his old team
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

	private class QuizDeleteTeamHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerDeleteTeamEvent sDTE=(ServerDeleteTeamEvent) event;

			Quiz quiz=Context.getContext().getQuiz();
			Team team=quiz.getTeamMap().get(sDTE.getTeamID());

			if (team!=null){
				for (Entry <Integer,String> entry : team.getPlayerMap().entrySet()){
					quiz.addUnassignedPlayer(entry.getKey(),entry.getValue());
				}
				quiz.removeTeam(team.getTeamID());
				int oldTeamID=Context.getContext().getTeamID();
				if (oldTeamID==team.getTeamID()){
					Context.getContext().setTeamID(-1);
				}
				Platform.runLater(new Runnable() {
					public void run() {
						quizRoomModel.updateTeams();
						quizRoomModel.updateTeamDetail(-1);
						if (oldTeamID==team.getTeamID() && team.getCaptainID() != Context.getContext().getUser().getUserID()){
							Alert alert = new Alert(AlertType.ERROR);
							alert.initOwner(main.getPrimaryStage());
							alert.setTitle("Team deleted!");
							alert.setHeaderText("Your captain deleted your team!");
							alert.setContentText("Please choose another team or create a new team.");
							alert.showAndWait();
						}
					}
				});

			}

		}

	}

}
