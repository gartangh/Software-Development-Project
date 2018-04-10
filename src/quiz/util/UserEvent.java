package quiz.util;

import eventbroker.Event;
import main.Context;

@SuppressWarnings("serial")
public class UserEvent extends Event {
	protected int userID;
	
	public UserEvent(){
		super();
		this.userID = Context.getContext().getUser().getID();
		this.type = "USER";
	}
	
	public int getUserID() {
		return userID;
	}
}
