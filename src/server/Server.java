package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;

import chat.ChatMessage;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientChangeTeamEvent;
import eventbroker.clientevent.ClientAnswerEvent;
import eventbroker.clientevent.ClientCreateAccountEvent;
import eventbroker.clientevent.ClientCreateQuizEvent;
import eventbroker.clientevent.ClientCreateRoundEvent;
import eventbroker.clientevent.ClientGetQuizzesEvent;
import eventbroker.clientevent.ClientHostReadyEvent;
import eventbroker.clientevent.ClientJoinQuizEvent;
import eventbroker.clientevent.ClientLeaveQuizEvent;
import eventbroker.clientevent.ClientLogInEvent;
import eventbroker.clientevent.ClientLogOutEvent;
import eventbroker.clientevent.ClientNewQuestionEvent;
import eventbroker.clientevent.ClientScoreboardDataEvent;
import eventbroker.clientevent.ClientVoteEvent;
import eventbroker.clientevent.ClientCreateTeamEvent;
import eventbroker.clientevent.ClientDeleteTeamEvent;
import eventbroker.clientevent.ClientEndQuizEvent;
import eventbroker.serverevent.ServerVoteAnswerEvent;
import eventbroker.serverevent.ServerAlreadyLoggedInEvent;
import eventbroker.serverevent.ServerChangeTeamEvent;
import eventbroker.serverevent.ServerCreateAccountFailEvent;
import eventbroker.serverevent.ServerCreateAccountSuccesEvent;
import eventbroker.serverevent.ServerCreateQuizFailEvent;
import eventbroker.serverevent.ServerEndQuizEvent;
import eventbroker.serverevent.ServerGetQuizzesEvent;
import eventbroker.serverevent.ServerHostLeavesQuizEvent;
import eventbroker.serverevent.ServerJoinQuizEvent;
import eventbroker.serverevent.ServerLogInFailEvent;
import eventbroker.serverevent.ServerLogInSuccesEvent;
import eventbroker.serverevent.ServerNewIPQuestionEvent;
import eventbroker.serverevent.ServerNewMCQuestionEvent;
import eventbroker.serverevent.ServerNewRoundEvent;
import eventbroker.serverevent.ServerDeleteTeamEvent;
import eventbroker.serverevent.ServerNewTeamEvent;
import eventbroker.serverevent.ServerCreateTeamSuccesEvent;
import eventbroker.serverevent.ServerNotAllAnsweredEvent;
import eventbroker.serverevent.ServerPlayerLeavesQuizEvent;
import eventbroker.serverevent.ServerQuizNewPlayerEvent;
import eventbroker.serverevent.ServerCreateQuizSuccesEvent;
import eventbroker.serverevent.ServerCreateTeamFailEvent;
import eventbroker.serverevent.ServerScoreboardDataEvent;
import eventbroker.serverevent.ServerNewQuizEvent;
import eventbroker.serverevent.ServerStartQuizEvent;
import eventbroker.serverevent.ServerStartRoundEvent;
import eventbroker.serverevent.ServerVoteEvent;
import javafx.scene.paint.Color;
import main.Main;
import quiz.model.Team;
import quiz.model.User;
import quiz.util.Difficulty;
import quiz.util.RoundType;
import quiz.util.Theme;
import server.timertask.IPQuestionTimerTask;
import server.timertask.QuestionDurationTimerTask;
import network.Network;
import quiz.model.IPQuestion;
import quiz.model.MCQuestion;
import quiz.model.Quiz;
import quiz.model.QuizModel;

public class Server extends EventPublisher {

	// Singleton
	private final static Server server = new Server();
	
