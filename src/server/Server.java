package server;

import java.util.ArrayList;
import java.util.Map;

import chat.ChatMessage;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import quiz.util.ChangeTeamEvent;
import quiz.util.NewTeamEvent;
import quiz.model.Team;
import network.Network;
import quiz.model.MCQuestion;
import quiz.util.ClientCreateAccountEvent;
import quiz.util.ClientCreateQuizEvent;
import quiz.util.ClientCreateRoundEvent;
import quiz.util.ClientGetQuizzesEvent;
import quiz.util.ClientHostReadyEvent;
import quiz.util.ClientJoinQuizEvent;
import quiz.util.ClientVoteEvent;
import quiz.util.QuizzerEvent;
import quiz.model.Quiz;
import quiz.util.ClientAnswerEvent;
import quiz.util.ClientNewQuestionEvent;

public class Server extends EventPublisher {

	// Singleton
	private final static Server server = new Server();
	private final static int SERVERPORT = 1025;
	private static ServerHandler serverHandler = new ServerHandler();

	public static void main(String[] args) {
		Network network = new Network(SERVERPORT, "SERVER");
		ServerContext.getContext().setNetwork(network);

		EventBroker.getEventBroker().addEventListener(network);
		EventBroker.getEventBroker().addEventListener(serverHandler);
		EventBroker.getEventBroker().start();

		System.out.println("Server running ...");
	}

	public static Server getServer() {
		return server;
	}

	// Inner class
	static class ServerHandler implements EventListener {

		@Override
		public void handleEvent(Event e) {
			ArrayList<Integer> destinations = new ArrayList<>();
			Boolean handled = false;
			switch (e.getType()) {
			case "CLIENT_JOIN_QUIZ":
				ClientJoinQuizEvent cjte = (ClientJoinQuizEvent) e;
				ServerContext.getContext().getQuizMap().get(cjte.getQuizID()).addUnassignedPlayer(cjte.getUserID(),
						cjte.getUserName());
				break;
			case "CLIENT_CREATE_ACCOUNT":
				ClientCreateAccountEvent cCAE = (ClientCreateAccountEvent) e;
				int userID = ServerContext.getContext().addUser(cCAE.getUserName(), cCAE.getUserPassword());
				ServerContext.getContext().getNetwork().getUserIDConnectionIDMap().put(userID, cCAE.getConnectionID());
				ServerReturnUserIDEvent sRUIDE = new ServerReturnUserIDEvent(userID, cCAE.getConnectionID());
				sRUIDE.addRecipient(userID);
				server.publishEvent(sRUIDE);
				handled = true;
				break;

			case "CLIENT_CREATE_QUIZ":
				ClientCreateQuizEvent cCQE = (ClientCreateQuizEvent) e;
				int quizID = ServerContext.getContext().addQuiz(cCQE.getQuizName(), cCQE.getMaxAmountOfTeams(),
						cCQE.getMaxAmountOfPlayersPerTeam(), cCQE.getMaxAmountOfRounds(),
						cCQE.getMaxAmountOfQuestionsPerRound(), cCQE.getUserID());
				Quiz quiz = ServerContext.getContext().getQuizMap().get(quizID);
				ServerReturnQuizEvent sRQE = new ServerReturnQuizEvent(quiz);
				sRQE.addRecipient(cCQE.getUserID());
				server.publishEvent(sRQE);
				handled = true;
				break;

			case "CLIENT_VOTE":
				handleClientVoteEvent((ClientVoteEvent) e);
				handled = true;
				break;

			case "CLIENT_ANSWER":
				handleClientAnswerEvent((ClientAnswerEvent) e);
				handled = true;
				break;

			case "CLIENT_NEW_QUESTION":
				handleClientNewQuestionEvent((ClientNewQuestionEvent) e);
				handled = true;
				break;

			case "CLIENT_CHAT":
				ChatMessage chatMessage = (ChatMessage) e;

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
				QuizzerEvent askForScoreboardData = (QuizzerEvent) e;
				ServerScoreboardDataEvent scoreboardData = new ServerScoreboardDataEvent(
						askForScoreboardData.getQuizID());
				scoreboardData.addRecipient(askForScoreboardData.getUserID());
				server.publishEvent(scoreboardData);
				handled = true;
				break;

			case "CLIENT_GET_QUIZZES":
				ClientGetQuizzesEvent cGQE = (ClientGetQuizzesEvent) e;
				ServerGetQuizzesEvent sGQE = new ServerGetQuizzesEvent();
				sGQE.addRecipient(cGQE.getUserID());
				server.publishEvent(sGQE);
				handled = true;
				break;

			case "CLIENT_NEW_TEAM":
				NewTeamEvent newteamevent = (NewTeamEvent) e;
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
				ChangeTeamEvent cte = (ChangeTeamEvent) e;
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
					ArrayList<Integer> receivers=ServerContext.getContext().getUsersFromQuiz(cte.getQuizID());
					serverChangeTeamEvent.addRecipients(receivers);
					server.publishEvent(serverChangeTeamEvent);
				}
				handled = true;
				break;

			case "CLIENT_CREATE_ROUND":
				// TODO: Old team (check for null) and newteam modifier
				handleClientCreateRoundEvent((ClientCreateRoundEvent) e);
				handled = true;
				break;
			case "CLIENT_HOST_READY":
				ClientHostReadyEvent chre=(ClientHostReadyEvent) e;
				ArrayList<Integer> receivers=ServerContext.getContext().getUsersFromQuiz(chre.getQuizID());
				ServerStartQuizEvent serverStartQuizEvent=new ServerStartQuizEvent(chre.getQuizID());
				serverStartQuizEvent.addRecipients(receivers);
				server.publishEvent(serverStartQuizEvent);
				break;
			}


