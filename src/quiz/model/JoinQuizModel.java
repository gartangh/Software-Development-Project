package quiz.model;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class JoinQuizModel {

	private ObservableList<Quiz> quizzes = FXCollections.observableArrayList();
	private StringProperty quiznameProperty;
	private StringProperty quizRoundsProperty;
	private StringProperty teamProperty;
	private StringProperty playersProperty;
	private BooleanProperty joinDisableProperty;

	public JoinQuizModel() {
		quiznameProperty = new SimpleStringProperty();
		quizRoundsProperty = new SimpleStringProperty();
		teamProperty = new SimpleStringProperty();
		playersProperty = new SimpleStringProperty();
		joinDisableProperty = new SimpleBooleanProperty(true);
	}

	public void updateQuizDetail(Quiz quiz) {
		// For the selected team
		Platform.runLater(new Runnable() {
			public void run() {
				quiznameProperty.setValue(quiz.getQuizname());
				quizRoundsProperty.setValue(Integer.toString(quiz.getRounds()));
				teamProperty.setValue(Integer.toString(quiz.getTeams()));
				playersProperty.setValue(Integer.toString(quiz.getPlayers()));
				joinDisableProperty.setValue(false);
			}
		});
	}

	public ObservableList<Quiz> getQuizzes() {
		return quizzes;
	}

	public void addQuiz(Quiz quiz) {
		Platform.runLater(new Runnable() {
			public void run() {
				quizzes.add(quiz);
			}
		});
	}

	public void deleteQuiz(int quizID) {
		Platform.runLater(new Runnable() {
			public void run() {
				for (int i = 0; i < quizzes.size(); i++) {
					if (quizzes.get(i).getQuizID() == quizID) {
						quizzes.remove(i);
						break;
					}
				}
			}
		});
	}

	public StringProperty getQuiznameProperty() {
		return quiznameProperty;
	}

	public StringProperty getQuizRoundsProperty() {
		return quizRoundsProperty;
	}

	public StringProperty getTeamProperty() {
		return teamProperty;
	}

	public StringProperty getPlayersProperty() {
		return playersProperty;
	}

	public BooleanProperty getJoinDisableProperty() {
		return joinDisableProperty;
	}

}
