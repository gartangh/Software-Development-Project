package eventbroker.timerevent;

import eventbroker.Event;

@SuppressWarnings("serial")
public class TimerNewPollEvent extends Event{

	public final static String EVENTTYPE = "TIMER_NEW_POLL";
	private int pollID;

	// Constructor
	public TimerNewPollEvent(int pollID) {
		super.type = EVENTTYPE;
		this.pollID = pollID;
	}

	// Getter
	public int getPollID() {
		return pollID;
	}
}