package eventbroker.serverevent;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import quiz.model.Quiz;
import server.ServerContext;

@SuppressWarnings("serial")
public class ServerGetQuizzesEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_GET_QUIZZES";

	private Map<Integer, Quiz> quizMap = new HashMap<Integer, Quiz>();

	// Constructor
	public ServerGetQuizzesEvent() {
		super.type = EVENTTYPE;

		Map<Integer, Quiz> map = ServerContext.getContext().getQuizMap();
		for (Entry<Integer, Quiz> quizEntry : map.entrySet()) {
			if (!quizEntry.getValue().isRunning())
				quizMap.put(quizEntry.getKey(), quizEntry.getValue());
		}
	}

	// Getter
	public Map<Integer, Quiz> getQuizMap() {
		return quizMap;
	}

}
