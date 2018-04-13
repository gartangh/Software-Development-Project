package eventbroker;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Event implements Serializable{

	private static long n = 0;
	
	protected String type;

	protected String message;
	private long id = n++;

	public Event() {
		// Empty default constructor
	}
	
	public Event(String type, String message) {
		this.type = type;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "ID" + id + "[" + type + "] : " + message;
	}

	public void setType(String type) {
		this.type = type;
	}

}
