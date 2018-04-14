package quiz.view;

import java.util.ArrayList;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import quiz.model.ScoreboardModel;
import quiz.model.ScoreboardTeam;

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
	
	private ScoreboardModel scoreboardModel;
	private ScoreboardEventHandler eventHandler;
	
	public ScoreboardController() {
		scoreboardModel = new ScoreboardModel();
	}
	
	@FXML
	private void initialize() {

		eventHandler = new ScoreboardEventHandler();
		EventBroker.getEventBroker().addEventListener(eventHandler);
		
		rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
		teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));
		scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
		scoreboardTable.setItems(getTeams());
		
		winnerLoser.textProperty().bind(scoreboardModel.getWinnerLoser());
		
	}
	
	private ObservableList<ScoreboardTeam> getTeams() {
		// TODO: Create Event and receive from Server, now only test-phase!
		
		ObservableList<ScoreboardTeam> teams = FXCollections.observableArrayList();
		teams.add(new ScoreboardTeam(1,"TEST1", 1, 250));
		teams.add(new ScoreboardTeam(5,"TEST2", 2, 25));
		teams.add(new ScoreboardTeam(4,"TEST3", 3, 30));
		teams.add(new ScoreboardTeam(3,"TEST4", 4, 125));
		teams.add(new ScoreboardTeam(2,"TEST5", 5, 200));
		
		return teams;
	}

	public class ScoreboardEventHandler implements EventListener {

		@Override
		public void handleEvent(Event e) {
			
		}

		@Override
		public void handleEvent(Event e, ArrayList<Integer> destinations) {
			
		}
		
	}
	
	public ScoreboardModel getScoreboardModel() {
		return scoreboardModel;
	}
}
