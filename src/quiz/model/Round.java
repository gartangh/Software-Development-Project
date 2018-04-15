package quiz.model;

import java.util.HashMap;
import java.util.Map;

import quiz.util.Difficulty;
import quiz.util.Theme;
import quiz.util.QuizType;

public class Round {

	private int amountOfQuestions;
	private int maxAmountOfQuestions;
	// Map(questionID -> question)
	private Map<Integer, Question> questions;
	private QuizType quizType;
	private Difficulty difficulty;
	private Theme theme;
	// Map(questionID -> Map(teamname -> answerID))
	private Map<Integer, Map<String, Integer>> answers;

	public Round(int maxAmountOfQuestions, QuizType quizType, Difficulty difficulty, Theme theme) {
		this.maxAmountOfQuestions = maxAmountOfQuestions;
		this.quizType = quizType;
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

	public QuizType getQuizType() {
		return quizType;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public Theme getTheme() {
		return theme;
	}

	// Adders
	public void addAnswer(String teamname, int questionID, int answer) {
		Map<String, Integer> questionAnswers = answers.get(questionID);
		if (questionAnswers == null)
			questionAnswers = new HashMap<String, Integer>();

		questionAnswers.put(teamname, answer);
		answers.put(questionID, questionAnswers);
	}

	// Methods
	public void makeQuestions() {
		for (amountOfQuestions = 0; amountOfQuestions < maxAmountOfQuestions; amountOfQuestions++) {
			switch (quizType) {
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
