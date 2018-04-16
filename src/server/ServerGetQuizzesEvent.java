package server;

import java.util.Map;

import quiz.model.Quiz;

@SuppressWarnings("serial")
public class ServerGetQuizzesEvent extends ServerEvent {

	private Map<Integer, Quiz> quizMap;

	public ServerGetQuizzesEvent() {
		quizMap = ServerContext.getContext().getQuizMap();
		this.type = "SERVER_GET_QUIZZES";
	}

	public Map<Integer, Quiz> getQuizMap() {
		return quizMap;
	}

}
