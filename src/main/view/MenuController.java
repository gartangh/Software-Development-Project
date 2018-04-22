package main.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.Context;
import main.Main;
import user.model.User;

public class MenuController {

	@FXML
	private void initialize() {
		// Empty initialize
	}

	/**
	 * Opens an profile dialog.
	 */
	@FXML
	private void handleProfile() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(Main.QUIZNAME);
		alert.setHeaderText("Profile");
		if (Context.getContext().getUser() == null)
			alert.setContentText("No user is logged in.");
		else {
			User user = Context.getContext().getUser();
			alert.setContentText("Username: " + user.getUsername() + "\nLevel: " + Integer.toString(user.getLevel())
					+ "\nXP: " + Long.toString(user.getXp()));
		}

		alert.showAndWait();
	}

	/**
	 * Opens an about dialog.
	 */
	@FXML
	private void handleAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(Main.QUIZNAME);
		alert.setHeaderText("About");
		alert.setContentText("Authors:\nArthur Crapé\nHannes Demuynck\nEmiel Huyge\nGarben Tanghe");

		alert.showAndWait();
	}

}
