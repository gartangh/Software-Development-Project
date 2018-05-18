package quiz.view;

import eventbroker.clientevent.ClientCreateTeamEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
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
		String errorMessage = "";

		if (mTeamname.getText() == null || !mTeamname.getText().matches(Team.TEAMNAMEREGEX))
			errorMessage += "No valid teamname.\n";

		if (mColor.getValue() == null)
			errorMessage += "No color picked.\n";

		if (errorMessage.length() == 0)
			return true;
		else {
			// Show the error message.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(dialogStage);
			alert.setTitle("Waring");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);
			alert.showAndWait();

			return false;
		}
	}

}