	// Handlers
	private final static CreateAccountHandler createAccountHandler = new CreateAccountHandler();
	private final static LogInHandler logInHandler = new LogInHandler();
	private final static LogOutHandler logOutHandler = new LogOutHandler();
	private final static JoinQuizHandler joinQuizHandler = new JoinQuizHandler();
	private final static CreateQuizHandler createQuizHandler = new CreateQuizHandler();
	private final static VoteHandler voteHandler = new VoteHandler();
	private final static AnswerHandler answerHandler = new AnswerHandler();
	private final static NewQuestionHandler newQuestionHandler = new NewQuestionHandler();
	private final static CreateRoundHandler createRoundHandler = new CreateRoundHandler();
	private final static ChatHandler chatHandler = new ChatHandler();
	private final static ScoreboardDataHandler scoreboardDataHandler = new ScoreboardDataHandler();
	private final static GetQuizzesHandler getQuizzesHandler = new GetQuizzesHandler();
	private final static CreateTeamHandler newTeamHandler = new CreateTeamHandler();
	private final static ChangeTeamHandler changeTeamHandler = new ChangeTeamHandler();
	private final static HostReadyHandler hostReadyHandler = new HostReadyHandler();
	private final static DeleteTeamHandler deleteTeamHandler = new DeleteTeamHandler();
	private final static LeaveQuizHandler leaveQuizHandler = new LeaveQuizHandler();
	private final static EndQuizHandler endQuizHandler = new EndQuizHandler();

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		// Create a network on port Main.SERVERPORT and type SERVER
		Network network = new Network(Main.SERVERPORT, Network.SERVERTYPE);
		ServerContext.getContext().setNetwork(network);

