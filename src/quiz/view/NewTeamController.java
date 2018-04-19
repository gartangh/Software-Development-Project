package quiz.view;

import eventbroker.clientevent.ClientNewTeamEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import quiz.model.Team;

public class NewTeamController {
	@FXML
    private TextField newTeamname;
    @FXML
    private ColorPicker colorpicker;

    private Team team;
    private ClientNewTeamEvent teamevent;


    private Stage dialogStage;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    public boolean isOkClicked() {
        return okClicked;
    }

    public void setTeam(Team team){
    	this.team=team;
    }

    public void setTeamEvent(ClientNewTeamEvent teamevent){
    	this.teamevent=teamevent;
    }

    /*@FXML
    private void handleOk() {
        if (isInputValid()) {
            team.setName(newTeamname.getText());
            team.setColor(colorpicker.getValue());

            okClicked = true;
            dialogStage.close();
        }
    }*/

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            teamevent.setTeamName(newTeamname.getText());
            teamevent.setColor(colorpicker.getValue());

            okClicked = true;
            dialogStage.close();
        }
    }


    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (newTeamname.getText() == null || newTeamname.getText().length() == 0) {
            errorMessage += "No valid teamname!\n";
        }
        if (colorpicker.getValue()== null ) {
            errorMessage += "No color picked!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
