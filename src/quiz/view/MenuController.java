package quiz.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.MainContext;
public class MenuController {

	// Methods
	@FXML
	private void initialize() {
		// Empty initialize
	}

	/**
	 * Opens a profile dialog.
	 */
	@FXML
	private void handleProfile() {
		MainContext context = MainContext.getContext();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText("Profile");
				if (context.getUser() == null)
					alert.setContentText("No user is logged in at the moment.");
				else {
					String message = "Username: " + context.getUser().getUsername() + "\nLevel: "
							+ Integer.toString(context.getUser().getLevel()) + "\nXP: "
							+ Long.toString(context.getUser().getXp());
					if (context.getQuiz() != null) {
						message += "\nQuiz: " + context.getQuiz().getQuizname();
						if (context.getTeam() != null)
							message += "\nTeam: " + context.getTeam().getTeamname();
					}
					alert.setContentText(message);
				}

				alert.showAndWait();
			}
		});
	}

	/**
	 * Opens an about dialog.
	 */
	@FXML
	private void handleAbout() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText("About");
				alert.setContentText("Authors:\nArthur Crapé\nHannes Demuynck\nEmiel Huyge\nGarben Tanghe");
				alert.showAndWait();
			}
		});
	}

}
