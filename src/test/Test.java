package test;

public class Test {

	public static boolean test() {
		if (!UserTest.userTest()) {
			System.err.println("Something went wrong in the user package!");

			return false;
		}

		if (!QuizTest.quizTest()) {
			System.err.println("Something went wrong in the quiz package!");

			return false;
		}

		if (!NetworkTest.networkTest()) {
			System.err.println("Something went wrong in the network package!");

			return false;
		}

		if (!GuiTest.guiTest()) {
			System.err.println("Something went wrong in the gui package!");

			return false;
		}

		return true;
	}

}
