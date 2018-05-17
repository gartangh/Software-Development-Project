package server.timertask;

import java.util.ArrayList;
import java.util.TimerTask;

import eventbroker.EventPublisher;
import eventbroker.timerevent.TimerNewPollEvent;
import eventbroker.timerevent.TimerPollFinishedEvent;

public class PollUsersTimerTask extends TimerTask {
	
	public static final int POLL_PERIOD = 1;
	private int pollID;
	private boolean started;
	
	private EventPublisher eventPublisher = new EventPublisher();
	
	public PollUsersTimerTask() {
		super();
		this.pollID = 0;
		this.started = false;
	}

	@Override
	public void run() {
		if(started) {
			TimerPollFinishedEvent tPFE = new TimerPollFinishedEvent(this.pollID);
			eventPublisher.publishEvent(tPFE);
		}
		else {
			started = true;
		}
		
		this.pollID++;
		
		TimerNewPollEvent tPSE = new TimerNewPollEvent(this.pollID);
		eventPublisher.publishEvent(tPSE);
	}
	
}
