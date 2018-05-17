package quiz.model;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class JoinQuizModel {

	private ObservableList<QuizModel> quizzes = FXCollections.observableArrayList();
	private StringProperty quiznameProperty = new SimpleStringProperty();
	private StringProperty quizRoundsProperty = new SimpleStringProperty();
	private StringProperty teamProperty = new SimpleStringProperty();
	private StringProperty playersProperty = new SimpleStringProperty();
	private BooleanProperty joinDisableProperty = new SimpleBooleanProperty(true);

	// Getters
	public ObservableList<QuizModel> getQuizzes() {
		return quizzes;
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

	// Adder and remover
	public void addQuiz(QuizModel quiz) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				JoinQuizModel.this.quizzes.add(quiz);
			}
		});
	}

	public void addQuizzes(ArrayList<QuizModel> quizzes) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				JoinQuizModel.this.quizzes.addAll(quizzes);
			}
		});
	}

	public void removeQuiz(int quizID) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for (QuizModel quiz : quizzes)
					if (quiz.getQuizID() == quizID) {
						quizzes.remove(quiz);
						quiznameProperty.setValue("");
						quizRoundsProperty.setValue("");
						teamProperty.setValue("");
						playersProperty.setValue("");
						joinDisableProperty.setValue(true);
						break;
					}
			}
		});
	}

	// Method
	public void updateQuizDetail(String quizname, int rounds, int teams, int players) {
		// For the selected quiz
		Platform.runLater(new Runnable() {
			public void run() {
				quiznameProperty.setValue(quizname);
				quizRoundsProperty.setValue(Integer.toString(rounds));
				teamProperty.setValue(Integer.toString(teams));
				playersProperty.setValue(Integer.toString(players));
				joinDisableProperty.setValue(false);
			}
		});
	}

}
