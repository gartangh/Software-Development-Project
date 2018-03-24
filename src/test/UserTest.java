package test;

import user.*;

public class UserTest {

	static boolean userTest() {
		try {
			// Test casting
			User user1 = new User("Username", "Password");
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
			User user4 = new User("Username.", "Password"); // Username has
															// invalid
															// characters
			User user5 = new User("Use", "Password"); // Username to short
			User user6 = new User("Usernameabc", "Password"); // Username to
																// long
			User user7 = new User("Usernam7", "Password."); // Password has
															// invalid
															// characters
			User user8 = new User("Usernam8", "Pas"); // Password to short
			User user9 = new User("Usernam9", "Passwordabc"); // Password to
																// long
			User user10 = new User("Username", "Password"); // Username is not
															// unique

			// TODO: add tests
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}