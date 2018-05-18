package quiz.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import main.MainContext;
import main.Main;
import quiz.model.JoinTeamModel;
import quiz.model.Quiz;
import quiz.model.Team;
import quiz.model.TeamNameID;
import quiz.model.User;
import server.timertask.ClientCheckPollTimerTask;

import java.util.Optional;
import java.util.Map.Entry;
import chat.ChatPanel;
import eventbroker.ClientPollHandler;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.HostLeavesQuizHandler;
import eventbroker.PlayerLeavesQuizHandler;
import eventbroker.clientevent.ClientChangeTeamEvent;
import eventbroker.clientevent.ClientHostReadyEvent;
import eventbroker.clientevent.ClientLeaveQuizEvent;
import eventbroker.clientevent.ClientCreateTeamEvent;
import eventbroker.clientevent.ClientDeleteTeamEvent;
import eventbroker.serverevent.ServerChangeTeamEvent;
import eventbroker.serverevent.ServerCreateTeamFailEvent;
import eventbroker.serverevent.ServerDeleteTeamEvent;
import eventbroker.serverevent.ServerHostLeavesQuizEvent;
import eventbroker.serverevent.ServerPlayerLeavesQuizEvent;
import eventbroker.serverevent.ServerNewTeamEvent;
import eventbroker.serverevent.ServerQuizNewPlayerEvent;
import eventbroker.serverevent.ServerStartQuizEvent;

public class JoinTeamController extends EventPublisher {

	@FXML
	private TableView<TeamNameID> teamTable;
	@FXML
	private TableColumn<TeamNameID, String> teamNameColumn;
	@FXML
	private TableColumn<TeamNameID, String> captainNameColumn;
	@FXML
	private ListView<String> unassignedPlayerList;
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
	@FXML
	private Button readyButton;

	private JoinTeamModel quizRoomModel = new JoinTeamModel();
	private NewTeamHandler newTeamHandler = new NewTeamHandler();
	private ChangeTeamHandler changeTeamHandler = new ChangeTeamHandler();
	private StartQuizHandler startQuizHandler = new StartQuizHandler();
	private QuizNewPlayerHandler quizNewPlayerHandler = new QuizNewPlayerHandler();
	private QuizDeleteTeamHandler quizDeleteTeamHandler = new QuizDeleteTeamHandler();
	private HostLeavesQuizHandler hostLeavesQuizHandler = new HostLeavesQuizHandler();
	private PlayerLeavesQuizHandler playerLeavesQuizHandler = new PlayerLeavesQuizHandler();
	private CreateTeamFailHandler createTeamFailHandler = new CreateTeamFailHandler();

	// Reference to the main application
	private Main main;

