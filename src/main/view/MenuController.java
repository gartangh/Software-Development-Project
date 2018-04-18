package main.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MenuController {

	@FXML
	private void initialize() {
		// Empty initialize
	}

	/**
	 * Opens an about dialog.
	 */
	@FXML
	private void handleAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Quiz");
		alert.setHeaderText("About");
		alert.setContentText("Authors:\nArthur Crapé\nHannes Demuynck\nEmiel Huyge\nGarben Tanghe");

		alert.showAndWait();
	}

}
