package quiz.view;


import java.io.IOException;


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
import user.model.*;
import main.*;


public class MainQuizroom extends Application{
	private Stage primaryStage;
	private Quizroom quizroom;
	private AnchorPane testRoot;

	@Override
	public void start(Stage primaryStage) throws IOException{
		this.primaryStage=primaryStage;
		this.primaryStage.setTitle("Quiz");
		//User.logIn("Hannes","1234");
		User user1=new User("hannes","1234");
		User user2=new User("Anse","dqd");
		User user3=new User("kendrik", "lamar");
		User user4=new User("inlist", "1154");
		Host host=new Host(user1);
		Player player1=new Player(user2.castToGuest());
		Player player2=new Player(user3.castToGuest());
		Player player3=new Player(user4.castToGuest());
		Quiz quiz=new Quiz(5,5,5,5,5,host);


		Team team1 = new Team(new SimpleStringProperty("Deborah leemans"),Color.rgb(0,0,255),player1,quiz.getMaxAmountOfPlayersPerTeam());
		team1.addMember(player3);
		team1.addMember(player2);
		Team team2 = new Team(new SimpleStringProperty("Team2"),Color.rgb(0,0,255),player2,quiz.getMaxAmountOfPlayersPerTeam());
		quiz.addTeam(team1);
		quiz.addTeam(team2);
		quizroom=new Quizroom(quiz,this);
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
	public static void main(String[] args) {
		launch(args);
	}
}
