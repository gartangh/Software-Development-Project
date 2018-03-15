package test;

import user.*;

public class UserTest {

	static boolean userTest() {
		try {
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
			
			// TODO: add tests
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}