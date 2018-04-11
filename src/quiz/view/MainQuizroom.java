package quiz.view;


import java.io.IOException;
import java.net.InetAddress;

import eventbroker.EventBroker;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import quiz.model.Quiz;
import quiz.model.Team;
import quiz.util.NewTeamEvent;
import user.model.*;
import main.*;
import network.Network;


public class MainQuizroom extends Application{
	private Stage primaryStage;
	private Quizroom quizroom;
	private AnchorPane testRoot;
	private Network network;

	@Override
	public void start(Stage primaryStage) throws IOException{
		EventBroker.getEventBroker().start();

		this.primaryStage=primaryStage;
		this.primaryStage.setTitle("Quiz");

		//testcode
		//User.logIn("Hannes","1234");
		User user1=new User(1,"hannes","1234");
		user1=User.createUser(user1);
		Quiz quiz=new Quiz(1,5,5,5,5,20);
		Context.getContext().setQuiz(quiz);
		Team team1 = new Team(new SimpleStringProperty("Deborah leemans"),Color.rgb(0,0,255),2,"james",quiz.getMaxAmountOfPlayersPerTeam());
		team1.addTeamMember(4,"Hendrik");
		team1.addTeamMember(5,"Natalie");
		Team team2 = new Team(new SimpleStringProperty("Team2"),Color.rgb(255,0,0),4,"Precious",quiz.getMaxAmountOfPlayersPerTeam());
		quiz.addTeam(team1);
		quiz.addTeam(team2);

		quizroom=new Quizroom(quiz,this);

		network = new Network(1024); //voorlopig vast
		EventBroker.getEventBroker().addEventListener(network);
		InetAddress a = InetAddress.getLocalHost();
		network.connect(a,1025);

		showQuizroom();
	}


	public void showQuizroom(){

        Scene scene = new Scene(quizroom.getContent());
        primaryStage.setScene(scene);
        primaryStage.show();


	}

	public boolean showNewTeam(Team team) throws IOException{
        try {
     			FXMLLoader loader = new FXMLLoader();
     	        loader.setLocation(MainQuizroom.class.getResource("NewTeam.fxml"));
     	        AnchorPane newteam = (AnchorPane) loader.load();

     	        Stage dialogStage = new Stage();
     	        dialogStage.setTitle("New Team");
     	        dialogStage.initModality(Modality.WINDOW_MODAL);
     	        dialogStage.initOwner(primaryStage);
     	        Scene scene = new Scene(newteam);
     	        dialogStage.setScene(scene);

                 // Set the person into the controller.
                 NewTeamController controller = loader.getController();
                 controller.setDialogStage(dialogStage);
                 controller.setTeam(team);

                 // Show the dialog and wait until the user closes it
                 dialogStage.showAndWait();

                 return controller.isOkClicked();
             } catch (IOException e) {
                 e.printStackTrace();
                 return false;
             }

	}

	public boolean showNewTeam(NewTeamEvent teamevent) throws IOException{
        try {
     			FXMLLoader loader = new FXMLLoader();
     	        loader.setLocation(MainQuizroom.class.getResource("NewTeam.fxml"));
     	        AnchorPane newteam = (AnchorPane) loader.load();

     	        Stage dialogStage = new Stage();
     	        dialogStage.setTitle("New Team");
     	        dialogStage.initModality(Modality.WINDOW_MODAL);
     	        dialogStage.initOwner(primaryStage);
     	        Scene scene = new Scene(newteam);
     	        dialogStage.setScene(scene);

                 // Set the person into the controller.
                 NewTeamController controller = loader.getController();
                 controller.setDialogStage(dialogStage);
                 controller.setTeamEvent(teamevent);

                 // Show the dialog and wait until the user closes it
                 dialogStage.showAndWait();

                 return controller.isOkClicked();
             } catch (IOException e) {
                 e.printStackTrace();
                 return false;
             }

	}

	public static void main(String[] args) {
		launch(args);
	}
}
