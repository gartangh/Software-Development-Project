package server.timertask;

import java.util.ArrayList;
import java.util.TimerTask;

import eventbroker.EventPublisher;
import eventbroker.serverevent.ServerNewPixelSizeEvent;
import server.ServerContext;

public class IPQuestionTimerTask extends TimerTask {
	
	private static final int REPIXEL_PERIOD = 3;
	
	private int pixelSize;
	private int quizID;
	private int questionID;
	private int seconds = 0;
	private EventPublisher eventPublisher = new EventPublisher();
	
	public IPQuestionTimerTask(int quizID, int questionID, int pixelSize) {
		super();
		this.pixelSize = pixelSize;
		this.quizID = quizID;
		this.questionID = questionID;
	}

	@Override
	public void run() {
		seconds++;
		if(pixelSize > 1) {
			if(seconds % REPIXEL_PERIOD == 0) {
				pixelSize/=Math.floor(2);
				ServerNewPixelSizeEvent sNPSE = new ServerNewPixelSizeEvent(questionID, pixelSize);
				
				ArrayList<Integer> receivers = new ArrayList<>();
				receivers.addAll(ServerContext.getContext().getUsersFromQuiz(quizID));
				sNPSE.addRecipients(receivers);
				
				eventPublisher.publishEvent(sNPSE);
			}
		}
		else this.cancel();
	}
	
}
