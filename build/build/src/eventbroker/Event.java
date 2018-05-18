package eventbroker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import quiz.model.User;

@SuppressWarnings("serial")
public class Event implements Serializable {

	protected String type = "EVENT";
	private ArrayList<Integer> recipients = new ArrayList<>();

	// Constructors
	public Event() {
		// Empty default constructor
	}

	// Getters and setters
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
		recipients.addAll(destinations);
	}

	public void addRecipients(Map<Integer, User> userMap) {
		for (Entry<Integer, User> entry : userMap.entrySet())
			addRecipient(entry.getValue().getUserID());
	}
	
	public void removeAllRecipients() {
		recipients.clear();
	}

}
