package quiz.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.Context;
import quiz.util.Difficulty;
import quiz.util.Theme;
import quiz.util.Type;
import server.ServerContext;

public class Round {

	private Type type;
	private Difficulty difficulty;
	private Theme theme;
	private Map<Integer, Map<Integer, Integer>> answers = new HashMap<Integer, Map<Integer, Integer>>(); // Map(questionID -> Map(teamID -> answerID))
	private int currentQuestion;

	public Round(Type type, Difficulty difficulty, Theme theme) {
		this.type = type;
		this.difficulty = difficulty;
		this.theme = theme;
		this.currentQuestion = -1;
	}

 	// TODO: Add getQuestions()

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
		boolean ready = false;
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
