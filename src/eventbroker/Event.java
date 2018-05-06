package eventbroker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import quiz.model.User;

@SuppressWarnings("serial")
public class Event implements Serializable {

	protected String type = "EVENT";
	protected String message = "";
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

	// Method
	@Override
	public String toString() {
		return "[" + type + "] : " + message;
	}

}
