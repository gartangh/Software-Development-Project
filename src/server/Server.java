package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

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
import eventbroker.clientevent.ClientLogInEvent;
import eventbroker.clientevent.ClientNewQuestionEvent;
import eventbroker.clientevent.ClientScoreboardDataEvent;
import eventbroker.clientevent.ClientVoteEvent;
import eventbroker.clientevent.ClientNewTeamEvent;
import eventbroker.serverevent.ServerAnswerEvent;
import eventbroker.serverevent.ServerChangeTeamEvent;
import eventbroker.serverevent.ServerCreateAccountFailEvent;
import eventbroker.serverevent.ServerCreateAccountSuccesEvent;
import eventbroker.serverevent.ServerEndQuizEvent;
import eventbroker.serverevent.ServerGetQuizzesEvent;
import eventbroker.serverevent.ServerJoinQuizEvent;
import eventbroker.serverevent.ServerLogInFailEvent;
import eventbroker.serverevent.ServerLogInSuccesEvent;
import eventbroker.serverevent.ServerNewQuestionEvent;
import eventbroker.serverevent.ServerNewRoundEvent;
import eventbroker.serverevent.ServerNewTeamEvent;
import eventbroker.serverevent.ServerNotAllAnsweredEvent;
import eventbroker.serverevent.ServerQuizNewPlayer;
import eventbroker.serverevent.ServerReturnQuizEvent;
import eventbroker.serverevent.ServerScoreboardDataEvent;
import eventbroker.serverevent.ServerSendQuizEvent;
import eventbroker.serverevent.ServerStartQuizEvent;
import eventbroker.serverevent.ServerStartRoundEvent;
import eventbroker.serverevent.ServerVoteEvent;
import main.Main;
import user.model.User;
import quiz.model.Team;
import network.Network;
import quiz.model.MCQuestion;
import quiz.model.Quiz;

public class Server extends EventPublisher {

	// Singleton
	private final static Server server = new Server();
	// TODO: Add handlers
	private static ServerHandler serverHandler = new ServerHandler();
	private static CreateAccountHandler createAccountHandler = new CreateAccountHandler();
	private static LogInHandler logInHandler = new LogInHandler();

	public static void main(String[] args) {
		// Create a network on port Main.SERVERPORT and type SERVER
		Network network = new Network(Main.SERVERPORT, "SERVER");
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
		EventBroker.getEventBroker().addEventListener(network);

		// TODO: Add handlers
		EventBroker.getEventBroker().addEventListener(serverHandler);
		EventBroker.getEventBroker().addEventListener(ClientCreateAccountEvent.EVENTTYPE, createAccountHandler);
		EventBroker.getEventBroker().addEventListener(ClientLogInEvent.EVENTTYPE, logInHandler);

		// Start the EventBroker
		EventBroker.getEventBroker().start();

		System.out.println("Server running ...");
	}

	public static Server getServer() {
		return server;
	}

