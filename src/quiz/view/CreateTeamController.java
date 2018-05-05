package quiz.view;

import eventbroker.clientevent.ClientCreateTeamEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import quiz.model.Team;

public class CreateTeamController {
	
	@FXML
	private TextField mTeamname;
	@FXML
	private ColorPicker mColor;

	private ClientCreateTeamEvent cNTE;
	private Stage dialogStage;
	private boolean okClicked = false;

	// Getters and setters
	public void setTeamEvent(ClientCreateTeamEvent cNTE) {
		this.cNTE = cNTE;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	// Methods
	@FXML
	private void initialize() {
		// Empty initialize
	}

	@FXML
	private void handleCreateTeam() {
		if (isInputValid()) {
			cNTE.setTeamName(mTeamname.getText());
			cNTE.setColor(mColor.getValue());

			okClicked = true;
			dialogStage.close();
		}
	}

	@FXML
	private void handleBack() {
		dialogStage.close();
	}

	private boolean isInputValid() {
		String teamname = mTeamname.getText();
		if (teamname == null || !teamname.matches(Team.TEAMNAMEREGEX)) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("Teamname is invalid!");
			alert.setContentText("Try again with a valid quizname.");
			alert.showAndWait();

			return false;
		}

		Color color = mColor.getValue();
		if (color == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText("No color picked!");
			alert.setContentText("Select a color and try again.");
			alert.showAndWait();

			return false;
		}

		// Everything is valid
		return true;
	}

}
