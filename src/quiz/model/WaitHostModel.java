package quiz.model;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.MainContext;

public class WaitHostModel {
	
	private ObservableList<TeamNameID> teams = FXCollections.observableArrayList();
	private StringProperty teamname;
	private StringProperty answer;
	
	// Question properties
	private StringProperty questionTitleProperty;
	private StringProperty questionTextProperty;
		
	// Answer properties
	private StringProperty answerPropertyA, answerPropertyB, answerPropertyC, answerPropertyD, correctAnswerProperty;
		
	public WaitHostModel() {
		// Question properties
		questionTitleProperty = new SimpleStringProperty("Question");
		questionTextProperty = new SimpleStringProperty();
		
		// Answer properties
		answerPropertyA = new SimpleStringProperty();
		answerPropertyB = new SimpleStringProperty();
		answerPropertyC = new SimpleStringProperty();
		answerPropertyD = new SimpleStringProperty();
		correctAnswerProperty = new SimpleStringProperty();
	}
	
	public void updateQuestion() {
		MCQuestion q = (MCQuestion) MainContext.getContext().getQuestion();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				questionTextProperty.setValue(q.getQuestion());
				answerPropertyA.setValue(q.getAnswers()[0]);
				answerPropertyB.setValue(q.getAnswers()[1]);
				answerPropertyC.setValue(q.getAnswers()[2]);
				answerPropertyD.setValue(q.getAnswers()[3]);
				String correctAnswer = q.getAnswers()[q.getCorrectAnswer()];
				if(q.getCorrectAnswer() == 0) correctAnswer = correctAnswer.concat(" (A)");
				else if(q.getCorrectAnswer() == 1) correctAnswer = correctAnswer.concat(" (B)");
				else if(q.getCorrectAnswer() == 2) correctAnswer = correctAnswer.concat(" (C)");
				else if(q.getCorrectAnswer() == 3) correctAnswer = correctAnswer.concat(" (D)");
				correctAnswerProperty.setValue(correctAnswer);
			}
		});
	}
	
	public void addTeam(TeamNameID teamNameID) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				teams.add(teamNameID);
			}
		});
	}
	
	public void clearTeams() {
		teams.clear();
	}

	public ObservableList<TeamNameID> getTeams() {
		return teams;
	}

	public StringProperty getTeamname() {
		return teamname;
	}

	public StringProperty getAnswer() {
		return answer;
	}

	public StringProperty getQuestionTitleProperty() {
		return questionTitleProperty;
	}

	public StringProperty getQuestionTextProperty() {
		return questionTextProperty;
	}

	public StringProperty getAnswerPropertyA() {
		return answerPropertyA;
	}

	public StringProperty getAnswerPropertyB() {
		return answerPropertyB;
	}

	public StringProperty getAnswerPropertyC() {
		return answerPropertyC;
	}

	public StringProperty getAnswerPropertyD() {
		return answerPropertyD;
	}

	public StringProperty getCorrectAnswerProperty() {
		return correctAnswerProperty;
	}
	
}