			if (handled)
				System.out.println("Event received and handled: " + e.getType());
			else
				System.out.println("Event received but left unhandled: " + e.getType());
		}

		public void handleClientVoteEvent(ClientVoteEvent cVE) {
			Quiz quiz = ServerContext.getContext().getQuiz(cVE.getQuizID());
			quiz.addVote(cVE.getUserID(), cVE.getTeamID(), cVE.getVote());

			ServerVoteEvent sVE = new ServerVoteEvent(cVE.getUserID(), cVE.getTeamID(), cVE.getQuizID(), cVE.getVote());
			server.publishEvent(sVE);
		}

		public void handleClientAnswerEvent(ClientAnswerEvent cAE) {
			Quiz quiz = ServerContext.getContext().getQuiz(cAE.getQuizID());
			quiz.addAnswer(cAE.getTeamID(), cAE.getQuestionID(), cAE.getAnswer());

			MCQuestion mCQ = (MCQuestion) ServerContext.getContext().getQuestion(cAE.getQuestionID());
			ServerAnswerEvent serverAnswer = new ServerAnswerEvent(cAE.getTeamID(), cAE.getQuestionID(),
					cAE.getAnswer(), mCQ.getCorrectAnswer());
			server.publishEvent(serverAnswer);
		}

		public void handleClientNewQuestionEvent(ClientNewQuestionEvent cNQE) {
			Quiz quiz = ServerContext.getContext().getQuiz(cNQE.getQuizID());

			if (quiz.getRound().getQuestionNumber() < quiz.getRound().getNumberOfQuestions()) {
				MCQuestion nQ = (MCQuestion) ServerContext.getContext().getQuestion(quiz.getRound().getNextQuestion());
				int[] permutatie = { 1, 2, 3, 4 };
				ServerNewQuestionEvent sNQE = new ServerNewQuestionEvent(nQ.getQuestionID(), nQ.getQuestion(),
						nQ.getAnswers(), permutatie);
				Server.getServer().publishEvent(sNQE);
			} else {
				if (quiz.getCurrentRound() < quiz.getMaxAmountOfRounds()) {
					// TODO: Trigger create round + players wait
				} else {
					// TODO: Trigger end quiz
				}
			}

		}

		public void handleClientCreateRoundEvent(ClientCreateRoundEvent cCRE) {
			Quiz quiz = ServerContext.getContext().getQuiz(cCRE.getQuizID());
			quiz.addRound(cCRE.getDiff(), cCRE.getTheme());
			quiz.getRound().addQuestions(cCRE.getNumberOfQuestions());

			MCQuestion nQ = (MCQuestion) ServerContext.getContext().getQuestion(quiz.getRound().getNextQuestion());
			int[] permutatie = { 1, 2, 3, 4 };

			ServerNewQuestionEvent sNQE = new ServerNewQuestionEvent(nQ.getQuestionID(), nQ.getQuestion(),
					nQ.getAnswers(), permutatie);

			server.publishEvent(sNQE);

			ServerStartRoundEvent sSRE = new ServerStartRoundEvent();
			// TODO: Add all players of quiz
			Server.getServer().publishEvent(sSRE);
		}
	}

}