	public void setMain(Main main) {
		this.main = main;
		hostLeavesQuizHandler.setMain(main);
		playerLeavesQuizHandler.setMain(main);
		playerLeavesQuizHandler.setJoinTeamController(this);
		
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerPlayerLeavesQuizEvent.EVENTTYPE, playerLeavesQuizHandler);
		eventBroker.addEventListener(ServerHostLeavesQuizEvent.EVENTTYPE, hostLeavesQuizHandler);

	}

	// Methods
	@FXML
	private void initialize() {

		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerNewTeamEvent.EVENTTYPE, newTeamHandler);
		eventBroker.addEventListener(ServerChangeTeamEvent.EVENTTYPE, changeTeamHandler);
		eventBroker.addEventListener(ServerStartQuizEvent.EVENTTYPE, startQuizHandler);
		eventBroker.addEventListener(ServerQuizNewPlayerEvent.EVENTTYPE, quizNewPlayerHandler);
		eventBroker.addEventListener(ServerDeleteTeamEvent.EVENTTYPE, quizDeleteTeamHandler);
		eventBroker.addEventListener(ServerCreateTeamFailEvent.EVENTTYPE, createTeamFailHandler);
		
		ClientPollHandler.activateClientPollHandler();
		ClientCheckPollTimerTask.getClientCheckPollTimerTask().activate();

		teamNameColumn.setCellValueFactory(cellData -> cellData.getValue().getTeamname());
		captainNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCaptainName());
		teamTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showTeamDetails(newValue));
		quizRoomModel = new JoinTeamModel();
		showTeamDetails(null);

		teamTable.setRowFactory(new Callback<TableView<TeamNameID>, TableRow<TeamNameID>>() {
	        @Override
	        public TableRow<TeamNameID> call(TableView<TeamNameID> param) {
	            return new TableRow<TeamNameID>() {
	            	@Override
	            	protected void updateItem(TeamNameID item, boolean empty) {
	            	    super.updateItem(item, empty);
	            	    Team team =MainContext.getContext().getTeam();
	            	    if (team != null && item !=null){
		            	    if (item.getTeamID()==team.getTeamID()) {
		            	        setStyle("-fx-font-weight: bold");
		            	    } else  {
		            	        setStyle("");
		            	    }
	            	    }
	            	    else setStyle("");
	            	}
	            };
	        }
	    });

		readyButton.textProperty().bind(quizRoomModel.getStartButtonText());
		CaptainLabel.textProperty().bind(quizRoomModel.getCaptainname());
		TeamnameLabel.textProperty().bind(quizRoomModel.getTeamname());
		circle.fillProperty().bind(quizRoomModel.getColor());
		teammemberslist.itemsProperty().bind(quizRoomModel.getMembers());
		unassignedPlayerList.itemsProperty().bind(quizRoomModel.getUnassignedPlayers());


		unassignedPlayerList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
	        @Override
	        public ListCell<String> call(ListView<String> param) {
	            return new ListCell<String>() {
	            	@Override
	            	protected void updateItem(String playerName, boolean empty) {
	            	    super.updateItem(playerName, empty);
	            	    User user =MainContext.getContext().getUser();
	            	    if (user != null && playerName != null){
		            	    if (user.getUsername().equals(playerName)) {
		            	    	setStyle("-fx-font-weight: bold");
		            	    } else  {
		            	        setStyle("");
		            	    }
	            	    }
	            	    else setStyle("");
	            	    setText(playerName);
	            	}
	            };
	        }
	    });

		teammemberslist.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
	        @Override
	        public ListCell<String> call(ListView<String> param) {
	            return new ListCell<String>() {
	            	@Override
	            	protected void updateItem(String playerName, boolean empty) {
	            	    super.updateItem(playerName, empty);
	            	    User user =MainContext.getContext().getUser();
	            	    if (user != null && playerName != null){
		            	    if (user.getUsername().equals(playerName)) {
		            	    	setStyle("-fx-font-weight: bold");
		            	    } else  {
		            	        setStyle("");
		            	    }
	            	    }
	            	    else setStyle("");
	            	    setText(playerName);
	            	}
	            };
	        }
	    });

		// ChatPanel (ChatModel and ChatController) are created
		ChatPanel chatPanel = ChatPanel.createChatPanel();
		mPlaceholder.getChildren().add(chatPanel.getContent());

		quizRoomModel.updateTeams();
		quizRoomModel.updateUnassignedPlayers();
		quizRoomModel.setStartButtonText(MainContext.getContext());
		teamTable.setItems(quizRoomModel.getTeams());

	}

	public void showTeamDetails(TeamNameID team) {
		if (team != null)
			quizRoomModel.updateTeamDetail(team.getTeamID());
		else {
			quizRoomModel.updateTeamDetail(-1);
		}
	}

	public void updateViews(){
		TeamNameID selectedTeam = teamTable.getSelectionModel().getSelectedItem();
		quizRoomModel.updateTeams();
		quizRoomModel.updateUnassignedPlayers();
		showTeamDetails(selectedTeam);
	}

	@FXML
	private void handleCreateTeam() {
		MainContext context = MainContext.getContext();
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
							Color.TRANSPARENT, context.getTeamID(), context.getUser().getUsername());

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
		MainContext context = MainContext.getContext();
		int userID = context.getUser().getUserID();
		int quizID = context.getQuiz().getQuizID();
		int hostID = context.getQuiz().getHostID();
		Team team = context.getTeam();

		if (team == null && hostID != userID) {
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
			Quiz quiz=MainContext.getContext().getQuiz();

			if (quiz.getTeamMap().size()>=Quiz.MINTEAMS){
				ClientHostReadyEvent cHRE = new ClientHostReadyEvent(quizID);
				publishEvent(cHRE);
			}
			else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText("Not enough teams!");
				alert.setContentText("There have to be at least " + Quiz.MINTEAMS + " teams before you can start the quiz. Please wait until there are enough teams");
				alert.showAndWait();
			}
		} else {
			// A player is ready
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
		} else if (captainID == userID)

		{
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
	private void handleBack() {
		MainContext context = MainContext.getContext();

		if (context.getQuiz().getHostID() == context.getUser().getUserID()) {
			// You are the host
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation");
			alert.setHeaderText("You are going to end the quiz!");
			alert.setContentText("Are you sure you want to end the quiz?");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.CANCEL)
				return;
		}

		ClientLeaveQuizEvent cLQE = new ClientLeaveQuizEvent(context.getUser().getUserID(),
				context.getQuiz().getQuizID(), context.getTeamID());
		publishEvent(cLQE);
	}

	@FXML
	private void handleDeleteTeam() {
		String errorMessage = "";
		MainContext context = MainContext.getContext();
		if (context.getQuiz().getHostID() != context.getUser().getUserID()) {
			TeamNameID selectedTeam = teamTable.getSelectionModel().getSelectedItem();
			if (selectedTeam != null) {
				if (context.getUser().getUserID() == context.getQuiz().getTeamMap().get(selectedTeam.getTeamID())
						.getCaptainID()) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirmation Dialog");
					alert.setHeaderText("You are going to delete this team");
					alert.setContentText("Are you sure you want to delete this team?");

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK) {
						ClientDeleteTeamEvent cDTE = new ClientDeleteTeamEvent(selectedTeam.getTeamID(),
								context.getQuiz().getQuizID());
						publishEvent(cDTE);
					}
				} else {
					errorMessage = "You can't delete a team if you're not the captain!";
				}
			} else {
				errorMessage = "Please select a team if you want to delete it. You can only delete a team if you are the captain!";
			}
		} else {
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
				Team.createTeam(quizID, teamID, teamname, color, captainID, captainname, players);
				quizRoomModel.updateTeams();
				quizRoomModel.updateUnassignedPlayers();
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
				}

				if (oldTeam != null)
					oldTeam.removePlayer(userID);
				else
					context.getQuiz().removeUnassignedPlayer(userID);

				TeamNameID selectedTeam=teamTable.getSelectionModel().getSelectedItem();
				showTeamDetails(selectedTeam);
				quizRoomModel.updateUnassignedPlayers();
				if (userID==context.getUser().getUserID()) {
					quizRoomModel.triggerTeams();//To change my team to bold
				}

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
			EventBroker.getEventBroker().removeEventListener(createTeamFailHandler);
			EventBroker.getEventBroker().removeEventListener(quizDeleteTeamHandler);

			// The host
			if (context.getQuiz().getHostID() == context.getUser().getUserID()) {
				context.getQuiz().setRunning(true);

				main.showCreateRoundScene();

				return;
			}

			// All unassigned players
			if (context.getTeam() == null) {
				context.setQuiz(null);

				main.showJoinQuizScene();

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
			quizRoomModel.updateUnassignedPlayers();
		}

	}

	private class QuizDeleteTeamHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerDeleteTeamEvent sDTE = (ServerDeleteTeamEvent) event;

			Quiz quiz=MainContext.getContext().getQuiz();
			Team team=quiz.getTeamMap().get(sDTE.getTeamID());
			TeamNameID selectedTeam=teamTable.getSelectionModel().getSelectedItem();

			if (team != null) {
				for (Entry<Integer, String> entry : team.getPlayerMap().entrySet()) {
					quiz.addUnassignedPlayer(entry.getKey(), entry.getValue());
				}
				quiz.removeTeam(team.getTeamID());
				int oldTeamID = MainContext.getContext().getTeamID();
				if (oldTeamID == team.getTeamID()) {
					MainContext.getContext().setTeam(null);
				}
				Platform.runLater(new Runnable() {
					public void run() {
						showTeamDetails(selectedTeam);
						quizRoomModel.updateTeams();
						quizRoomModel.updateUnassignedPlayers();

						if (oldTeamID==team.getTeamID() && team.getCaptainID() != MainContext.getContext().getUser().getUserID()){
							Alert alert = new Alert(AlertType.WARNING);
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

	// Inner classes
	private class CreateTeamFailHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerCreateTeamFailEvent sCTFE = (ServerCreateTeamFailEvent) event;

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Team creation failed!");
					alert.setContentText("The teamname already exists.");
					alert.showAndWait();
				}
			});
		}
	}

}
