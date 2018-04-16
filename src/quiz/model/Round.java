package quiz.model;

import java.util.HashMap;
import java.util.Map;

import quiz.util.Difficulty;
import quiz.util.Theme;
import server.ServerContext;
import quiz.util.RoundType;

public class Round {
	
	private Map<Integer, Question> questions;	// Map(questionID -> question)
	private RoundType roundType;
	private Difficulty difficulty;
	private Theme theme;
	private Map<Integer, Map<Integer, Integer>> answers = new HashMap<Integer, Map<Integer, Integer>>(); // Map(questionID -> Map(teamID -> answerID))
	private int currentQuestion;

	public Round(RoundType roundType, Difficulty difficulty, Theme theme) {
		this.roundType = roundType;
		this.difficulty = difficulty;
		this.theme = theme;
		this.currentQuestion = -1;
	}

	// Getters and setters
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
	
	public int getNextQuestion() {
		currentQuestion++;
		return (int) answers.keySet().toArray()[currentQuestion];
	}

	// Methods
	public void addQuestions(int numberOfQuestions) {
		// TODO get questions out database
		Map<Integer, MCQuestion>  questions = ServerContext.getContext().getOrderedMCQuestionMap().get(theme.ordinal()).get(difficulty.ordinal());
		while(numberOfQuestions > 0) {
			int i = (int) Math.floor(Math.random()*questions.size());
			int qID = (int) questions.keySet().toArray()[i];
			if(!answers.containsKey(qID)) {
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

}
