package server.timertask;

import java.util.TimerTask;

import eventbroker.EventPublisher;
import eventbroker.timerevent.IPTimerEvent;

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
				IPTimerEvent iPTE=new IPTimerEvent(pixelSize, quizID , questionID);
				eventPublisher.publishEvent(iPTE);
			}
		}
		else this.cancel();
	}

}
