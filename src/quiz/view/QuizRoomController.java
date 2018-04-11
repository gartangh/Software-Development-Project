package quiz.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import quiz.model.*;
import quiz.util.ClientVoteEvent;
import quiz.util.NewTeamEvent;
import main.*;
import user.model.*;
import server.*;


import java.io.IOException;

import eventbroker.*;

public class QuizRoomController extends EventPublisher{
	@FXML
    private TableView<Team> teamTable;
    @FXML
    private TableColumn<Team, String> NameColumn;
    @FXML
    private Label TeamnameLabel;
    @FXML
    private Label CaptainLabel;
    @FXML
    private ListView<String> teammemberslist;
    @FXML
    private Circle circle;

    private Quiz quiz;

    // TODO: change to real mainapp
    private MainQuizroom mainQuizRoom;
    private Main main;
    private QuizroomHandler quizroomhandler=new QuizroomHandler();

	public class QuizroomHandler implements EventListener{
		public void handleEvent(Event e){
			if (e!=null){
			switch (e.getType()){
				case "SERVER_NEW_TEAM":
					ServerNewTeamEvent newTeamEvent=(ServerNewTeamEvent) e;
					if (newTeamEvent.getQuizID()==Context.getContext().getQuiz().getID()) {//extra controle
						Team newTeam=new Team(newTeamEvent.getQuizID(),new SimpleStringProperty(newTeamEvent.getTeamName()),newTeamEvent.getColor(),newTeamEvent.getCaptainID(),newTeamEvent.getCaptainName(),quiz.getMaxAmountOfPlayersPerTeam());
						quiz.addTeam(newTeam);//TAbleview vanzelf geupdatet via bindings
						if (newTeam.getCaptainID()==Context.getContext().getUser().getID()){// i am the captain,change Team in context
							Context.getContext().setTeam(newTeam);
						}
					}
				}
			}
			else System.out.println("received_null_event");
		}
	}

    public QuizRoomController() {
    }

    public QuizRoomController(Quiz quiz){
    	this.quiz=quiz;
    }

    public void addListener(){
    	EventBroker.getEventBroker().addEventListener(quizroomhandler);
    }

    @FXML
    private void initialize() {
        NameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        showTeamDetails(null);


        teamTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTeamDetails(newValue));
    }

    public void setMain(MainQuizroom testQuizroom) {
        this.mainQuizRoom = testQuizroom;
    }

    public void setMain(Main main) {
        this.main=main;
    }

    public void setQuiz(Quiz quiz){
    	this.quiz=quiz;
    	teamTable.setItems(quiz.getTeams());
    }

    public void showTeamDetails(Team team){
        if (team != null) {
            // Fill the labels with info from the person object.
        	TeamnameLabel.setText(team.getName());
            CaptainLabel.setText(team.getTeamMembers().get(team.getCaptainID()));
            circle.setFill(team.getColor());
            ObservableList<String> membernames=FXCollections.observableArrayList(team.getTeamMembers().values());
            teammemberslist.setItems(membernames);
        } else {
        	TeamnameLabel.setText("");
            CaptainLabel.setText("");
            circle.setFill(Color.TRANSPARENT);
            teammemberslist.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    private void handleNewTeam() throws IOException{
    	/*Team team = new Team(new SimpleStringProperty(""),Color.rgb(0,0,100),Context.getContext().getUser().getID(),Context.getContext().getUser().getUsername(),quiz.getMaxAmountOfPlayersPerTeam());
    	boolean okClicked = mainQuizRoom.showNewTeam(team);
        if (okClicked) {
            quiz.addTeam(team);
            //TODO publish event to eventbroker, then delete previous line (is for the quizroomeventhandler)
        }*/
    	NewTeamEvent teamevent = new NewTeamEvent(Context.getContext().getQuiz().getID(),"",Color.TRANSPARENT);
    	boolean okClicked = mainQuizRoom.showNewTeam(teamevent);
        if (okClicked) {
        	publishEvent(teamevent);
        	System.out.println(teamevent.getTeamName());
        }

    }

    @FXML
    private void handleReady(){

    }

    @FXML
    private void handleJoin(){
    	Team selectedTeam = teamTable.getSelectionModel().getSelectedItem();
    	if (selectedTeam!=null){
    		User currUser=Context.getContext().getUser();
    		if (selectedTeam.getTeamMembers().get(currUser.getID())==null){//user nog niet in dit team
    			selectedTeam.getTeamMembers().put(currUser.getID(),currUser.getUsername());
    			showTeamDetails(selectedTeam);
    			//TODO error handling, get out of other team, check if team not full (error report)
    			//naar quizroomlistener brengen en via eventbroker!! (anders dubbel werk), eerst genoeg checks doen!
    		}
    	}
    }
}
