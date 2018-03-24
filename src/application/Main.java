package application;

import java.util.concurrent.TimeUnit;

import test.Test;

public class Main {

	private final static boolean DEBUG = true;

	public static void main(String[] args) {
		
		Context context = Context.getContext();
		
		// Tests
		if (DEBUG && !Test.test())
				return;
		
		// TODO: Show WelcomeScreen	-> there is none atm :)
		// TODO: Show Login or CreateAccount to identify a User	-> done, validation still needed + push new user to server

		// TODO: On authentication: context.setUser(user) -> done

		// TODO: Show OptionScreen (select Guest or Host mode) -> not yet; in listOfQuizzes "add quiz button which makes him host or checkbox on authentication?

		// TODO: On select mode: context.setUser(guest or host)
	}

}