	// Inner classes
	private static class ServerHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ArrayList<Integer> destinations = new ArrayList<>();
			Boolean handled = false;
			switch (event.getType()) {
			case "CLIENT_JOIN_QUIZ":
				ClientJoinQuizEvent cJTE = (ClientJoinQuizEvent) event;

				ServerContext.getContext().getQuizMap().get(cJTE.getQuizID()).addUnassignedPlayer(cJTE.getUserID(),
						cJTE.getUserName());
				ServerJoinQuizEvent sJQE = new ServerJoinQuizEvent(
						ServerContext.getContext().getQuizMap().get(cJTE.getQuizID()));
				sJQE.addRecipient(cJTE.getUserID());
				server.publishEvent(sJQE);
				ServerQuizNewPlayer sQNP = new ServerQuizNewPlayer(cJTE.getUserID(),
						ServerContext.getContext().getUserMap().get(cJTE.getUserID()).getUsername());
				sQNP.addRecipients(ServerContext.getContext().getUsersFromQuiz(cJTE.getQuizID()));
				server.publishEvent(sQNP);
				break;

			case "CLIENT_CREATE_QUIZ":
				ClientCreateQuizEvent cCQE = (ClientCreateQuizEvent) event;
				int quizID = ServerContext.getContext().addQuiz(cCQE.getQuizName(), cCQE.getMaxAmountOfTeams(),
						cCQE.getMaxAmountOfPlayersPerTeam(), cCQE.getMaxAmountOfRounds(),
						cCQE.getMaxAmountOfQuestionsPerRound(), cCQE.getUserID());
				Quiz quiz = ServerContext.getContext().getQuizMap().get(quizID);

				ServerReturnQuizEvent sRQE = new ServerReturnQuizEvent(quiz);
				sRQE.addRecipient(cCQE.getUserID());
				server.publishEvent(sRQE);

				ServerSendQuizEvent sSQE = new ServerSendQuizEvent(quiz);
				sSQE.addRecipients(ServerContext.getContext().getUserMap());
				server.publishEvent(sSQE);
				handled = true;
				break;

			case "CLIENT_VOTE":
				handleClientVoteEvent((ClientVoteEvent) event);
				handled = true;
				break;

			case "CLIENT_ANSWER":
				handleClientAnswerEvent((ClientAnswerEvent) event);
				handled = true;
				break;

			case "CLIENT_NEW_QUESTION":
				handleClientNewQuestionEvent((ClientNewQuestionEvent) event);
				handled = true;
				break;

			case "CLIENT_CHAT":
				ChatMessage chatMessage = (ChatMessage) event;

				// TODO: Add all usernames for the chat

				for (Map.Entry<Integer, Integer> entry : ServerContext.getContext().getNetwork()
						.getUserIDConnectionIDMap().entrySet())
					if (entry.getKey() != chatMessage.getUserID())
						destinations.add(entry.getKey());

				chatMessage.setType("SERVER_CHAT");
				chatMessage.addRecipients(destinations);
				server.publishEvent(chatMessage);
				handled = true;
				break;

			case "CLIENT_SCOREBOARDDATA":
				ClientScoreboardDataEvent cSDE = (ClientScoreboardDataEvent) event;
				ServerScoreboardDataEvent scoreboardData = new ServerScoreboardDataEvent(cSDE.getQuizID());
				scoreboardData.addRecipient(cSDE.getUserID());
				server.publishEvent(scoreboardData);
				handled = true;
				break;

			case "CLIENT_GET_QUIZZES":
				ClientGetQuizzesEvent cGQE = (ClientGetQuizzesEvent) event;
				ServerGetQuizzesEvent sGQE = new ServerGetQuizzesEvent();
				sGQE.addRecipient(cGQE.getUserID());
				server.publishEvent(sGQE);
				handled = true;
				break;

			case "CLIENT_NEW_TEAM":
				ClientNewTeamEvent newteamevent = (ClientNewTeamEvent) event;
				int newTeamID = ServerContext.getContext().addTeam(newteamevent.getQuizID(), newteamevent.getTeamName(),
						newteamevent.getColor(), newteamevent.getUserID());
				if (newTeamID != -1) {
					Team newteam = ServerContext.getContext().getQuiz(newteamevent.getQuizID()).getTeams()
							.get(newTeamID);
					ServerNewTeamEvent serverNewTeamEvent = new ServerNewTeamEvent(newteamevent.getQuizID(), newTeamID,
							newteam.getTeamName(), newteam.getColor(), newteam.getCaptainID(),
							newteam.getPlayers().get(newteam.getCaptainID()));
					ArrayList<Integer> receivers = ServerContext.getContext()
							.getUsersFromQuiz(newteamevent.getQuizID());
					ServerContext.getContext().getQuizMap().get(newteamevent.getQuizID())
							.removeUnassignedPlayer(newteam.getCaptainID());
					serverNewTeamEvent.addRecipients(receivers);
					server.publishEvent(serverNewTeamEvent);
				} else
					System.out.println("newTeamID != -1");

				handled = true;
				break;

			case "CLIENT_CHANGE_TEAM":
				ClientChangeTeamEvent cte = (ClientChangeTeamEvent) event;
				String userName = ServerContext.getContext().changeTeam(cte.getQuizID(), cte.getNewTeamID(),
						cte.getUserID(), 'a');
				ServerContext.getContext().changeTeam(cte.getQuizID(), cte.getOldTeamID(), cte.getUserID(), 'd');
				if (cte.getOldTeamID() == -1) {// remove from unassinged list
					ServerContext.getContext().getQuizMap().get(cte.getQuizID())
							.removeUnassignedPlayer(cte.getUserID());
				}
				if (userName != null) {
					ServerChangeTeamEvent serverChangeTeamEvent = new ServerChangeTeamEvent(cte.getQuizID(),
							cte.getNewTeamID(), cte.getOldTeamID(), cte.getUserID(), userName);
					ArrayList<Integer> receivers = ServerContext.getContext().getUsersFromQuiz(cte.getQuizID());
					serverChangeTeamEvent.addRecipients(receivers);
					server.publishEvent(serverChangeTeamEvent);
				}
				handled = true;
				break;

			case "CLIENT_CREATE_ROUND":
				// TODO: Old team (check for null) and newteam modifier
				handleClientCreateRoundEvent((ClientCreateRoundEvent) event);
				handled = true;
				break;
			case "CLIENT_HOST_READY":
				ClientHostReadyEvent chre = (ClientHostReadyEvent) event;
				ServerContext.getContext().getQuizMap().get(chre.getQuizID()).setRunning(true);
				ServerContext.getContext().getQuizMap().get(chre.getQuizID()).clearUnassignedPlayers();
				ServerStartQuizEvent serverStartQuizEvent = new ServerStartQuizEvent(chre.getQuizID());
				ArrayList<Integer> receivers = new ArrayList<Integer>();
				receivers.addAll(ServerContext.getContext().getUserMap().keySet());
				serverStartQuizEvent.addRecipients(receivers);
				server.publishEvent(serverStartQuizEvent);
				break;
			}

