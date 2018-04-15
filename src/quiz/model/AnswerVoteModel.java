package quiz.model;

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

	// Question properties
	private StringProperty questionTitelProperty, questionTextProperty;
	// Answer properties
	private StringProperty answerPropertyA, answerPropertyB, answerPropertyC, answerPropertyD;
	private ObjectProperty<Paint> paintPropertyA, paintPropertyB, paintPropertyC, paintPropertyD;
	// Vote properties
	private DoubleProperty progressPropertyA, progressPropertyB, progressPropertyC, progressPropertyD;
	private StringProperty percentagePropertyA, percentagePropertyB, percentagePropertyC, percentagePropertyD;
	private StringProperty numberOfVotesProperty;
	// Button properties
	private BooleanProperty voteVisibilityProperty, confirmVisibilityProperty;

	// Constructor
	public AnswerVoteModel() {
		questionTitelProperty = new SimpleStringProperty("Question:");
		questionTextProperty = new SimpleStringProperty("");

		// Answer properties
		answerPropertyA = new SimpleStringProperty("");
		answerPropertyB = new SimpleStringProperty("");
		answerPropertyC = new SimpleStringProperty("");
		answerPropertyD = new SimpleStringProperty("");
		paintPropertyA = new SimpleObjectProperty<Paint>(Color.BLACK);
		paintPropertyB = new SimpleObjectProperty<Paint>(Color.BLACK);
		paintPropertyC = new SimpleObjectProperty<Paint>(Color.BLACK);
		paintPropertyD = new SimpleObjectProperty<Paint>(Color.BLACK);

		// Vote properties
		progressPropertyA = new SimpleDoubleProperty(0.0);
		progressPropertyB = new SimpleDoubleProperty(0.0);
		progressPropertyC = new SimpleDoubleProperty(0.0);
		progressPropertyD = new SimpleDoubleProperty(0.0);
		percentagePropertyA = new SimpleStringProperty("0 %");
		percentagePropertyB = new SimpleStringProperty("0 %");
		percentagePropertyC = new SimpleStringProperty("0 %");
		percentagePropertyD = new SimpleStringProperty("0 %");
		numberOfVotesProperty = new SimpleStringProperty("0 votes");

		// Button properties
		voteVisibilityProperty = new SimpleBooleanProperty(true);
		confirmVisibilityProperty = new SimpleBooleanProperty(true);
	}

	// Getters
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

	public StringProperty getQuestionTitelProperty() {
		return questionTitelProperty;
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

	// Methods
	/*
	 * QuestionFormController calls this every SERVER_VOTE event
	 */
	public void updateVotes(String teamname) {
		Map<String, Map<String, Integer>> allVotes = Context.getContext().getQuiz().getVotes();
		if (allVotes != null) {
			Map<String, Integer> teamVotes = allVotes.get(teamname);
			if (teamVotes != null) {
				int total = teamVotes.size();
				int[] votes = new int[4];
				for (int vote : teamVotes.values()) {
					votes[vote]++;
				}
				if (total > 0) {
					int A = Math.round(100 * (votes[0] / total));
					int B = Math.round(100 * (votes[1] / total));
					int C = Math.round(100 * (votes[2] / total));
					int D = Math.round(100 * (votes[3] / total));

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							progressPropertyA.setValue((double) A / 100);
							progressPropertyB.setValue((double) B / 100);
							progressPropertyC.setValue((double) C / 100);
							progressPropertyD.setValue((double) D / 100);
							percentagePropertyA.setValue(A + " %");
							percentagePropertyB.setValue(B + " %");
							percentagePropertyC.setValue(C + " %");
							percentagePropertyD.setValue(D + " %");
							if (total == 1)
								numberOfVotesProperty.setValue(total + " vote");
							else
								numberOfVotesProperty.setValue(total + " votes");
						}
					});
				}
			}
		}
	}

	public void updateAnswer(int answer, int correctAnswer) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (answer != correctAnswer) {
					switch (answer) {
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
				switch (correctAnswer) {
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

}
