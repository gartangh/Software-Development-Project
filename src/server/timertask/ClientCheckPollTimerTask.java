package server.timertask;

import java.util.TimerTask;

import eventbroker.Event;
import eventbroker.EventListener;
import main.Main;

public class ClientCheckPollTimerTask extends TimerTask implements EventListener{
	
	public static final int POLL_TIMEOUT = PollUsersTimerTask.POLL_PERIOD + 2;
	public static final ClientCheckPollTimerTask clientCheckPollTimerTask = new ClientCheckPollTimerTask(); // Singleton
	
	private static Main main;
	private static int betweenPollsTime;
	private static boolean activated;
	
	private ClientCheckPollTimerTask() {
		super();
		betweenPollsTime = 0;
		activated = false;
	}
	
	public void setMain(Main main) {
		ClientCheckPollTimerTask.main = main;
	}

	@Override
	public void run() {
		synchronized(this) {
			if(activated) {
				betweenPollsTime++;
				if(betweenPollsTime == POLL_TIMEOUT) {
					main.onConnectionLost();
				}
			}
		}
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		synchronized(this) {
			betweenPollsTime = 0;
		}
	}
	
	public void activate() {
		synchronized(this) {
			activated = true;
		}
	}
	
	public void disable() {
		synchronized(this) {
			activated = false;
			betweenPollsTime = 0;
		}
	}
	
	public static ClientCheckPollTimerTask getClientCheckPollTimerTask(){
		return clientCheckPollTimerTask;
	}
	
}