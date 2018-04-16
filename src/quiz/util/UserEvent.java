package quiz.util;

import eventbroker.Event;
import main.Context;

@SuppressWarnings("serial")
public class UserEvent extends Event {

	protected int userID;

	public UserEvent() {
		this.userID = Context.getContext().getUser().getUserID();
		this.type = "CLIENT_USER";
		this.message = "";
	}

	public int getUserID() {
		return userID;
	}
}