			if (handled)
				System.out.println("Event received and handled: " + event.getType());
			else
				System.out.println("Event received but left unhandled: " + event.getType());
		}

		public void handleClientVoteEvent(ClientVoteEvent cVE) {
			Quiz quiz = ServerContext.getContext().getQuiz(cVE.getQuizID());
			quiz.addVote(cVE.getUserID(), cVE.getTeamID(), cVE.getVote());

			ArrayList<Integer> receivers = new ArrayList<Integer>();
			receivers.addAll(ServerContext.getContext().getQuiz(cVE.getQuizID()).getTeams().get(cVE.getTeamID())
					.getPlayers().keySet());

			ServerVoteEvent sVE = new ServerVoteEvent(cVE.getUserID(), cVE.getTeamID(), cVE.getQuizID(), cVE.getVote());
			sVE.addRecipients(receivers);
			server.publishEvent(sVE);
		}

		public void handleClientAnswerEvent(ClientAnswerEvent cAE) {
			Quiz quiz = ServerContext.getContext().getQuiz(cAE.getQuizID());
			quiz.addAnswer(cAE.getTeamID(), cAE.getQuestionID(), cAE.getAnswer());
			quiz.addPoints(cAE.getTeamID(), cAE.getQuestionID(), cAE.getAnswer());
			ArrayList<Integer> receivers = new ArrayList<Integer>();
			receivers.addAll(ServerContext.getContext().getQuiz(cAE.getQuizID()).getTeams().get(cAE.getTeamID())
					.getPlayers().keySet());

			MCQuestion mCQ = (MCQuestion) ServerContext.getContext().getQuestion(cAE.getQuestionID());
			ServerAnswerEvent serverAnswer = new ServerAnswerEvent(cAE.getTeamID(), cAE.getQuestionID(),
					cAE.getAnswer(), mCQ.getCorrectAnswer());
			serverAnswer.addRecipients(receivers);
			server.publishEvent(serverAnswer);
		}

		public void handleClientNewQuestionEvent(ClientNewQuestionEvent cNQE) {
			Quiz quiz = ServerContext.getContext().getQuiz(cNQE.getQuizID());
			if (quiz.isAnsweredByAll()) {
				ArrayList<Integer> receivers = ServerContext.getContext().getUsersFromQuiz(cNQE.getQuizID());

				if ((quiz.getRound().getQuestionNumber() + 1) < quiz.getRound().getNumberOfQuestions()) {
					MCQuestion nQ = (MCQuestion) ServerContext.getContext()
							.getQuestion(quiz.getRound().getNextQuestion());
					int[] permutatie = { 1, 2, 3, 4 };
					ServerNewQuestionEvent sNQE = new ServerNewQuestionEvent(nQ.getQuestionID(), nQ.getQuestion(),
							nQ.getAnswers(), permutatie);
					sNQE.addRecipients(receivers);
					Server.getServer().publishEvent(sNQE);
				} else {
					if ((quiz.getCurrentRound() + 1) < quiz.getMaxAmountOfRounds()) {
						ServerNewRoundEvent sNRE = new ServerNewRoundEvent(quiz.getCurrentRound() + 1);
						receivers = ServerContext.getContext().getUsersFromQuiz(cNQE.getQuizID());
						receivers.add(ServerContext.getContext().getQuiz(cNQE.getQuizID()).getQuizmaster());
						sNRE.addRecipients(receivers);
						Server.getServer().publishEvent(sNRE);
					} else {
						// TODO: trigger end quiz
						ServerEndQuizEvent sEQE = new ServerEndQuizEvent();
						sEQE.addRecipients(ServerContext.getContext().getUsersFromQuiz(quiz.getQuizID()));
						server.publishEvent(sEQE);
					}
				}
			} else {
				ServerNotAllAnsweredEvent sNAEE = new ServerNotAllAnsweredEvent();
				sNAEE.addRecipient(cNQE.getUserID());
				server.publishEvent(sNAEE);
			}
		}

		public void handleClientCreateRoundEvent(ClientCreateRoundEvent cCRE) {
			Quiz quiz = ServerContext.getContext().getQuiz(cCRE.getQuizID());
			quiz.addRound(cCRE.getDiff(), cCRE.getTheme());
			// quiz.getRound().addQuestions(cCRE.getNumberOfQuestions());
			ArrayList<Integer> receivers = ServerContext.getContext().getUsersFromQuiz(cCRE.getQuizID());

			MCQuestion nQ = (MCQuestion) ServerContext.getContext().getQuestion(quiz.getRound().getNextQuestion());
			int[] permutatie = { 1, 2, 3, 4 };

			ServerNewQuestionEvent sNQE = new ServerNewQuestionEvent(nQ.getQuestionID(), nQ.getQuestion(),
					nQ.getAnswers(), permutatie);
			sNQE.addRecipients(receivers);
			server.publishEvent(sNQE);

			ServerStartRoundEvent sSRE = new ServerStartRoundEvent();
			sSRE.addRecipients(receivers);
			Server.getServer().publishEvent(sSRE);
		}
	}

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

			int userID = -1;
			int level = 0;
			long xp = 0L;
			for (Entry<Integer, User> user : ServerContext.getContext().getUserMap().entrySet()) {
				if (user.getValue().getUsername().equals(username) && user.getValue().getPassword().equals(password)) {
					userID = user.getKey();
					level = user.getValue().getLevel();
					xp = user.getValue().getXp();
					break;
				}
			}

			if (userID == -1) {
				// Username and / or password are incorrect
				ServerLogInFailEvent sLIFE = new ServerLogInFailEvent();
				sLIFE.addRecipient(userID);
				server.publishEvent(sLIFE);
			} else {
				// Username and password are correct
				// TODO: connectionID = 0 in stead of the correct connectionID?
				ServerContext.getContext().getNetwork().getUserIDConnectionIDMap().put(userID, cLIE.getConnectionID());

				ServerLogInSuccesEvent sLISE = new ServerLogInSuccesEvent(userID, username, password, level, xp);
				sLISE.addRecipient(userID);
				server.publishEvent(sLISE);
			}
		}

	}

}