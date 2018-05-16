package quiz.model;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import main.MainContext;
import quiz.util.RoundType;

public class WaitHostModel {
	
	private ObservableList<TeamNameID> teams = FXCollections.observableArrayList();
	private StringProperty teamname;
	private StringProperty answer;
	
	// Question properties
	private StringProperty questionTitleProperty;
	private StringProperty questionTextProperty;
	private ObjectProperty<Image> imageProperty;
		
	// Answer properties
	private StringProperty answerPropertyA, answerPropertyB, answerPropertyC, answerPropertyD, correctAnswerProperty, pointsProperty;
	private RoundType roundType;
		
	public WaitHostModel() {
		// Question properties
		questionTitleProperty = new SimpleStringProperty("Question");
		questionTextProperty = new SimpleStringProperty();
		imageProperty = new SimpleObjectProperty<Image>();

		
		// Answer properties
		answerPropertyA = new SimpleStringProperty();
		answerPropertyB = new SimpleStringProperty();
		answerPropertyC = new SimpleStringProperty();
		answerPropertyD = new SimpleStringProperty();
		correctAnswerProperty = new SimpleStringProperty();
		pointsProperty = new SimpleStringProperty();
	}
	
	public void updateQuestion() {
		if(MainContext.getContext().getQuestion() != null) {
			switch(this.roundType) {
			case IP:
				IPQuestion ipQ = (IPQuestion) MainContext.getContext().getQuestion();
				Platform.runLater(new Runnable() {
					public void run() {
						imageProperty.setValue(ipQ.getFullFXImage());

						answerPropertyA.setValue(ipQ.getAnswers()[0]);
						answerPropertyB.setValue(ipQ.getAnswers()[1]);
						answerPropertyC.setValue(ipQ.getAnswers()[2]);
						answerPropertyD.setValue(ipQ.getAnswers()[3]);
						
						String correctAnswer = ipQ.getAnswers()[ipQ.getCorrectAnswer()];
						if(ipQ.getCorrectAnswer() == 0) correctAnswer = correctAnswer.concat(" (A)");
						else if(ipQ.getCorrectAnswer() == 1) correctAnswer = correctAnswer.concat(" (B)");
						else if(ipQ.getCorrectAnswer() == 2) correctAnswer = correctAnswer.concat(" (C)");
						else if(ipQ.getCorrectAnswer() == 3) correctAnswer = correctAnswer.concat(" (D)");
						correctAnswerProperty.setValue(correctAnswer);
					}
				});			
				break;
			case MC:
				MCQuestion mcQ = (MCQuestion) MainContext.getContext().getQuestion();
				Platform.runLater(new Runnable() {
					public void run() {
						questionTextProperty.setValue(mcQ.getQuestion());

						answerPropertyA.setValue(mcQ.getAnswers()[0]);
						answerPropertyB.setValue(mcQ.getAnswers()[1]);
						answerPropertyC.setValue(mcQ.getAnswers()[2]);
						answerPropertyD.setValue(mcQ.getAnswers()[3]);
						
						String correctAnswer = mcQ.getAnswers()[mcQ.getCorrectAnswer()];
						if(mcQ.getCorrectAnswer() == 0) correctAnswer = correctAnswer.concat(" (A)");
						else if(mcQ.getCorrectAnswer() == 1) correctAnswer = correctAnswer.concat(" (B)");
						else if(mcQ.getCorrectAnswer() == 2) correctAnswer = correctAnswer.concat(" (C)");
						else if(mcQ.getCorrectAnswer() == 3) correctAnswer = correctAnswer.concat(" (D)");
						correctAnswerProperty.setValue(correctAnswer);
					}
				});
				break;
			}
		}
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
	
	public ObjectProperty<Image> getImageProperty() {
		return imageProperty;
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
	
	public RoundType getRoundType() {
		return this.roundType;
	}
	
	public void setRoundType(RoundType roundType) {
		this.roundType = roundType;
	}
	
}
