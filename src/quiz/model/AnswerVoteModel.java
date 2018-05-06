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
import main.MainContext;

public class AnswerVoteModel {
	// Question properties
	private StringProperty questionTitleProperty;
	private StringProperty questionTextProperty;

	// Answer properties
	private StringProperty answerPropertyA, answerPropertyB, answerPropertyC, answerPropertyD;
	private ObjectProperty<Paint> paintPropertyA, paintPropertyB, paintPropertyC, paintPropertyD;

	// Vote properties
	private DoubleProperty progressPropertyA, progressPropertyB, progressPropertyC, progressPropertyD;
	private StringProperty percentagePropertyA, percentagePropertyB, percentagePropertyC, percentagePropertyD;
	private StringProperty numberOfVotesProperty;

	// Button properties
	private BooleanProperty voteDisableProperty, confirmDisableProperty, nextDisableProperty;

	// Constructor
	public AnswerVoteModel() {
		// Question properties
		questionTitleProperty = new SimpleStringProperty("Question");
		questionTextProperty = new SimpleStringProperty();

		// Answer properties
		answerPropertyA = new SimpleStringProperty();
		answerPropertyB = new SimpleStringProperty();
		answerPropertyC = new SimpleStringProperty();
		answerPropertyD = new SimpleStringProperty();
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
		voteDisableProperty = new SimpleBooleanProperty(false);
		confirmDisableProperty = new SimpleBooleanProperty(false);
		nextDisableProperty = new SimpleBooleanProperty(true);
	}

	public void updateVotes(int teamID) {
		Map<Integer, Map<Integer, Integer>> allVotes = MainContext.getContext().getQuiz().getVotes();
		if (allVotes != null) {
			Map<Integer, Integer> teamVotes = allVotes.get(teamID);
			if (teamVotes != null) {
				int total = teamVotes.size();
				int[] votes = new int[4];
				for (int vote : teamVotes.values())
					votes[vote]++;

				if (total > 0) {
					double fA = (100.0 * votes[0]) / total;
					double fB = (100.0 * votes[1]) / total;
					double fC = (100.0 * votes[2]) / total;
					double fD = (100.0 * votes[3]) / total;

					long A = Math.round(fA);
					long B = Math.round(fB);
					long C = Math.round(fC);
					long D = Math.round(fD);

					Platform.runLater(new Runnable() {
						public void run() {
							progressPropertyA.setValue((double) A / 100);
							progressPropertyB.setValue((double) B / 100);
							progressPropertyC.setValue((double) C / 100);
							progressPropertyD.setValue((double) D / 100);
							percentagePropertyA.setValue(A + "%");
							percentagePropertyB.setValue(B + "%");
							percentagePropertyC.setValue(C + "%");
							percentagePropertyD.setValue(D + "%");
							if (total == 1)
								numberOfVotesProperty.setValue(total + " vote");
							else
								numberOfVotesProperty.setValue(total + " votes");
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
				else {
					// TODO Make xp depend on difficulty
					MainContext.getContext().getUser().addXp(10);
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

				voteDisableProperty.setValue(true);
				confirmDisableProperty.setValue(true);
				nextDisableProperty.setValue(false);
			}
		});
	}

	public void updateQuestion() {
		MCQuestion q = (MCQuestion) MainContext.getContext().getQuestion();
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

				voteDisableProperty.setValue(false);
				confirmDisableProperty.setValue(false);
				nextDisableProperty.setValue(true);
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

	public BooleanProperty getVoteDisableProperty() {
		return voteDisableProperty;
	}

	public BooleanProperty getConfirmDisableProperty() {
		return confirmDisableProperty;
	}

	public BooleanProperty getNextDisableProperty() {
		return nextDisableProperty;
	}

}
