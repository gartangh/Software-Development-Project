package server.timertask;

import java.util.Timer;
import java.util.TimerTask;

import eventbroker.Event;
import eventbroker.EventListener;
import main.Main;

public class ClientCheckPollTimerTask extends TimerTask implements EventListener{
	
	public static final int POLL_TIMEOUT = PollUsersTimerTask.POLL_PERIOD*2;
	public static final ClientCheckPollTimerTask clientCheckPollTimerTask = new ClientCheckPollTimerTask(); // Singleton
	public static final Timer pollTimer = new Timer();
	
	private static Main main;
	private static Object lock = new Object();
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
		synchronized(lock) {
			betweenPollsTime++;
			System.out.println("Between time: " + betweenPollsTime);
			if(betweenPollsTime == POLL_TIMEOUT) {
				main.onConnectionLost();
			}
			lock.notifyAll();
		}
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		synchronized(lock) {
			betweenPollsTime = 0;
			lock.notifyAll();
		}
	}
	
	public void activate() {
		if(!activated) {
			activated = true;
			betweenPollsTime = 0;
			pollTimer.schedule(clientCheckPollTimerTask, 0, 1000);
		}
	}
	
	public void disable() {
		if(activated) {
			this.cancel();
			activated = false;
		}
	}
	
	public static ClientCheckPollTimerTask getClientCheckPollTimerTask(){
		return clientCheckPollTimerTask;
	}
	
	public static Timer getPollTimer(){
		return pollTimer;
	}
	
}
