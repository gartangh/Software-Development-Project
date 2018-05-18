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
				String content = "";
				
				if (context.getUser() == null)
					content += "No user is logged in at the moment.";
				else {
					content += "Username: " + context.getUser().getUsername() + "\nLevel: "
							+ Integer.toString(context.getUser().getLevel()) + "\nXP: "
							+ Long.toString(context.getUser().getXp());
					if (context.getQuiz() != null) {
						content += "\nQuiz: " + context.getQuiz().getQuizname();
						if (context.getQuiz().getHostID() == context.getUser().getUserID())
							content += " [Host]";
						if (context.getTeam() != null) {
							content += "\nTeam: " + context.getTeam().getTeamname();
							if (context.getTeam().getCaptainID() == context.getUser().getUserID())
								content += " [Captain]";
						}
					}
				}
				alert.setContentText(content);
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
	
	
	/**
	 * Opens Rules dialog.
	 */
	@FXML
	private void handleRules() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText("Rules");
				alert.setContentText("The rules of the quiz:\nRESPECT each other.\nENJOY!");
				alert.showAndWait();
			}
		});
	}
}
