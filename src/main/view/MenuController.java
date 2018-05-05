package main.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.Context;
import quiz.model.User;

public class MenuController {

	// Methods
	@FXML
	private void initialize() {
		// Empty initialize
	}

	/**
	 * Opens an profile dialog.
	 */
	@FXML
	private void handleProfile() {
		User user = Context.getContext().getUser();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText("Profile");
		if (user == null)
			alert.setContentText("No user is logged in at the moment.");
		else
			alert.setContentText("Username: " + user.getUsername() + "\nLevel: " + Integer.toString(user.getLevel())
					+ "\nXP: " + Long.toString(user.getXp()));
		alert.showAndWait();
	}

	/**
	 * Opens an about dialog.
	 */
	@FXML
	private void handleAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText("About");
		alert.setContentText("Authors:\nArthur Crapé\nHannes Demuynck\nEmiel Huyge\nGarben Tanghe");
		alert.showAndWait();
	}

}
