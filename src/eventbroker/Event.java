package eventbroker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import user.model.User;

@SuppressWarnings("serial")
public class Event implements Serializable {

	protected static long n = 0;

	protected String type = "EVENT";
	protected String message = "";
	protected long eventID = n++;
	private ArrayList<Integer> recipients = new ArrayList<>();

	// Constructors
	public Event() {
		// Empty default contstructor
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

	// Adders and removers
	public void addRecipient(Integer userID) {
		recipients.add(userID);
	}

	public void addRecipients(ArrayList<Integer> destinations) {
		for (Integer userID : destinations)
			addRecipient(userID);
	}

	public void addRecipients(Map<Integer, User> userMap) {
		for (Entry<Integer, User> entry : userMap.entrySet())
			addRecipient(entry.getValue().getUserID());
	}

	public void removeAllRecipients() {
		recipients.clear();
	}

	// Methods
	@Override
	public String toString() {
		return "ID" + eventID + "[" + type + "] : " + message;
	}

}
