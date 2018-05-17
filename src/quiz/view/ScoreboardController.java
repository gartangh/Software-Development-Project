package quiz.view;

import java.util.ArrayList;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.PlayerLeavesQuizHandler;
import eventbroker.clientevent.ClientEndQuizEvent;
import eventbroker.clientevent.ClientScoreboardDataEvent;
import eventbroker.serverevent.ServerScoreboardDataEvent;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import main.MainContext;
import main.Main;
import quiz.model.ScoreboardModel;
import quiz.model.ScoreboardTeam;
import quiz.model.Team;
import quiz.model.TeamNameID;

public class ScoreboardController extends EventPublisher {

	@FXML
	private TableView<ScoreboardTeam> scoreboardTable;
	@FXML
	private TableColumn<ScoreboardTeam, Integer> rankColumn;
	@FXML
	private TableColumn<ScoreboardTeam, String> teamNameColumn;
	@FXML
	private TableColumn<ScoreboardTeam, Integer> scoreColumn;
	@FXML
	private Label winnerLoser;

	private ScoreboardModel scoreboardModel = new ScoreboardModel();
	private ScoreboardDataHandler scoreboardDataHandler = new ScoreboardDataHandler();

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}

	// Getters
	public ScoreboardModel getScoreboardModel() {
		return scoreboardModel;
	}

	// Methods
	@FXML
	private void initialize() {
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerScoreboardDataEvent.EVENTTYPE, scoreboardDataHandler);

		scoreboardTable.setItems(scoreboardModel.getScoreboardTeams());
		
		winnerLoser.textProperty().bind(scoreboardModel.getWinnerLoserProperty());
		rankColumn
				.setCellValueFactory(cellData -> (new SimpleIntegerProperty(cellData.getValue().getRank()).asObject()));
		teamNameColumn.setCellValueFactory(cellData -> (new SimpleStringProperty(cellData.getValue().getTeamname())));
		scoreColumn.setCellValueFactory(
				cellData -> (new SimpleIntegerProperty(cellData.getValue().getScore()).asObject()));

		
		scoreboardTable.setRowFactory(new Callback<TableView<ScoreboardTeam>, TableRow<ScoreboardTeam>>() {
	        @Override
	        public TableRow<ScoreboardTeam> call(TableView<ScoreboardTeam> param) {
	            return new TableRow<ScoreboardTeam>() {
	            	@Override
	            	protected void updateItem(ScoreboardTeam item, boolean empty) {
	            	    super.updateItem(item, empty);
	            	    Team team = MainContext.getContext().getTeam();
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
		
		
		if(MainContext.getContext().getUser().getUserID() == MainContext.getContext().getQuiz().getHostID()) {
			ClientScoreboardDataEvent cSDE = new ClientScoreboardDataEvent();
			publishEvent(cSDE);
		}
	}

	/**
	 * Called when the user clicks on the quit button.
	 */
	@FXML
	private void handleBack() {
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.removeEventListeners();
		
		MainContext context = MainContext.getContext();
		if (context.getQuiz().getHostID() == context.getUser().getUserID()) {
			ClientEndQuizEvent cEQE = new ClientEndQuizEvent();
			publishEvent(cEQE);
		}
			
		context.setQuestion(null);
		context.setTeam(null);
		context.setQuiz(null);

		main.showJoinQuizScene();
	}

	// Inner class
	public class ScoreboardDataHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerScoreboardDataEvent sSDE = (ServerScoreboardDataEvent) event;

			ArrayList<ScoreboardTeam> scoreboardTeams = sSDE.getScoreboardTeams();

			scoreboardModel.addScoreboardTeams(scoreboardTeams);
			
			MainContext context = MainContext.getContext();
			if (context.getQuiz().getHostID() == context.getUser().getUserID())
				scoreboardModel.updateWinnerLoser("HOST");
			else {
				if (scoreboardTeams.size() > 0) {
					int curTeamID = context.getTeam().getTeamID();
					ScoreboardTeam curTeam = null;
					for (ScoreboardTeam team : scoreboardTeams) {
						if (team.getTeamID() == curTeamID) {
							curTeam = team;
							break;
						}
					}
	
	
					if (curTeam != null) {
						if (scoreboardTeams.get(0).getTeamID() == curTeamID) {
							if(scoreboardTeams.get(1).getScore() == curTeam.getScore())
								scoreboardModel.updateWinnerLoser(curTeam.getTeamname() + ": TIED");
							else
								scoreboardModel.updateWinnerLoser(curTeam.getTeamname() + ": WINNER WINNER CHICKEN DINNER");
						}
						else {
							if(scoreboardTeams.get(0).getScore() == curTeam.getScore())
								scoreboardModel.updateWinnerLoser(curTeam.getTeamname() + ": TIED");
							else scoreboardModel.updateWinnerLoser(curTeam.getTeamname() + ": LOSER");
						}
					}
				}
			}
		}

	}

}
