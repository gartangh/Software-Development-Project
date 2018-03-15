package application;

import test.Test;

public class Main {

	private final static boolean DEBUG = true;

	public static void main(String[] args) {
		Context context = Context.getContext();

		// Tests
		if (DEBUG && !Test.test())
				return;

		// TODO: Show WelcomeScreen
		// TODO: Show Login or CreateAccount to identify a User

		// TODO: On authentication: context.setUser(user)

		// TODO: Show OptionScreen (select Guest or Host mode)

		// TODO: On select mode: context.setUser(guest or host)
	}

}
