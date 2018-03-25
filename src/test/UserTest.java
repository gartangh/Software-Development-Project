package test;

import user.Guest;
import user.Host;
import user.Player;
import user.Quizmaster;
import user.Spectator;
import user.User;

public class UserTest {

	@SuppressWarnings("unused")
	static boolean userTest() {
		try {
			// Test casting
			User user1 = User.createAccount("Username", "Password");
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
			User user4 = User.createAccount("Username.", "Password");
			// Username to short
			User user5 = User.createAccount("Use", "Password");
			// Username to long
			User user6 = User.createAccount("Usernameabc", "Password");
			// Password has invalid characters
			User user7 = User.createAccount("Usernam7", "Password.");
			// Password to short
			User user8 = User.createAccount("Usernam8", "Pas");
			// Password to long
			User user9 = User.createAccount("Usernam9", "Passwordabc");
			// Username is not unique
			User user10 = User.createAccount("Username", "Password");

			// TODO: add tests
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}

		return true;
	}

}