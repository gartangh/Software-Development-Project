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
import eventbroker.clientevent.ClientCreateTeamEvent;
import eventbroker.serverevent.ServerVoteAnswerEvent;
import eventbroker.serverevent.ServerChangeTeamEvent;
import eventbroker.serverevent.ServerCreateAccountFailEvent;
import eventbroker.serverevent.ServerCreateAccountSuccesEvent;
import eventbroker.serverevent.ServerCreateQuizFailEvent;
import eventbroker.serverevent.ServerEndQuizEvent;
import eventbroker.serverevent.ServerGetQuizzesEvent;
import eventbroker.serverevent.ServerJoinQuizEvent;
import eventbroker.serverevent.ServerLogInFailEvent;
import eventbroker.serverevent.ServerLogInSuccesEvent;
import eventbroker.serverevent.ServerNewIPQuestionEvent;
import eventbroker.serverevent.ServerNewMCQuestionEvent;
import eventbroker.serverevent.ServerNewRoundEvent;
import eventbroker.serverevent.ServerCreateTeamEvent;
import eventbroker.serverevent.ServerNotAllAnsweredEvent;
import eventbroker.serverevent.ServerQuizNewPlayerEvent;
import eventbroker.serverevent.ServerCreateQuizSuccesEvent;
import eventbroker.serverevent.ServerScoreboardDataEvent;
import eventbroker.serverevent.ServerSendQuizEvent;
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
import network.Network;
import quiz.model.IPQuestion;
import quiz.model.MCQuestion;
import quiz.model.Quiz;

public class Server extends EventPublisher {

	// Singleton
	private final static Server server = new Server();

	private static CreateAccountHandler createAccountHandler = new CreateAccountHandler();
	private static LogInHandler logInHandler = new LogInHandler();
	private static JoinQuizHandler joinQuizHandler = new JoinQuizHandler();
	private static CreateQuizHandler createQuizHandler = new CreateQuizHandler();
	private static VoteHandler voteHandler = new VoteHandler();
	private static AnswerHandler answerHandler = new AnswerHandler();
	private static NewQuestionHandler newQuestionHandler = new NewQuestionHandler();
	private static CreateRoundHandler createRoundHandler = new CreateRoundHandler();
	private static ChatHandler chatHandler = new ChatHandler();
	private static ScoreboardDataHandler scoreboardDataHandler = new ScoreboardDataHandler();
	private static GetQuizzesHandler getQuizzesHandler = new GetQuizzesHandler();
	private static CreateTeamHandler newTeamHandler = new CreateTeamHandler();
	private static ChangeTeamHandler changeTeamHandler = new ChangeTeamHandler();
	private static HostReadyHandler hostReadyHandler = new HostReadyHandler();

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

		// Start the EventBroker
		eventBroker.start();

