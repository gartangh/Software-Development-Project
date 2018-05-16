package eventbroker;

import eventbroker.serverevent.ServerPlayerLeavesQuizEvent;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.Main;
import main.MainContext;
import quiz.model.Quiz;
import quiz.model.Team;
import quiz.view.JoinTeamController;

public class PlayerLeavesQuizHandler implements EventListener {

	private Main main;
	private JoinTeamController joinTeamController;

	public PlayerLeavesQuizHandler(){
	}


	public void setMain(Main main) {
		this.main = main;
	}

	public void setJoinTeamController(JoinTeamController joinTeamController) {
		this.joinTeamController = joinTeamController;
	}

	@Override
	public void handleEvent(Event event) {
		ServerPlayerLeavesQuizEvent sPLQE = (ServerPlayerLeavesQuizEvent) event;
		MainContext context = MainContext.getContext();
		boolean enoughTeamsLeft=true;
		if (context.getQuiz().getQuizID() == sPLQE.getQuizID()) {
			if (sPLQE.getTeamID() != -1) {
				if (sPLQE.getNewCaptainID() != -1) {// to be sure
					Team team = context.getQuiz().getTeamMap().get(sPLQE.getTeamID());
					int oldCaptainID = team.getCaptainID();

					team.setCaptainID(sPLQE.getNewCaptainID());// the captain can change or the captain can be the											// same
					team.removePlayer(sPLQE.getUserID());

					if (team.getPlayerMap().size() == 0) {
						context.getQuiz().removeTeam(team.getTeamID());
						if (sPLQE.isRunning() && context.getQuiz().getTeamMap().size()<Quiz.MINTEAMS){
							enoughTeamsLeft=false;
						}
					} else if (team.getCaptainID() == context.getUser().getUserID()
							&& team.getCaptainID() != oldCaptainID) {
						Platform.runLater(new Runnable() {
							public void run() {
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.initOwner(main.getPrimaryStage());
								alert.setTitle("Captain left quiz");
								alert.setHeaderText(null);
								alert.setContentText("Your captain left the quiz, you are the captain now");
								alert.showAndWait();
							}
						});
					}
				}
			} else
				context.getQuiz().removeUnassignedPlayer(sPLQE.getUserID());

			if (context.getUser().getUserID() == sPLQE.getUserID() || !enoughTeamsLeft) {
				context.setQuiz(null);
				context.setTeam(null);
				EventBroker.getEventBroker().removeEventListeners();

				if (!enoughTeamsLeft){
					Platform.runLater(new Runnable() {
						public void run() {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.initOwner(main.getPrimaryStage());
							alert.setTitle("Quiz ended");
							alert.setHeaderText("There are not enough teams left to continue.");
							alert.setContentText("Please select another quiz if you want to continue.");
							alert.showAndWait();
							main.showJoinQuizScene();
						}
					});
				}
				else {
					Platform.runLater(new Runnable() {
						public void run() {
							main.showJoinQuizScene();
						}
					});
				}


			}else if (!sPLQE.isRunning()){
				joinTeamController.updateViews();
			}

		}

	}
}
