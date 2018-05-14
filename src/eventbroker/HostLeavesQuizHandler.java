package eventbroker;

import eventbroker.serverevent.ServerHostLeavesQuizEvent;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.Main;
import main.MainContext;

public class HostLeavesQuizHandler implements EventListener {

	// Reference to the main application
	private Main main;

	public void setMain(Main main) {
		this.main = main;
	}

	@Override
	public void handleEvent(Event event) {
		ServerHostLeavesQuizEvent sHLQE = (ServerHostLeavesQuizEvent) event;

		MainContext context = MainContext.getContext();
		if (sHLQE.getQuizID() == context.getQuiz().getQuizID()) {
			final int quizHostID = context.getQuiz().getHostID();
			context.setQuiz(null);
			context.setTeam(null);

			EventBroker.getEventBroker().removeEventListeners();

			Platform.runLater(new Runnable() {
				public void run() {
					if (quizHostID != context.getUser().getUserID()) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(main.getPrimaryStage());
						alert.setTitle("Quiz ended!");
						alert.setHeaderText("The host ended the quiz.");
						alert.setContentText("You can join another quiz or create a new one.");
						alert.showAndWait();
					}

					main.showJoinQuizScene();
				}
			});

		}
	}
}
