package test;

import main.Context;
import user.model.Guest;
import user.model.Host;
import user.model.Player;
import user.model.Quizmaster;
import user.model.Spectator;
import user.model.User;

public class UserTest {

	@SuppressWarnings("unused")
	static boolean userTest() {
		try {
			Context context = Context.getContext();
			
			// Test casting
			User.createAccount("Username", "Password");
			User user1 = context.getUser();
			Guest guest1 = user1.castToGuest();
			Player player1 = guest1.castToPlayer();
			Guest guest2 = player1.castToGuest();
			Spectator spectator1 = guest2.castToSpectator();
			Guest guest3 = spectator1.castToGuest();
			User user2 = guest3.castToUser();
			Host host1 = user2.castToHost();
			Quizmaster quizmaster1 = host1.castToQuizmaster();
			Host host2 = quizmaster1.castToHost();
			User user3 = host2.castToUser();

			// Test invalid input
			// Username has invalid characters
			User.createAccount("Username.", "Password");
			User user4 = context.getUser();
			// Username to short
			User.createAccount("Use", "Password");
			User user5 = context.getUser();
			// Username to long
			User.createAccount("Usernameabc", "Password");
			User user6 = context.getUser();
			// Password has invalid characters
			User.createAccount("Usernam7", "Password.");
			User user7 = context.getUser();
			// Password to short
			User.createAccount("Usernam8", "Pas");
			User user8 = context.getUser();
			// Password to long
			User.createAccount("Usernam9", "Passwordabc");
			User user9 = context.getUser();
			// Username is not unique
			User.createAccount("Username", "Password");
			User user10 = context.getUser();

			// TODO: add tests
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}

}