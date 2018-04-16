package eventbroker;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Event implements Serializable {

	private static long n = 0;

	protected String type;
	protected String message;
	private long id = n++;
	private ArrayList<Integer> recipients = new ArrayList<>();

	// Constructors
	public Event() {
		// Empty default constructor
	}

	public Event(String type, String message) {
		this.type = type;
		this.message = message;
	}

	// Getters and setters
	public String getMessage() {
		return message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<Integer> getRecipients() {
		return recipients;
	}

	// Methods
	@Override
	public String toString() {
		return "ID" + id + "[" + type + "] : " + message;
	}

	// Adders
	public void addRecipient(Integer userID) {
		recipients.add(userID);
	}

	public void addRecipients(ArrayList<Integer> destinations) {
		for (Integer userID : destinations)
			addRecipient(userID);
	}

	public void removeAllRecipients() {
		recipients.clear();
	}

}