		System.out.println("Server running ...");
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
			} else {
				// Username and password are correct
				ServerContext.getContext().getNetwork().getUserIDConnectionIDMap().put(userID, connectionID);

				ServerLogInSuccesEvent sLISE = new ServerLogInSuccesEvent(userID, username, password, level, xp);
				sLISE.addRecipient(userID);
				server.publishEvent(sLISE);
			}
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
			for (Entry<Integer, Quiz> quiz : context.getQuizMap().entrySet()) {
				if (quiz.getValue().getQuizname().equals(quizname)) {
					exists = true;
					break;
				}
			}

			if (exists) {
				// Quizname is already in use
				ServerCreateQuizFailEvent sCQFE = new ServerCreateQuizFailEvent();
				server.publishEvent(sCQFE);
			} else {
				// Quizname is not yet in use
				int userID = cCQE.getUserID();
				int teams = cCQE.getMaxAmountOfTeams();
				int players = cCQE.getMaxAmountOfPlayersPerTeam();
				int rounds = cCQE.getMaxAmountOfRounds();
				String hostname = cCQE.getHostname();

				int quizID = Quiz.createServerQuiz(quizname, rounds, teams, players, userID, hostname);

				ServerCreateQuizSuccesEvent sCQSE = new ServerCreateQuizSuccesEvent(quizID, quizname, teams, players,
						rounds, userID, hostname);
				sCQSE.addRecipient(userID);
				server.publishEvent(sCQSE);

				ServerSendQuizEvent sSQE = new ServerSendQuizEvent(quizID, quizname, teams, players, rounds, userID,
						hostname);
				sSQE.addRecipients(context.getUserMap());
				server.publishEvent(sSQE);
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
			quiz.addPoints(teamID, questionID, answer);
			ArrayList<Integer> receivers = new ArrayList<>();
			receivers.addAll(context.getQuiz(quizID).getTeamMap().get(teamID).getPlayerMap().keySet());

			MCQuestion mCQ = (MCQuestion) context.getQuestion(questionID);
			ServerVoteAnswerEvent serverAnswer = new ServerVoteAnswerEvent(teamID, questionID, answer,
					mCQ.getCorrectAnswer());
			serverAnswer.addRecipients(receivers);
			server.publishEvent(serverAnswer);
		}

	}

	private static class NewQuestionHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientNewQuestionEvent cNQE = (ClientNewQuestionEvent) event;

			int userID = cNQE.getUserID();
			int quizID = cNQE.getQuizID();

			ServerContext context = ServerContext.getContext();
			Quiz quiz = context.getQuiz(quizID);
			if (quiz.isAnsweredByAll()) {
				ArrayList<Integer> receivers = context.getUsersFromQuiz(quizID);

				if ((quiz.getRoundList().get(quiz.getCurrentRound()).getCurrentQuestion() + 1) < quiz.getRoundList()
						.get(quiz.getCurrentRound()).getQuestions()) {
					MCQuestion nQ = (MCQuestion) context
							.getQuestion(quiz.getRoundList().get(quiz.getCurrentRound()).getNextQuestion());

					ServerNewMCQuestionEvent sNQE = new ServerNewMCQuestionEvent(nQ.getQuestionID(), nQ.getQuestion(),
							nQ.getAnswers());
					sNQE.addRecipients(receivers);
					server.publishEvent(sNQE);
				} else {
					if ((quiz.getCurrentRound() + 1) < quiz.getRounds()) {
						ServerNewRoundEvent sNRE = new ServerNewRoundEvent(quiz.getCurrentRound() + 1);
						receivers = context.getUsersFromQuiz(cNQE.getQuizID());
						receivers.add(context.getQuiz(quizID).getHostID());
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

			switch (roundType) {
			case IP:
				IPQuestion iPQuestion = (IPQuestion) context
						.getQuestion(quiz.getRoundList().get(quiz.getCurrentRound()).getNextQuestion());

				ServerNewIPQuestionEvent sNIPQE = new ServerNewIPQuestionEvent(iPQuestion.getQuestionID(),
						iPQuestion.getQuestion(), iPQuestion.getAnswers());
				sNIPQE.addRecipients(receivers);
				server.publishEvent(sNIPQE);

			default:
				MCQuestion mCQuestion = (MCQuestion) context
						.getQuestion(quiz.getRoundList().get(quiz.getCurrentRound()).getNextQuestion());

				ServerNewMCQuestionEvent sNMCQE = new ServerNewMCQuestionEvent(mCQuestion.getQuestionID(),
						mCQuestion.getQuestion(), mCQuestion.getAnswers());
				sNMCQE.addRecipients(receivers);
				server.publishEvent(sNMCQE);
			}

			ServerStartRoundEvent sSRE = new ServerStartRoundEvent();
			sSRE.addRecipients(receivers);
			server.publishEvent(sSRE);
		}

	}

	private static class ChatHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ChatMessage chatMessage = (ChatMessage) event;
			chatMessage.setType(ChatMessage.SERVERTYPE);
			ArrayList<Integer> destinations = new ArrayList<>();
			for (Map.Entry<Integer, Integer> entry : ServerContext.getContext().getNetwork().getUserIDConnectionIDMap()
					.entrySet())
				destinations.add(entry.getKey());
			chatMessage.addRecipients(destinations);
			server.publishEvent(chatMessage);
		}

	}

	private static class ScoreboardDataHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientScoreboardDataEvent cSDE = (ClientScoreboardDataEvent) event;

			int userID = cSDE.getUserID();
			int quizID = cSDE.getQuizID();

			ServerScoreboardDataEvent sSDE = new ServerScoreboardDataEvent(quizID);
			sSDE.addRecipient(userID);
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

	private static class CreateTeamHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientCreateTeamEvent cCTE = (ClientCreateTeamEvent) event;

			int userID = cCTE.getUserID();
			int quizID = cCTE.getQuizID();
			String teamname = cCTE.getTeamname();
			Color color = cCTE.getColor();

			ServerContext context = ServerContext.getContext();
			int newTeamID = context.addTeam(quizID, teamname, color, userID);
			if (newTeamID != -1) {
				Team newteam = context.getQuiz(quizID).getTeamMap().get(newTeamID);

				ServerCreateTeamEvent sCTE = new ServerCreateTeamEvent(quizID, newTeamID, newteam.getTeamname(),
						newteam.getColor(), newteam.getCaptainID(), newteam.getPlayerMap().get(newteam.getCaptainID()));
				ArrayList<Integer> receivers = context.getUsersFromQuiz(quizID);
				context.getQuizMap().get(cCTE.getQuizID()).removeUnassignedPlayer(newteam.getCaptainID());
				sCTE.addRecipients(receivers);
				server.publishEvent(sCTE);
			} else
				System.out.println("newTeamID != -1");
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

	private static class HostReadyHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientHostReadyEvent cHRE = (ClientHostReadyEvent) event;

			int quizID = cHRE.getQuizID();

			ServerContext context = ServerContext.getContext();
			context.getQuizMap().get(quizID).setRunning(true);
			context.getQuizMap().get(quizID).clearUnassignedPlayers();

			ServerStartQuizEvent sSQE = new ServerStartQuizEvent(quizID);
			ArrayList<Integer> receivers = new ArrayList<>();
			receivers.addAll(context.getUserMap().keySet());
			sSQE.addRecipients(receivers);
			server.publishEvent(sSQE);
		}

	}

}