		try {
			if (Main.LOCAL)
				System.out.println(InetAddress.getLocalHost());
			else
				System.out.println(InetAddress.getLocalHost().getHostAddress());

			System.out.println(Integer.toString(network.getConnectionListener().getServerPort()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// Load Data
		ServerContext.getContext().loadData();

		// Add EventListeners
		EventBroker eventBroker = EventBroker.getEventBroker();

		eventBroker.addEventListener(network);

		eventBroker.addEventListener(ClientCreateAccountEvent.EVENTTYPE, createAccountHandler);
		eventBroker.addEventListener(ClientLogInEvent.EVENTTYPE, logInHandler);
		eventBroker.addEventListener(ClientLogOutEvent.EVENTTYPE, logOutHandler);
		eventBroker.addEventListener(ClientJoinQuizEvent.EVENTTYPE, joinQuizHandler);
		eventBroker.addEventListener(ClientCreateQuizEvent.EVENTTYPE, createQuizHandler);
		eventBroker.addEventListener(ClientVoteEvent.EVENTTYPE, voteHandler);
		eventBroker.addEventListener(ClientAnswerEvent.EVENTTYPE, answerHandler);
		eventBroker.addEventListener(ClientNewQuestionEvent.EVENTTYPE, newQuestionHandler);
		eventBroker.addEventListener(ClientCreateRoundEvent.EVENTTYPE, createRoundHandler);
		eventBroker.addEventListener(ChatMessage.CLIENTTYPE, chatHandler);
		eventBroker.addEventListener(ClientScoreboardDataEvent.EVENTTYPE, scoreboardDataHandler);
		eventBroker.addEventListener(ClientGetQuizzesEvent.EVENTTYPE, getQuizzesHandler);
		eventBroker.addEventListener(ClientCreateTeamEvent.EVENTTYPE, newTeamHandler);
		eventBroker.addEventListener(ClientChangeTeamEvent.EVENTTYPE, changeTeamHandler);
		eventBroker.addEventListener(ClientHostReadyEvent.EVENTTYPE, hostReadyHandler);
		eventBroker.addEventListener(ClientDeleteTeamEvent.EVENTTYPE, deleteTeamHandler);
		eventBroker.addEventListener(ClientLeaveQuizEvent.EVENTTYPE, leaveQuizHandler);
		eventBroker.addEventListener(ClientEndQuizEvent.EVENTTYPE, endQuizHandler);

		// Start the EventBroker
		eventBroker.start();

		System.out.println("Server running ...");
	}

	/**
	 * On connection lost.
	 *
	 * @param userID
	 *            the user ID
	 */
	public static void onConnectionLost(int userID) {
		ServerContext context = ServerContext.getContext();
		// Log user out
		User user = context.getUser(userID);
		user.setLoggedIn(false);
		context.getNetwork().getUserIDConnectionIDMap().remove(userID);
		int toRemoveTeamID=-1;
		int toRemoveQuizID=-1;

		for (Quiz quiz : context.getQuizMap().values()) {
			boolean foundTeam = false;
			boolean foundQuiz = false;
			for (Team team : quiz.getTeamMap().values()) {
				for (int playerID : team.getPlayerMap().keySet()) {
					if (userID == playerID) {
						// The user is in the team
						foundTeam = true;
						foundQuiz = true;
						toRemoveTeamID=team.getTeamID();
						toRemoveQuizID=quiz.getQuizID();
						
						break;
					}
				}
				if (foundTeam)
					break;
			}

			for (int uPlayerID : quiz.getUnassignedPlayers().keySet()){
				if (userID == uPlayerID) {
					// The user is in the team
					foundQuiz = true;
					toRemoveQuizID=quiz.getQuizID();
					
					break;
				}
			}

			if (foundQuiz)
				break;
		}

		if (toRemoveQuizID != -1)
			playerLeavesQuiz(toRemoveQuizID, userID, toRemoveTeamID);
	}

	public static void playerLeavesQuiz(int quizID, int userID, int teamID) {
		ServerContext context = ServerContext.getContext();
		Quiz quiz = context.getQuizMap().get(quizID);
		User user = context.getUserMap().get(userID);
		Team team = quiz.getTeamMap().get(teamID);

		if (user.getUserID() == quiz.getHostID()) {
			context.getQuizMap().remove(quiz.getQuizID());
			context.terminateTimers(quiz.getQuizID());
			ServerHostLeavesQuizEvent sHLQE = new ServerHostLeavesQuizEvent(quiz.getQuizID());
			sHLQE.addRecipients(context.getUserMap());
			server.publishEvent(sHLQE);
		} else {
			int captainID = -1;
			ArrayList<Integer> receivers = context.getUsersFromQuiz(quiz.getQuizID());
			if (team != null) {
				context.changeTeam(quiz.getQuizID(), team.getTeamID(), user.getUserID(), 'd');
				captainID = team.getCaptainID();
				if (quiz.getTeamMap().size()<Quiz.MINTEAMS && quiz.isRunning()) {
					context.getQuizMap().remove(quiz.getQuizID());
					context.terminateTimers(quiz.getQuizID());
				}
			} else {
				quiz.removeUnassignedPlayer(user.getUserID());
			}

			ServerPlayerLeavesQuizEvent sPLQE = new ServerPlayerLeavesQuizEvent(quiz.getQuizID(), user.getUserID(),
					teamID, captainID, quiz.isRunning());
			sPLQE.addRecipients(receivers);
			server.publishEvent(sPLQE);
		}

	}

	// Inner classes
	private static class CreateAccountHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientCreateAccountEvent cCAE = (ClientCreateAccountEvent) event;

			String username = cCAE.getUsername();
			int connectionID = cCAE.getConnectionID();

			boolean exists = false;
			for (Entry<Integer, User> user : ServerContext.getContext().getUserMap().entrySet()) {
				if (user.getValue().getUsername().equals(username)) {
					exists = true;
					break;
				}
			}

			if (exists) {
				// Username is already in use
				ServerCreateAccountFailEvent sCAFE = new ServerCreateAccountFailEvent(connectionID);
				server.publishEvent(sCAFE);
			} else {
				// Username is not yet in use
				String password = cCAE.getPassword();

				int userID = User.createUser(username, password, connectionID);

				ServerCreateAccountSuccesEvent sCASE = new ServerCreateAccountSuccesEvent(userID, username, password,
						connectionID);
				sCASE.addRecipient(userID);
				server.publishEvent(sCASE);
			}
		}

	}

	private static class LogInHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientLogInEvent cLIE = (ClientLogInEvent) event;

			String username = cLIE.getUsername();
			String password = cLIE.getPassword();
			int connectionID = cLIE.getConnectionID();

			int userID = -1;
			int level = 0;
			long xp = 0L;
			for (Entry<Integer, User> user : ServerContext.getContext().getUserMap().entrySet()) {
				if (user.getValue().getUsername().equals(username) && user.getValue().getPassword().equals(password)) {
					if (user.getValue().isLoggedIn()) {
						// User is already logged in
						ServerAlreadyLoggedInEvent sALIE = new ServerAlreadyLoggedInEvent(connectionID);
						server.publishEvent(sALIE);

						return;
					}

					// User is not logged in yet
					userID = user.getKey();
					level = user.getValue().getLevel();
					xp = user.getValue().getXp();
					break;
				}
			}

