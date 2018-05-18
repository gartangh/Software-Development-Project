package eventbroker.timerevent;

import eventbroker.Event;

@SuppressWarnings("serial")
public class TimerPollFinishedEvent extends Event{
	
	public final static String EVENTTYPE = "TIMER_POLL_FINISHED";
	
	private int pollID;

	// Constructor
	public TimerPollFinishedEvent(int pollID) {
		super.type = EVENTTYPE;
		this.pollID = pollID;
	}

	// Getter
	public int getPollID() {
		return pollID;
	}
}
