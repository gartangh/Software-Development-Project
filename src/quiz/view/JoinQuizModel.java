package quiz.view;

import java.util.Set;
import java.util.Map.Entry;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import quiz.model.Quiz;

public class JoinQuizModel {
	
	private ObservableList<Quiz> quizzes = FXCollections.observableArrayList();
	private StringProperty quiznameProperty;
	private StringProperty quizRoundsProperty;
	private StringProperty questionsPerRoundProperty;
	private StringProperty teamProperty;
	private StringProperty playersPerTeamProperty;
	
	private BooleanProperty joinDisableProperty;
	
	public JoinQuizModel() {
		//quizzes = FXCollections.observableArrayList();
		quiznameProperty = new SimpleStringProperty("");
		quizRoundsProperty = new SimpleStringProperty();
		questionsPerRoundProperty = new SimpleStringProperty();
		teamProperty = new SimpleStringProperty();
		playersPerTeamProperty = new SimpleStringProperty();
		
		joinDisableProperty = new SimpleBooleanProperty(true);
	}
	
	public void updateQuizDetail(Quiz quiz){//for the selected team

		Platform.runLater(new Runnable(){
			public void run(){
				quiznameProperty.setValue(quiz.getQuizname());
				quizRoundsProperty.setValue(Integer.toString(quiz.getAmountOfRounds()));
				questionsPerRoundProperty.setValue(Integer.toString(quiz.getMaxAmountofQuestionsPerRound()));
				teamProperty.setValue(Integer.toString(quiz.getAmountOfTeams()));
				playersPerTeamProperty.setValue(Integer.toString(quiz.getMaxAmountOfPlayersPerTeam()));
				joinDisableProperty.setValue(false);
			}
		});
	}
	
	public ObservableList<Quiz> getQuizzes() {
		return quizzes;
	}

	public void setQuizzes(ObservableList<Quiz> quizzes) {
		this.quizzes = quizzes;
	}

	public void addQuiz(Quiz quiz) {
		quizzes.add(quiz);
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

	public BooleanProperty getJoinDisableProperty() {
		return joinDisableProperty;
	}

}
