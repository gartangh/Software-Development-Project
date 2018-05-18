package eventbroker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import main.MainContext;
import network.Network;
import server.timertask.ClientCheckPollTimerTask;

final public class EventBroker implements Runnable {

	private final static EventBroker broker = new EventBroker(); // Singleton

	private LinkedList<QueueItem> queue = new LinkedList<>();
	private Map<String, ArrayList<EventListener>> listeners = new HashMap<>();
	private Map<String, ArrayList<EventListener>> newListeners = new HashMap<>();
	private ArrayList<EventListener> toRemoveListeners = new ArrayList<>();

	private boolean stop = false;
	private boolean proceed;
	private boolean done;

	private EventBroker() {
		// Empty constructor
	}

	// Getters
	public static EventBroker getEventBroker() {
		return broker;
	}

	public void addEventListener(EventListener el) {
		if (newListeners.get("all") == null) {
			ArrayList<EventListener> al = new ArrayList<>();
			newListeners.put("all", al);
		}

		newListeners.get("all").add(el);
	}

	public void addEventListener(String type, EventListener el) {
		if (newListeners.get(type) == null) {
			ArrayList<EventListener> al = new ArrayList<>();
			newListeners.put(type, al);
		}

		newListeners.get(type).add(el);
	}

	public void removeEventListener(EventListener el) {
		toRemoveListeners.add(el);
	}
	
	public void removeEventListeners() {
		Network network = MainContext.getContext().getNetwork();
		for (ArrayList<EventListener> topicListeners : listeners.values())
			if (!(topicListeners.contains(network) || topicListeners.contains(ClientPollHandler.getClientPollHandler()) || topicListeners.contains(ClientCheckPollTimerTask.getClientCheckPollTimerTask())))
				toRemoveListeners.addAll(topicListeners);
			else
				for (EventListener listener : topicListeners)
					if (listener != network && listener != ClientPollHandler.getClientPollHandler() && listener != ClientCheckPollTimerTask.getClientCheckPollTimerTask())
						toRemoveListeners.add(listener);
	}

	public void addEvent(EventPublisher source, Event event) {
		QueueItem qI = new QueueItem(source, event);
		synchronized (this) {
			queue.add(qI);
			setProceed(true);
			this.notifyAll();
		}
	}

	private void process(EventPublisher source, Event event) {		
		for (Map.Entry<String, ArrayList<EventListener>> entry : newListeners.entrySet()) {
			if (!listeners.containsKey(entry.getKey()))
				listeners.put(entry.getKey(), entry.getValue());
			else
				listeners.get(entry.getKey()).addAll(entry.getValue());
		}

		newListeners.clear();

		for (EventListener el : toRemoveListeners) {
			for (Map.Entry<String, ArrayList<EventListener>> entry : listeners.entrySet())
				if (entry.getValue().contains(el))
					entry.getValue().remove(el);
		}

		toRemoveListeners.clear();

		for (Map.Entry<String, ArrayList<EventListener>> entry : listeners.entrySet())
			if (entry.getKey().equals(event.getType()) || entry.getKey().equals("all"))
				for (EventListener el : entry.getValue())
					if (source != el)
						el.handleEvent(event);
	}

	@Override
	public void run() {
		while (!stop) {
			synchronized (this) {
				try {
					while (!proceed) {
						if (stop) {
							done = true;
							this.notifyAll();

							break;
						}

						wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				this.setProceed(false);
			}

			while (!queue.isEmpty()) {
				QueueItem qI = null;

				synchronized (this.queue) {
					if (!queue.isEmpty())
						qI = queue.poll();
				}

				if (qI != null)
					process(qI.source, qI.event);
			}
		}

		synchronized (this) {
			done = true;
			this.notifyAll();
		}

		System.out.println("EventBroker terminated");
	}

	public void start() {
		new Thread(this).start();
	}

	public void stop() {
		synchronized (this) {
			stop = true;
			this.notifyAll();
		}

		synchronized (this) {
			try {
				while (!done)
					wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void setProceed(boolean p) {
		proceed = p;
	}

	// Internal class
	private class QueueItem {

		Event event;
		EventPublisher source;

		public QueueItem(EventPublisher source, Event e) {
			this.source = source;
			this.event = e;
		}

	}

}
