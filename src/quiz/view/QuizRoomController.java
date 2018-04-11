package quiz.view;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.util.Callback;
import quiz.model.*;
import main.*;
import user.model.*;


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

    private Quiz quiz;

    // TODO: change to real mainapp
    private MainQuizroom mainQuizRoom;
    private Main main;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public QuizRoomController() {
    }

    public QuizRoomController(Quiz quiz){
    	this.quiz=quiz;
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
            ObservableList<String> membernames=FXCollections.observableArrayList(team.getTeamMembers().values());
            teammemberslist.setItems(membernames);
        } else {
        	TeamnameLabel.setText("");
            CaptainLabel.setText("");
            teammemberslist.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    private void handleNewTeam() throws IOException{
    	Team team = new Team(new SimpleStringProperty(""),Color.rgb(0,0,100),Context.getContext().getUser().getID(),Context.getContext().getUser().getUsername(),quiz.getMaxAmountOfPlayersPerTeam());
    	boolean okClicked = mainQuizRoom.showNewTeam(team);
        if (okClicked) {
            quiz.addTeam(team);
            //TODO publish event to eventbroker, then delete previous line (is for the quizroomeventhandler)
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
