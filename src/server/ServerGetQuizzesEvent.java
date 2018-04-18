package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import quiz.model.Quiz;

@SuppressWarnings("serial")
public class ServerGetQuizzesEvent extends ServerEvent {

	private Map<Integer, Quiz> quizMap=new HashMap<Integer,Quiz>();

	public ServerGetQuizzesEvent() {
		Map<Integer,Quiz> map=ServerContext.getContext().getQuizMap();
			for (Entry<Integer,Quiz> quizEntry:map.entrySet()){
				if(!quizEntry.getValue().isRunning())
					quizMap.put(quizEntry.getKey(),quizEntry.getValue());
			}
		this.type = "SERVER_GET_QUIZZES";
	}

	public Map<Integer, Quiz> getQuizMap() {
		return quizMap;
	}

}
