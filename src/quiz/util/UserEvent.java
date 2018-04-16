package quiz.util;

import eventbroker.Event;
import main.Context;

@SuppressWarnings("serial")
public class UserEvent extends Event {

	protected String username;

	public UserEvent() {
		this.username = Context.getContext().getUser().getUsername();
		this.type = "USER";
		this.message = "";
	}

	public String getUsername() {
		return username;
	}

}
