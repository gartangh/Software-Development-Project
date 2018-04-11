package quiz.model;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VoteModel {
	DoubleProperty progressPropertyA, progressPropertyB, progressPropertyC, progressPropertyD;
	StringProperty percentagePropertyA, percentagePropertyB, percentagePropertyC, percentagePropertyD, numberOfVotesProperty;
	
	public VoteModel() {
		progressPropertyA = new SimpleDoubleProperty(0.0);
		progressPropertyB = new SimpleDoubleProperty(0.0);
		progressPropertyC = new SimpleDoubleProperty(0.0);
		progressPropertyD = new SimpleDoubleProperty(0.0);
		percentagePropertyA = new SimpleStringProperty("0%");
		percentagePropertyB = new SimpleStringProperty("0%");
		percentagePropertyC = new SimpleStringProperty("0%");
		percentagePropertyD = new SimpleStringProperty("0%");
		numberOfVotesProperty = new SimpleStringProperty("0 votes");
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

	public void updateModel(Quiz quiz, int teamID) {
		Map<Integer, Map<Integer, Integer>> allVotes = quiz.getVotes();
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
				}
			}
		}
	}
}
