package quiz.model;

import java.io.Serializable;

import main.Context;
import quiz.util.UserType;
import server.ServerContext;

@SuppressWarnings("serial")
public class User implements Serializable {

	public final static String USERNAMEREGEX = "^[a-zA-Z0-9._-]{3,}$";
	public final static String PASSWORDREGEX = "^[a-zA-Z0-9._-]{3,}$";

	private int userID;
	private String username;
	private String password;
	private int level = 1;
	private long xp = 0L;
	private UserType userType = UserType.USER;

	// Constructors
	public User(int userID, String username, String password) {
		this.userID = userID;
		this.username = username;
		this.password = password;
	}

	private User(int userID, String username, String password, int level, long xp) {
		this(userID, username, password);
		this.level = level;
		this.xp = xp;
	}

	User(User user) {
		// Copy constructor
		this.username = user.username;
		this.password = user.password;
		this.level = user.level;
		this.xp = user.xp;
		this.userID = user.userID;
	}

	// Factory methods
	public static int createUser(String username, String password, int connectionID) {
		// Server side
		// Generate random userID
		ServerContext context = ServerContext.getContext();
		int userID;
		do {
			userID = (int) (Math.random() * Integer.MAX_VALUE);
		} while (context.getUserMap().containsKey(userID));
		
		context.getUserMap().put(userID, new User(userID, username, password));
		context.getNetwork().getUserIDConnectionIDMap().put(userID, connectionID);
		
		return userID;
	}
	
	public static void createAccount(int userID, String username, String password) {
		// Client side
		Context context = Context.getContext();
		context.setUser(new User(userID, username, password));
		context.getNetwork().getUserIDConnectionIDMap().put(userID, 0);
	}
	
	public static void logIn(int userID, String username, String password, int level, long xp) {
		// Client side
		Context context = Context.getContext();
		context.setUser(new User(userID, username, password, level, xp));
		context.getNetwork().getUserIDConnectionIDMap().put(userID, 0);
	}

	// Getters and setters
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	// Password cannot be changed for the moment
	/*public int setPassword(String password1, String password2) {
		if (!password1.equals(password2)) {
			// TODO: Go back and show error

			return 1;
		} else if (!password1.matches(PASSWORDREGEX)) {
			// TODO: Go back and show error

			return 2;
		}

		this.password = password1;

		return 0;
	}*/

	public int getLevel() {
		return level;
	}

	public long getXp() {
		return xp;
	}
	
	public UserType getUserType() {
		return userType;
	}
	
	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	// Methods
	public void addXp(int xp) {
		this.xp += xp;

		if (this.xp >= level * 1000) {
			this.level++;
			this.xp -= level * 1000;
		}
	}

	@Override
	public String toString() {
		// For tableview in Quizroom
		return username;
	}

}
