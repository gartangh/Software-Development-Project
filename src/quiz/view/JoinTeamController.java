package quiz.view;

import java.util.Map.Entry;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.Context;
import main.Main;
import quiz.model.Team;
import user.model.Player;

public class JoinTeamController {

	@FXML
	private VBox mTeams;
	@FXML
	private Button mBack;

	private ObservableList<Team> teams;

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}

	@FXML
	private void initialize() {
		teams = Context.getContext().getQuiz().getTeams();

		for (Team team : teams) {
			GridPane mTeam = new GridPane();

			// Teamname label
			Label teamnameLabel = new Label("Team name");
			GridPane.setConstraints(teamnameLabel, 0, 0);
			// Teamname output / join button
			Button mTeamname = new Button(team.getTeamname());
			// Lambda expression
			mTeamname.setOnAction(e -> {
				handleJoin(team.getTeamname());
			});
			GridPane.setConstraints(mTeamname, 1, 0);

			// Color label
			Label colorLabel = new Label("Color");
			GridPane.setConstraints(colorLabel, 0, 1);
			// Color output
			TextField mColor = new TextField(team.getColor().toString());
			GridPane.setConstraints(mColor, 1, 1);

			int i = 0;
			for (Entry<Integer, String> player : team.getPlayers().entrySet()) {
				// Username label
				Label usernameLabel = new Label("Player" + i++);
				GridPane.setConstraints(usernameLabel, 0, i + 1);
				// Username output
				TextField mUsername = new TextField(player.getValue());
				GridPane.setConstraints(mUsername, 1, i + 1);

				mTeam.getChildren().addAll(usernameLabel, mUsername);
			}

			// Add all constraints to mQuiz
			mTeam.getChildren().addAll(teamnameLabel, mTeamname, colorLabel, mColor);

			mTeams.getChildren().add(mTeam);
		}
	}

	@FXML
	private void handleJoin(String teamname) {
		// TODO: Handle join
		// Context.getContext().setTeam();
		// main.show..Scene();
	}

	@FXML
	private void handleBack() {
		// TODO: Handle back
		Context.getContext().setQuiz(null);
		main.showJoinQuizScene();
	}

}
