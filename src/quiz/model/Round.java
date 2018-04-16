package quiz.model;

import java.util.HashMap;
import java.util.Map;

import quiz.util.Difficulty;
import quiz.util.Theme;
import quiz.util.RoundType;

public class Round {

	private int amountOfQuestions;
	private int maxAmountOfQuestions;
	// Map(questionID -> question)
	private Map<Integer, Question> questions;
	private RoundType roundType;
	private Difficulty difficulty;
	private Theme theme;
	// Map(questionID -> Map(teamID -> answerID))
	private Map<Integer, Map<Integer, Integer>> answers;

	public Round(int maxAmountOfQuestions, RoundType roundType, Difficulty difficulty, Theme theme) {
		this.maxAmountOfQuestions = maxAmountOfQuestions;
		this.roundType = roundType;
		this.difficulty = difficulty;
		this.theme = theme;
	}

	// Getters and setters
	public int getAmountOfQuestions() {
		return amountOfQuestions;
	}

	public void setAmountOfQuestions(int amountOfQuestions) {
		this.amountOfQuestions = amountOfQuestions;
	}

	public int getMaxAmountOfQuestions() {
		return maxAmountOfQuestions;
	}

	public void setMaxAmountOfQuestions(int maxAmountOfQuestions) {
		this.maxAmountOfQuestions = maxAmountOfQuestions;
	}

	public Map<Integer, Question> getQuestions() {
		return questions;
	}

	public RoundType getRoundType() {
		return roundType;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public Theme getTheme() {
		return theme;
	}

	// Adders
	public void addAnswer(int teamID, int questionID, int answer) {
		Map<Integer, Integer> questionAnswers = answers.get(questionID);
		if (questionAnswers == null)
			questionAnswers = new HashMap<Integer, Integer>();

		questionAnswers.put(teamID, answer);
		answers.put(questionID, questionAnswers);
	}

	// Methods
	public void makeQuestions() {
		for (amountOfQuestions = 0; amountOfQuestions < maxAmountOfQuestions; amountOfQuestions++) {
			switch (roundType) {
			case MC:
				questions.put(amountOfQuestions, new MCQuestion(difficulty, theme));
				break;
			case IP:
				questions.put(amountOfQuestions, new IPQuestion(difficulty, theme));
				break;
			default:
				// TODO: Go back and show error
			}
		}
	}

}