			if (userID == -1) {
				// Username and / or password are incorrect
				ServerLogInFailEvent sLIFE = new ServerLogInFailEvent(connectionID);
				server.publishEvent(sLIFE);

				return;
			}

			// Username and password are correct
			ServerContext context = ServerContext.getContext();
			context.getUserMap().get(userID).setLoggedIn(true);
			context.getNetwork().getUserIDConnectionIDMap().put(userID, connectionID);

			ServerLogInSuccesEvent sLISE = new ServerLogInSuccesEvent(userID, username, password, level, xp);
			sLISE.addRecipient(userID);
			server.publishEvent(sLISE);
		}

	}

	private static class LogOutHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientLogOutEvent cLOE = (ClientLogOutEvent) event;

			int userID = cLOE.getUserID();

			ServerContext context = ServerContext.getContext();
			context.getUserMap().get(userID).setLoggedIn(false);
			context.getNetwork().getUserIDConnectionIDMap().remove(userID);
		}

	}

	private static class JoinQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientJoinQuizEvent cJQE = (ClientJoinQuizEvent) event;

			int userID = cJQE.getUserID();
			String username = cJQE.getUsername();
			int quizID = cJQE.getQuizID();

			ServerContext context = ServerContext.getContext();
			context.getQuizMap().get(quizID).addUnassignedPlayer(userID, username);

			ServerJoinQuizEvent sJQE = new ServerJoinQuizEvent(context.getQuizMap().get(quizID));
			sJQE.addRecipient(userID);
			server.publishEvent(sJQE);

			ServerQuizNewPlayerEvent sQNP = new ServerQuizNewPlayerEvent(userID, username);
			sQNP.addRecipients(context.getUsersFromQuiz(quizID));
			server.publishEvent(sQNP);
		}

	}

	private static class CreateQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientCreateQuizEvent cCQE = (ClientCreateQuizEvent) event;

			String quizname = cCQE.getQuizname();

			ServerContext context = ServerContext.getContext();
			boolean exists = false;
			for (Quiz quiz : context.getQuizMap().values())
				if (quiz.getQuizname().equals(quizname)) {
					exists = true;
					break;
				}

			if (exists) {
				// Quizname is already in use
				ServerCreateQuizFailEvent sCQFE = new ServerCreateQuizFailEvent();
				sCQFE.addRecipient(cCQE.getUserID());
				server.publishEvent(sCQFE);
			} else {
				// Quizname is not yet in use
				int userID = cCQE.getUserID();
				int teams = cCQE.getTeams();
				int players = cCQE.getPlayers();
				int rounds = cCQE.getRounds();
				String hostname = cCQE.getHostname();

				int quizID = Quiz.createServerQuiz(quizname, rounds, teams, players, userID, hostname);

				ServerCreateQuizSuccesEvent sCQSE = new ServerCreateQuizSuccesEvent(
						new QuizModel(quizID, quizname, teams, players, rounds, userID, hostname));
				sCQSE.addRecipient(userID);
				server.publishEvent(sCQSE);

				ServerNewQuizEvent sNQE = new ServerNewQuizEvent(
						new QuizModel(quizID, quizname, teams, players, rounds, userID, hostname));
				sNQE.addRecipients(context.getUserMap());
				server.publishEvent(sNQE);
			}
		}

	}

	private static class CreateTeamHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientCreateTeamEvent cCTE = (ClientCreateTeamEvent) event;

			int oldTeamID = cCTE.getOldTeamID();
			int userID = cCTE.getUserID();
			int quizID = cCTE.getQuizID();
			String teamname = cCTE.getTeamname();

			ServerContext context = ServerContext.getContext();
			boolean exists = false;
			for (Team team : context.getQuizMap().get(quizID).getTeamMap().values())
				if (team.getTeamname().equals(teamname)) {
					exists = true;
					break;
				}

			if (exists) {
				// Teamname is already in use
				ServerCreateTeamFailEvent sCTFE = new ServerCreateTeamFailEvent();
				sCTFE.addRecipient(userID);
				server.publishEvent(sCTFE);
			} else {
				// Teamname is not yet in use
				Color color = cCTE.getColor();
				int captainID = cCTE.getUserID();
				String captainname = cCTE.getCaptainname();

				int teamID = Team.createServerTeam(quizID, teamname, color, captainID, captainname);

				int players = context.getQuiz(quizID).getPlayers();
				ServerCreateTeamSuccesEvent sCTSE = new ServerCreateTeamSuccesEvent(quizID, teamID, teamname, color,
						captainID, captainname, players);
				sCTSE.addRecipient(captainID);
				// server.publishEvent(sCTSE);

				// Remove the captain from the list of unassigned players
				context.getQuiz(quizID).removeUnassignedPlayer(captainID);

				ServerNewTeamEvent sNTE = new ServerNewTeamEvent(quizID, teamID, teamname, color, captainID,
						captainname, players);
				sNTE.addRecipients(context.getUsersFromQuiz(quizID));
				server.publishEvent(sNTE);

				if (oldTeamID != -1) {
					context.changeTeam(quizID, oldTeamID, userID, 'd');
					String userName = context.getUserMap().get(userID).getUsername();
					ServerChangeTeamEvent sCHTE = new ServerChangeTeamEvent(quizID, -1, oldTeamID, userID, userName);
					sCHTE.addRecipients(context.getUsersFromQuiz(quizID));
					server.publishEvent(sCHTE);
				}
			}
		}

	}

	private static class ChangeTeamHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientChangeTeamEvent cCTE = (ClientChangeTeamEvent) event;

			int userID = cCTE.getUserID();
			int quizID = cCTE.getQuizID();
			int oldTeamID = cCTE.getOldTeamID();
			int newTeamID = cCTE.getNewTeamID();

			ServerContext context = ServerContext.getContext();
			String userName = context.changeTeam(quizID, newTeamID, userID, 'a');
			context.changeTeam(quizID, oldTeamID, userID, 'd');
			if (cCTE.getOldTeamID() == -1)
				// remove from unassinged list
				context.getQuizMap().get(quizID).removeUnassignedPlayer(userID);

			if (userName != null) {
				ServerChangeTeamEvent sCTE = new ServerChangeTeamEvent(quizID, newTeamID, oldTeamID, userID, userName);
				ArrayList<Integer> receivers = context.getUsersFromQuiz(cCTE.getQuizID());
				sCTE.addRecipients(receivers);
				server.publishEvent(sCTE);
			}
		}

	}

	private static class VoteHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientVoteEvent cVE = (ClientVoteEvent) event;

			int userID = cVE.getUserID();
			int quizID = cVE.getQuizID();
			int teamID = cVE.getTeamID();
			int vote = cVE.getVote();

			ServerContext context = ServerContext.getContext();
			Quiz quiz = context.getQuiz(quizID);
			quiz.addVote(userID, teamID, vote);

			ArrayList<Integer> receivers = new ArrayList<Integer>();
			receivers.addAll(context.getQuiz(quizID).getTeamMap().get(teamID).getPlayerMap().keySet());

			ServerVoteEvent sVE = new ServerVoteEvent(userID, teamID, quizID, vote);
			sVE.addRecipients(receivers);
			server.publishEvent(sVE);
		}

	}

	private static class AnswerHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientAnswerEvent cAE = (ClientAnswerEvent) event;

			int quizID = cAE.getQuizID();
			int teamID = cAE.getTeamID();
			int questionID = cAE.getQuestionID();
			int answer = cAE.getAnswer();

			ServerContext context = ServerContext.getContext();
			Quiz quiz = context.getQuiz(quizID);
			quiz.addAnswer(teamID, questionID, answer);
			int points = quiz.addPoints(teamID, questionID, cAE.getPixelSize(), answer);
			ArrayList<Integer> receivers = new ArrayList<>();
			receivers.addAll(context.getQuiz(quizID).getTeamMap().get(teamID).getPlayerMap().keySet());
			receivers.add(context.getQuiz(quizID).getHostID());

			int qType = ServerContext.getContext().getQuestionTypeMap().get(questionID);
			int correctAnswer = 0;
			if (qType == RoundType.IP.ordinal()) {
				IPQuestion iPQ = (IPQuestion) ServerContext.getContext().getQuestion(questionID);
				correctAnswer = iPQ.getCorrectAnswer();
			}
			else if (qType == RoundType.MC.ordinal()) {
				MCQuestion mCQ = (MCQuestion) ServerContext.getContext().getQuestion(questionID);
				correctAnswer = mCQ.getCorrectAnswer();
			}

			if (quiz.isAnsweredByAll()) {
				Timer pixelTimer = ServerContext.getContext().getQuizPixelTimerMap().get(quizID);
				if (pixelTimer != null) {
					pixelTimer.cancel();
					ServerContext.getContext().getQuizPixelTimerMap().put(quizID, null);
				}

				Timer durationTimer = ServerContext.getContext().getQuizTimerMap().get(quizID);
				if (durationTimer != null) {
					durationTimer.cancel();
					ServerContext.getContext().getQuizTimerMap().put(quizID, null);
				}
			}

			ServerVoteAnswerEvent sVAE = new ServerVoteAnswerEvent(teamID, questionID, answer, correctAnswer, points);
			sVAE.addRecipients(receivers);
			server.publishEvent(sVAE);
		}

	}

	private static class NewQuestionHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientNewQuestionEvent cNQE = (ClientNewQuestionEvent) event;

			int userID = cNQE.getUserID();
			int quizID = cNQE.getQuizID();
			
			if(cNQE.getQuestionID() == ServerContext.getContext().getQuiz(quizID).getRoundList().get(ServerContext.getContext().getQuiz(quizID).getCurrentRound()).getQuestionID()) {
				

				ServerContext context = ServerContext.getContext();
				Quiz quiz = context.getQuiz(quizID);
				if (quiz.isAnsweredByAll()) {
	
					ArrayList<Integer> receivers = context.getUsersFromQuiz(quizID);
	
					if ((quiz.getRoundList().get(quiz.getCurrentRound()).getCurrentQuestion() + 1) < quiz.getRoundList()
							.get(quiz.getCurrentRound()).getQuestions()) {
	
						Timer durationTimer = ServerContext.getContext().getQuizTimerMap().get(quizID);
						if (durationTimer != null) {
							durationTimer.cancel();
							ServerContext.getContext().getQuizTimerMap().put(quizID, null);
						}
	
						int questionID;
	
						switch (quiz.getRoundList().get(quiz.getCurrentRound()).getRoundType()) {
						case IP:
							Timer pixelTimer = ServerContext.getContext().getQuizPixelTimerMap().get(quizID);
							if (pixelTimer != null) {
								pixelTimer.cancel();
								ServerContext.getContext().getQuizPixelTimerMap().put(quizID, null);
							}
	
							IPQuestion newIPQ = (IPQuestion) context
									.getQuestion(quiz.getRoundList().get(quiz.getCurrentRound()).getNextQuestion());
	
							questionID = newIPQ.getQuestionID();
	
							ServerNewIPQuestionEvent sNIPQE = new ServerNewIPQuestionEvent(newIPQ.getQuestionID(),
									newIPQ.getBufferedImage(), newIPQ.getPixelSize(), newIPQ.getAnswers(),
									newIPQ.getCorrectAnswer());
							sNIPQE.addRecipients(receivers);
							server.publishEvent(sNIPQE);
	
							pixelTimer = new Timer();
							ServerContext.getContext().getQuizPixelTimerMap().put(quizID, pixelTimer);
							IPQuestionTimerTask iPQTT = new IPQuestionTimerTask(quizID, newIPQ.getQuestionID(),
									newIPQ.getPixelSize());
							pixelTimer.scheduleAtFixedRate(iPQTT, 0, 1000);
	
							break;
						case MC:
						default:
							MCQuestion newMCQ = (MCQuestion) context
									.getQuestion(quiz.getRoundList().get(quiz.getCurrentRound()).getNextQuestion());
	
							questionID = newMCQ.getQuestionID();
	
							ServerNewMCQuestionEvent sNMCQE = new ServerNewMCQuestionEvent(newMCQ.getQuestionID(),
									newMCQ.getQuestion(), newMCQ.getAnswers(), newMCQ.getCorrectAnswer());
							sNMCQE.addRecipients(receivers);
							server.publishEvent(sNMCQE);
							break;
						}
	
						durationTimer = new Timer();
						ServerContext.getContext().getQuizTimerMap().put(quizID, durationTimer);
						QuestionDurationTimerTask qDTT = new QuestionDurationTimerTask(quizID, questionID);
						durationTimer.scheduleAtFixedRate(qDTT, 0, 1000);
	
					} else {
						if ((quiz.getCurrentRound() + 1) < quiz.getRounds()) {
							ServerNewRoundEvent sNRE = new ServerNewRoundEvent(quiz.getCurrentRound() + 1);
							receivers = context.getUsersFromQuiz(cNQE.getQuizID());
							sNRE.addRecipients(receivers);
							server.publishEvent(sNRE);
						} else {
							ServerEndQuizEvent sEQE = new ServerEndQuizEvent();
							sEQE.addRecipients(context.getUsersFromQuiz(quiz.getQuizID()));
							server.publishEvent(sEQE);
						}
					}
				} else {
					ServerNotAllAnsweredEvent sNAEE = new ServerNotAllAnsweredEvent();
					sNAEE.addRecipient(userID);
					server.publishEvent(sNAEE);
				}
			}
		}
	}

	private static class CreateRoundHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientCreateRoundEvent cCRE = (ClientCreateRoundEvent) event;

			RoundType roundType = cCRE.getRoundType();
			int quizID = cCRE.getQuizID();
			Difficulty difficulty = cCRE.getDifficulty();
			Theme theme = cCRE.getTheme();
			int questions = cCRE.getQuestions();

			ServerContext context = ServerContext.getContext();
			Quiz quiz = context.getQuiz(quizID);
			quiz.addRound(roundType, theme, difficulty, questions);
			ArrayList<Integer> receivers = context.getUsersFromQuiz(quizID);

			ServerStartRoundEvent sSRE = new ServerStartRoundEvent(roundType);
			sSRE.addRecipients(receivers);
			server.publishEvent(sSRE);

			receivers.add(context.getQuiz(quizID).getHostID());

			Timer durationTimer = ServerContext.getContext().getQuizTimerMap().get(quizID);
			if (durationTimer != null) {
				durationTimer.cancel();
				ServerContext.getContext().getQuizTimerMap().put(quizID, null);
			}

			int questionID;

			switch (roundType) {
			case IP:
				Timer pixelTimer = ServerContext.getContext().getQuizPixelTimerMap().get(quizID);
				if (pixelTimer != null) {
					pixelTimer.cancel();
					ServerContext.getContext().getQuizPixelTimerMap().put(quizID, null);
				}

				IPQuestion newIPQ = (IPQuestion) context.getQuestion(quiz.getRoundList().get(quiz.getCurrentRound()).getNextQuestion());

				questionID = newIPQ.getQuestionID();

				ServerNewIPQuestionEvent sNIPQE = new ServerNewIPQuestionEvent(newIPQ.getQuestionID(),
						newIPQ.getBufferedImage(), newIPQ.getPixelSize(), newIPQ.getAnswers(),
						newIPQ.getCorrectAnswer());
				sNIPQE.addRecipients(receivers);
				server.publishEvent(sNIPQE);

				pixelTimer = ServerContext.getContext().getQuizPixelTimerMap().get(quizID);
				if (pixelTimer == null) {
					pixelTimer = new Timer();
					ServerContext.getContext().getQuizPixelTimerMap().put(quizID, pixelTimer);
				}
				IPQuestionTimerTask iPQTT = new IPQuestionTimerTask(quizID, newIPQ.getQuestionID(),
						newIPQ.getPixelSize());
				pixelTimer.scheduleAtFixedRate(iPQTT, 0, 1000);

				break;
			default:
				MCQuestion mCQuestion = (MCQuestion) context
						.getQuestion(quiz.getRoundList().get(quiz.getCurrentRound()).getNextQuestion());

				questionID = mCQuestion.getQuestionID();

				ServerNewMCQuestionEvent sNMCQE = new ServerNewMCQuestionEvent(mCQuestion.getQuestionID(),
						mCQuestion.getQuestion(), mCQuestion.getAnswers(), mCQuestion.getCorrectAnswer());
				sNMCQE.addRecipients(receivers);
				server.publishEvent(sNMCQE);
			}

			durationTimer = ServerContext.getContext().getQuizTimerMap().get(quizID);
			if (durationTimer == null) {
				durationTimer = new Timer();
				ServerContext.getContext().getQuizTimerMap().put(quizID, durationTimer);
			}
			QuestionDurationTimerTask qDTT = new QuestionDurationTimerTask(quizID, questionID);
			durationTimer.scheduleAtFixedRate(qDTT, 0, 1000);
		}

	}

	private static class ChatHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ChatMessage chatMessage = (ChatMessage) event;
			chatMessage.setType(ChatMessage.SERVERTYPE);
			ArrayList<Integer> destinations = new ArrayList<>();

			if (chatMessage.getReceiverType().equals("TEAM")) {
				Map<Integer, Team> listOfTeams = ServerContext.getContext().getQuiz(chatMessage.getQuizID())
						.getTeamMap();
				for (Map.Entry<Integer, Team> teamEntry : listOfTeams.entrySet())
					if (teamEntry.getValue().getPlayerMap().containsKey(chatMessage.getUserID()))
						for (Map.Entry<Integer, String> playerEntry : teamEntry.getValue().getPlayerMap().entrySet())
							destinations.add(playerEntry.getKey());
			} else if (chatMessage.getReceiverType().equals("ALL")) {
				destinations.addAll(ServerContext.getContext().getUsersFromQuiz(chatMessage.getQuizID()));
			}

			chatMessage.addRecipients(destinations);
			server.publishEvent(chatMessage);
		}

	}

	private static class ScoreboardDataHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientScoreboardDataEvent cSDE = (ClientScoreboardDataEvent) event;

			int quizID = cSDE.getQuizID();

			ServerScoreboardDataEvent sSDE = new ServerScoreboardDataEvent(quizID);
			Quiz quiz = ServerContext.getContext().getQuiz(quizID);
			for (Team team : quiz.getTeamMap().values())
				for (int userid : team.getPlayerMap().keySet())
					sSDE.addRecipient(userid);
			sSDE.addRecipient(quiz.getHostID());
			server.publishEvent(sSDE);
		}

	}

	private static class GetQuizzesHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientGetQuizzesEvent cGQE = (ClientGetQuizzesEvent) event;

			int userID = cGQE.getUserID();

			ServerGetQuizzesEvent sGQE = new ServerGetQuizzesEvent();
			sGQE.addRecipient(userID);
			server.publishEvent(sGQE);
		}

	}

	private static class DeleteTeamHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientDeleteTeamEvent cDTE = (ClientDeleteTeamEvent) event;

			ServerContext context = ServerContext.getContext();
			Quiz quiz = context.getQuizMap().get(cDTE.getQuizID());
			Team team = quiz.getTeamMap().get(cDTE.getTeamID());

			if (team != null) {
				for (Entry<Integer, String> entry : team.getPlayerMap().entrySet())
					quiz.addUnassignedPlayer(entry.getKey(), entry.getValue());

				quiz.removeTeam(team.getTeamID());
				ServerDeleteTeamEvent sDTE = new ServerDeleteTeamEvent(team.getTeamID());
				ArrayList<Integer> receivers = context.getUsersFromQuiz(quiz.getQuizID());
				sDTE.addRecipients(receivers);
				server.publishEvent(sDTE);
			}
		}
	}

	private static class LeaveQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientLeaveQuizEvent cLQE = (ClientLeaveQuizEvent) event;

			Server.playerLeavesQuiz(cLQE.getQuizID(), cLQE.getUserID(), cLQE.getTeamID());
		}

	}

	private static class HostReadyHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientHostReadyEvent cHRE = (ClientHostReadyEvent) event;

			int quizID = cHRE.getQuizID();

			ServerContext context = ServerContext.getContext();
			context.getQuizMap().get(quizID).setRunning(true);

			ServerStartQuizEvent sSQE = new ServerStartQuizEvent(quizID);
			sSQE.addRecipients(context.getUsersFromQuiz(quizID));

			context.getQuizMap().get(quizID).clearUnassignedPlayers();
			server.publishEvent(sSQE);
		}

	}

	private static class EndQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientEndQuizEvent cEQE = (ClientEndQuizEvent) event;

			int quizID = cEQE.getQuizID();

			ServerContext context = ServerContext.getContext();
			// Quiz has stopped
			context.getQuiz(quizID).setRunning(false);
			// Remove quiz from context
			context.getQuizMap().remove(quizID);
		}

	}

}