package quiz.view;

import java.util.Set;
import java.util.Map.Entry;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import quiz.model.Quiz;

public class JoinQuizModel {
	
	private ObservableList<Quiz> quizzes;
	private StringProperty quiznameProperty;
	private StringProperty quizRoundsProperty;
	private StringProperty questionsPerRoundProperty;
	private StringProperty teamProperty;
	private StringProperty playersPerTeamProperty;
	
	public JoinQuizModel() {
		quizzes = FXCollections.observableArrayList();
		quiznameProperty = new SimpleStringProperty("");
		quizRoundsProperty = new SimpleStringProperty();
		questionsPerRoundProperty = new SimpleStringProperty();
		teamProperty = new SimpleStringProperty();
		playersPerTeamProperty = new SimpleStringProperty();
	}
	
	public void updateQuizDetail(Quiz quiz){//for the selected team

		Platform.runLater(new Runnable(){
			public void run(){
				quiznameProperty.setValue(quiz.getQuizname());
				quizRoundsProperty.setValue(Integer.toString(quiz.getAmountOfRounds()));
				questionsPerRoundProperty.setValue(Integer.toString(quiz.getMaxAmountofQuestionsPerRound()));
				teamProperty.setValue(Integer.toString(quiz.getAmountOfTeams()));
				playersPerTeamProperty.setValue(Integer.toString(quiz.getMaxAmountOfPlayersPerTeam()));
			}
		});
	}
	
	public ObservableList<Quiz> getQuizzes() {
		return quizzes;
	}

	public StringProperty getQuiznameProperty() {
		return quiznameProperty;
	}

	public StringProperty getQuizRoundsProperty() {
		return quizRoundsProperty;
	}

	public StringProperty getQuestionsPerRoundProperty() {
		return questionsPerRoundProperty;
	}

	public StringProperty getTeamProperty() {
		return teamProperty;
	}
	
	public StringProperty getPlayersPerTeamProperty() {
		return playersPerTeamProperty;
	}

}
