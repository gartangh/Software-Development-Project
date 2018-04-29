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
import eventbroker.serverevent.ServerVoteAnswerEvent;
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
import eventbroker.serverevent.ServerQuizNewPlayerEvent;
import eventbroker.serverevent.ServerCreateQuizEvent;
import eventbroker.serverevent.ServerScoreboardDataEvent;
import eventbroker.serverevent.ServerSendQuizEvent;
import eventbroker.serverevent.ServerStartQuizEvent;
import eventbroker.serverevent.ServerStartRoundEvent;
import eventbroker.serverevent.ServerVoteEvent;
import javafx.scene.paint.Color;
import main.Main;
import quiz.model.Team;
import quiz.util.Difficulty;
import quiz.util.Theme;
import user.User;
import network.Network;
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
	private static NewTeamHandler newTeamHandler = new NewTeamHandler();
	private static ChangeTeamHandler changeTeamHandler = new ChangeTeamHandler();
	private static HostReadyHandler hostReadyHandler = new HostReadyHandler();

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
		eventBroker.addEventListener(ChatMessage.EVENTTYPE, chatHandler);
		eventBroker.addEventListener(ClientScoreboardDataEvent.EVENTTYPE, scoreboardDataHandler);
		eventBroker.addEventListener(ClientGetQuizzesEvent.EVENTTYPE, getQuizzesHandler);
		eventBroker.addEventListener(ClientNewTeamEvent.EVENTTYPE, newTeamHandler);
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

			int userID = cCQE.getUserID();
			String quizname = cCQE.getQuizname();
			int maxAmountOfTeams = cCQE.getMaxAmountOfTeams();
			int maxAmountOfPlayersPerTeam = cCQE.getMaxAmountOfPlayersPerTeam();
			int maxAmountOfRounds = cCQE.getMaxAmountOfRounds();
			String hostname = cCQE.getHostname();

			ServerContext context = ServerContext.getContext();
			int quizID = context.addQuiz(quizname, maxAmountOfTeams, maxAmountOfPlayersPerTeam, maxAmountOfRounds, userID);

			ServerCreateQuizEvent sCQE = new ServerCreateQuizEvent(quizID, quizname, maxAmountOfTeams, maxAmountOfPlayersPerTeam, maxAmountOfRounds, userID, hostname);
			sCQE.addRecipient(userID);
			server.publishEvent(sCQE);

			ServerSendQuizEvent sSQE = new ServerSendQuizEvent(quizID, quizname, maxAmountOfTeams, maxAmountOfPlayersPerTeam, maxAmountOfRounds, userID, hostname);
			sSQE.addRecipients(context.getUserMap());
			server.publishEvent(sSQE);
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
			receivers.addAll(context.getQuiz(quizID).getTeams().get(teamID).getPlayers().keySet());

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
			receivers.addAll(context.getQuiz(quizID).getTeams().get(teamID).getPlayers().keySet());

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

				if ((quiz.getRound().getCurrentQuestion() + 1) < quiz.getRound().getQuestions()) {
					MCQuestion nQ = (MCQuestion) context.getQuestion(quiz.getRound().getNextQuestion());

					int[] permutatie = { 1, 2, 3, 4 };
					ServerNewQuestionEvent sNQE = new ServerNewQuestionEvent(nQ.getQuestionID(), nQ.getQuestion(),
							nQ.getAnswers(), permutatie);
					sNQE.addRecipients(receivers);
					server.publishEvent(sNQE);
				} else {
					if ((quiz.getCurrentRound() + 1) < quiz.getMaxAmountOfRounds()) {
						ServerNewRoundEvent sNRE = new ServerNewRoundEvent(quiz.getCurrentRound() + 1);
						receivers = context.getUsersFromQuiz(cNQE.getQuizID());
						receivers.add(context.getQuiz(quizID).getHostID());
						sNRE.addRecipients(receivers);
						server.publishEvent(sNRE);
					} else {
						// TODO: Trigger end of quiz
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
			// TODO Old team (check for null) and newteam modifier
			ClientCreateRoundEvent cCRE = (ClientCreateRoundEvent) event;

			int quizID = cCRE.getQuizID();
			Difficulty difficulty = cCRE.getDifficulty();
			Theme theme = cCRE.getTheme();
			int numberOfQuestions = cCRE.getNumberOfQuestions();

			ServerContext context = ServerContext.getContext();
			Quiz quiz = context.getQuiz(quizID);
			quiz.addRound(difficulty, theme, numberOfQuestions);
			ArrayList<Integer> receivers = context.getUsersFromQuiz(quizID);

			MCQuestion nQ = (MCQuestion) context.getQuestion(quiz.getRound().getNextQuestion());
			int[] permutatie = { 1, 2, 3, 4 };

			ServerNewQuestionEvent sNQE = new ServerNewQuestionEvent(nQ.getQuestionID(), nQ.getQuestion(),
					nQ.getAnswers(), permutatie);
			sNQE.addRecipients(receivers);
			server.publishEvent(sNQE);

			ServerStartRoundEvent sSRE = new ServerStartRoundEvent();
			sSRE.addRecipients(receivers);
			server.publishEvent(sSRE);
		}

	}

	private static class ChatHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ChatMessage chatMessage = (ChatMessage) event;

			ArrayList<Integer> destinations = new ArrayList<>();

			// TODO Add all usernames for the chat
			for (Map.Entry<Integer, Integer> entry : ServerContext.getContext().getNetwork().getUserIDConnectionIDMap()
					.entrySet())
				if (entry.getKey() != chatMessage.getUserID())
					destinations.add(entry.getKey());

			chatMessage.setType(ChatMessage.EVENTTYPESERVER);
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

	private static class NewTeamHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ClientNewTeamEvent cNTE = (ClientNewTeamEvent) event;

			int userID = cNTE.getUserID();
			int quizID = cNTE.getQuizID();
			String teamname = cNTE.getTeamname();
			Color color = cNTE.getColor();

			ServerContext context = ServerContext.getContext();
			int newTeamID = context.addTeam(quizID, teamname, color, userID);
			if (newTeamID != -1) {
				Team newteam = context.getQuiz(quizID).getTeams().get(newTeamID);

				ServerNewTeamEvent serverNewTeamEvent = new ServerNewTeamEvent(quizID, newTeamID, newteam.getTeamName(),
						newteam.getColor(), newteam.getCaptainID(), newteam.getPlayers().get(newteam.getCaptainID()));
				ArrayList<Integer> receivers = context.getUsersFromQuiz(quizID);
				context.getQuizMap().get(cNTE.getQuizID()).removeUnassignedPlayer(newteam.getCaptainID());
				serverNewTeamEvent.addRecipients(receivers);
				server.publishEvent(serverNewTeamEvent);
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