package quiz.view;

import java.io.IOException;

import eventbroker.EventBroker;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import main.Main;
import quiz.model.*;

public class Quizroom {
	AnchorPane content;
	Quiz quizmodel;
	QuizRoomController quizcontroller;


	public Quizroom(Quiz quiz, Main main) throws IOException{
		createQuizroom(quiz, main);
	}

	public void createQuizroom(Quiz quiz, Main main) throws IOException{

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Quizroom.class.getResource("Quizroom.fxml"));
        content = (AnchorPane) loader.load();
        quizcontroller = loader.getController();
        quizcontroller.setMain(main);
        quizcontroller.setQuiz(quiz);
        quizcontroller.addListener();


	}

	public AnchorPane getContent(){
		return content;
	}

//test functions
	public Quizroom(Quiz quiz, MainQuizroom main) throws IOException{
		createQuizroom(quiz, main);
	}


	public void createQuizroom(Quiz quiz, MainQuizroom main) throws IOException{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Quizroom.class.getResource("Quizroom.fxml"));
        content = (AnchorPane) loader.load();
        quizcontroller = loader.getController();
        quizcontroller.setMain(main);
        quizcontroller.setQuiz(quiz);
        quizcontroller.addListener();

	}




}
