package quiz.model;

import java.util.HashMap;
import java.util.Map;

import quiz.util.Difficulty;
import quiz.util.Theme;
import server.ServerContext;
import quiz.util.RoundType;

public class Round {

	public final static int MINQUESTIONS = 1;
	public final static int MAXQUESTIONS = 5;

	private RoundType roundType;
	private Theme theme;
	private Difficulty difficulty;
	private int questions;
	private int currentQuestion = -1;
	// Map(questionID -> Map(teamID -> answerID))
	private Map<Integer, Map<Integer, Integer>> answers = new HashMap<>();

	// Constructor
	public Round(RoundType roundType, Theme theme, Difficulty difficulty, int questions) {
		this.roundType = roundType;
		this.theme = theme;
		this.difficulty = difficulty;
		this.questions = questions;
		addQuestions(questions);
	}

	// Getters and setters
	public RoundType getRoundType() {
		return roundType;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public Theme getTheme() {
		return theme;
	}

	public int getCurrentQuestion() {
		return currentQuestion;
	}

	public int getQuestions() {
		return questions;
	}

	public int getNumberOfAnswers() {
		return answers.get((int) answers.keySet().toArray()[currentQuestion]).size();
	}

	// Methods
	private void addQuestions(int numberOfQuestions) {
		// TODO get questions out of database
		// Should not be loaded from ServerContext, right?
		Map<Integer, MCQuestion> questions = ServerContext.getContext().getOrderedMCQuestionMap().get(theme.ordinal())
				.get(difficulty.ordinal());
		
		while (numberOfQuestions > 0) {
			int i = (int) Math.floor(Math.random() * questions.size());
			int qID = (int) questions.keySet().toArray()[i];
			if (!answers.containsKey(qID)) {
				answers.put(qID, new HashMap<Integer, Integer>());
				numberOfQuestions--;
			}
		}
	}

	public void addAnswer(int teamID, int questionID, int answer) {
		Map<Integer, Integer> questionAnswers = answers.get(questionID);
		questionAnswers.put(teamID, answer);
		answers.put(questionID, questionAnswers);
	}
	
	public int getNextQuestion() {
		return (int) answers.keySet().toArray()[++currentQuestion];
	}

}
