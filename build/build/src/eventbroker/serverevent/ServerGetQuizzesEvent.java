package eventbroker.serverevent;

import java.util.ArrayList;

import quiz.model.Quiz;
import quiz.model.QuizModel;
import server.ServerContext;

@SuppressWarnings("serial")
public class ServerGetQuizzesEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_GET_QUIZZES";

	private ArrayList<QuizModel> quizzes = new ArrayList<>();

	// Constructor
	public ServerGetQuizzesEvent() {
		super.type = EVENTTYPE;

		// Add all running quizzes to quizzes
		for (Quiz quiz : ServerContext.getContext().getQuizMap().values())
			if (!quiz.isRunning())
				quizzes.add(new QuizModel(quiz.getQuizID(), quiz.getQuizname(), quiz.getRounds(), quiz.getTeams(),
						quiz.getPlayers(), quiz.getHostID(), quiz.getHostname()));
	}

	// Getter
	public ArrayList<QuizModel> getQuizzes() {
		return quizzes;
	}

}
