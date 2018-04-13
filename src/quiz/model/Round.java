package quiz.model;

import java.util.HashMap;
import java.util.Map;

import quiz.util.Difficulty;
import quiz.util.Theme;
import quiz.util.Type;

public class Round {

	private Type type;
	private Difficulty difficulty;
	private Theme theme;
	private Map<Integer, Map<Integer, Integer>> answers; // Map(questionID -> Map(teamID -> answerID))	

	public Round(Type type, Difficulty difficulty, Theme theme) {
		this.type = type;
		this.difficulty = difficulty;
		this.theme = theme;
	}

 	// TODO: Add getQuestions()

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public Theme getTheme() {
		return theme;
	}

	// Methods
	/*public void makeQuestions() {
		Question question;
		for (amountOfQuestions = 0; amountOfQuestions < maxAmountOfQuestions; amountOfQuestions++) {
			switch (type) {
			case MC:
				question = new MCQuestion(difficulty, theme);
				break;
			case IP:
				question = new IPQuestion(difficulty, theme);
				break;

			default:
				// TODO: ERROR! No such type
				break;
			}

			// TODO: Add question to questions
		}
	}*/
	
	public void addAnswer(int teamID, int questionID, int answer) {
		Map<Integer, Integer> questionAnswers = answers.get(questionID);
		if(questionAnswers == null) {
			questionAnswers = new HashMap<Integer, Integer>();
		}
		questionAnswers.put(teamID, answer);
		answers.put(questionID, questionAnswers);
	}

}
