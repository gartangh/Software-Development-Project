package quiz.model;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.Context;

public class AnswerVoteModel {
	private StringProperty questionTitleProperty, questionTextProperty;	// Question properties
	
	private StringProperty answerPropertyA, answerPropertyB, answerPropertyC, answerPropertyD;	// Answer properties
	private ObjectProperty<Paint> paintPropertyA, paintPropertyB, paintPropertyC, paintPropertyD;
	
	private DoubleProperty progressPropertyA, progressPropertyB, progressPropertyC, progressPropertyD;	// Vote properties
	private StringProperty percentagePropertyA, percentagePropertyB, percentagePropertyC, percentagePropertyD;
	private StringProperty numberOfVotesProperty;
	
	private BooleanProperty voteVisibilityProperty, confirmVisibilityProperty; // Button properties
	
	public AnswerVoteModel() {
		questionTitleProperty = new SimpleStringProperty("Question:");
		questionTextProperty = new SimpleStringProperty("");
		
		answerPropertyA = new SimpleStringProperty("");	// Answer properties
		answerPropertyB = new SimpleStringProperty("");
		answerPropertyC = new SimpleStringProperty("");
		answerPropertyD = new SimpleStringProperty("");
		paintPropertyA = new SimpleObjectProperty<Paint>(Color.BLACK);
		paintPropertyB = new SimpleObjectProperty<Paint>(Color.BLACK);
		paintPropertyC = new SimpleObjectProperty<Paint>(Color.BLACK);
		paintPropertyD = new SimpleObjectProperty<Paint>(Color.BLACK);
		
		progressPropertyA = new SimpleDoubleProperty(0.0);	// Vote properties
		progressPropertyB = new SimpleDoubleProperty(0.0);
		progressPropertyC = new SimpleDoubleProperty(0.0);
		progressPropertyD = new SimpleDoubleProperty(0.0);
		percentagePropertyA = new SimpleStringProperty("0%");
		percentagePropertyB = new SimpleStringProperty("0%");
		percentagePropertyC = new SimpleStringProperty("0%");
		percentagePropertyD = new SimpleStringProperty("0%");
		numberOfVotesProperty = new SimpleStringProperty("0 votes");
		
		voteVisibilityProperty = new SimpleBooleanProperty(true);	// Button properties
		confirmVisibilityProperty = new SimpleBooleanProperty(true);
	}

	public void updateVotes(int teamID) {
		Map<Integer, Map<Integer, Integer>> allVotes = Context.getContext().getQuiz().getVotes();
		if(allVotes != null) {
			Map<Integer, Integer> teamVotes = allVotes.get(teamID);
			if(teamVotes != null) {
				int total = teamVotes.size();
				int [] votes = new int[4];
				for(int vote : teamVotes.values()) {
					votes[vote]++;
				}
				if(total>0) {
					int A = (int) Math.round(100*(votes[0]/total));
					int B = (int) Math.round(100*(votes[1]/total));
					int C = (int) Math.round(100*(votes[2]/total));
					int D = (int) Math.round(100*(votes[3]/total));
									
					Platform.runLater(new Runnable() {
						public void run() {
							progressPropertyA.setValue((double) A/100);
							progressPropertyB.setValue((double) B/100);
							progressPropertyC.setValue((double) C/100);
							progressPropertyD.setValue((double) D/100);
							percentagePropertyA.setValue(A+"%");
							percentagePropertyB.setValue(B+"%");
							percentagePropertyC.setValue(C+"%");
							percentagePropertyD.setValue(D+"%");
							if(total == 1) numberOfVotesProperty.setValue(total+" vote");
							else numberOfVotesProperty.setValue(total+" votes");
						}
					});
					
					return;
				}
			}
		}
		Platform.runLater(new Runnable() {
			public void run() {
				progressPropertyA.setValue(0.0);
				progressPropertyB.setValue(0.0);
				progressPropertyC.setValue(0.0);
				progressPropertyD.setValue(0.0);
				percentagePropertyA.setValue("0%");
				percentagePropertyB.setValue("0%");
				percentagePropertyC.setValue("0%");
				percentagePropertyD.setValue("0%");
				numberOfVotesProperty.setValue("0 votes");
			}
		});
	}
	
	public void updateAnswer(int answer, int correctAnswer) {
		Platform.runLater(new Runnable() {
			public void run() {
				if(answer != correctAnswer) {
					switch(answer) {
					case 0:
						paintPropertyA.setValue(Color.RED);
						break;
					case 1:
						paintPropertyB.setValue(Color.RED);
						break;
					case 2:
						paintPropertyC.setValue(Color.RED);
						break;
					case 3:
						paintPropertyD.setValue(Color.RED);
						break;
					}
				}
				switch(correctAnswer) {
				case 0:
					paintPropertyA.setValue(Color.GREEN);
					break;
				case 1:
					paintPropertyB.setValue(Color.GREEN);
					break;
				case 2:
					paintPropertyC.setValue(Color.GREEN);
					break;
				case 3:
					paintPropertyD.setValue(Color.GREEN);
					break;
				}
				voteVisibilityProperty.setValue(false);
				confirmVisibilityProperty.setValue(false);
			}
		});
	}
	
	public void updateQuestion() {
		MCQuestion q = (MCQuestion) Context.getContext().getQuestion();
		System.out.println(q.getQuestion());
		Platform.runLater(new Runnable() {
			public void run() {
				questionTextProperty.setValue(q.getQuestion());
				
				answerPropertyA.setValue(q.getAnswers()[0]);
				answerPropertyB.setValue(q.getAnswers()[1]);
				answerPropertyC.setValue(q.getAnswers()[2]);
				answerPropertyD.setValue(q.getAnswers()[3]);
				paintPropertyA.setValue(Color.BLACK);
				paintPropertyB.setValue(Color.BLACK);
				paintPropertyC.setValue(Color.BLACK);
				paintPropertyD.setValue(Color.BLACK);
				
				voteVisibilityProperty.setValue(true);
				confirmVisibilityProperty.setValue(true);
			}
		});
	}
	
	public StringProperty getNumberOfVotesProperty() {
		return numberOfVotesProperty;
	}

	public DoubleProperty getProgressPropertyA() {
		return progressPropertyA;
	}

	public DoubleProperty getProgressPropertyB() {
		return progressPropertyB;
	}

	public DoubleProperty getProgressPropertyC() {
		return progressPropertyC;
	}

	public DoubleProperty getProgressPropertyD() {
		return progressPropertyD;
	}

	public StringProperty getPercentagePropertyA() {
		return percentagePropertyA;
	}

	public StringProperty getPercentagePropertyB() {
		return percentagePropertyB;
	}

	public StringProperty getPercentagePropertyC() {
		return percentagePropertyC;
	}

	public StringProperty getPercentagePropertyD() {
		return percentagePropertyD;
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

	public ObjectProperty<Paint> getPaintPropertyA() {
		return paintPropertyA;
	}

	public ObjectProperty<Paint> getPaintPropertyB() {
		return paintPropertyB;
	}

	public ObjectProperty<Paint> getPaintPropertyC() {
		return paintPropertyC;
	}

	public ObjectProperty<Paint> getPaintPropertyD() {
		return paintPropertyD;
	}

	public BooleanProperty getVoteVisibilityProperty() {
		return voteVisibilityProperty;
	}

	public BooleanProperty getConfirmVisibilityProperty() {
		return confirmVisibilityProperty;
	}
	
}
