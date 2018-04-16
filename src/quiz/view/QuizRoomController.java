package quiz.view;

import javafx.application.Platform;
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
import quiz.util.ChangeTeamEvent;
import quiz.util.ClientVoteEvent;
import quiz.util.NewTeamEvent;
import main.*;
import user.model.*;
import server.*;


import java.io.IOException;

import eventbroker.*;

public class QuizRoomController extends EventPublisher{
	@FXML
    private TableView<TeamNameID> teamTable;
    @FXML
    private TableColumn<TeamNameID, String> NameColumn;
    @FXML
    private Label TeamnameLabel;
    @FXML
    private Label CaptainLabel;
    @FXML
    private ListView<String> teammemberslist;
    @FXML
    private Circle circle;


    // TODO: change to real mainapp
    private MainQuizroom mainQuizRoom;
    private Main main;
    private QuizroomHandler quizroomhandler=new QuizroomHandler();
    private QuizRoomModel quizRoomModel=new QuizRoomModel();

	public class QuizroomHandler implements EventListener{
		public void handleEvent(Event e){
			if (e!=null){
			switch (e.getType()){
				case "SERVER_NEW_TEAM":
					ServerNewTeamEvent newTeamEvent=(ServerNewTeamEvent) e;
					if (newTeamEvent.getQuizID()==Context.getContext().getQuiz().getQuizID()) {//extra controle
						Team newTeam=new Team(newTeamEvent.getTeamID(),newTeamEvent.getTeamName(),newTeamEvent.getColor(),newTeamEvent.getCaptainID(),newTeamEvent.getCaptainName());
						Context.getContext().getQuiz().addTeam(newTeam);//TAbleview vanzelf geupdatet via bindings
						if (newTeam.getCaptainID()==Context.getContext().getUser().getID()){// i am the captain,change Team in context
							Context.getContext().setTeamID(newTeam.getTeamID());
						}
						quizRoomModel.updateTeams();
						quizRoomModel.updateTeamDetail(newTeam.getTeamID());
					}
				break;
				case "SERVER_CHANGE_TEAM":
					ServerChangeTeamEvent cte=(ServerChangeTeamEvent) e;
					if (cte.getQuizID()==Context.getContext().getQuiz().getQuizID()){
						int newteamID=cte.getNewTeamID();
						int oldteamID=cte.getOldTeamID();
						int userID=cte.getUserID();
						String userName=cte.getUserName();
						Team newteam=null;
						Team oldteam=null;

						newteam=Context.getContext().getQuiz().getTeams().get(newteamID);
						oldteam=Context.getContext().getQuiz().getTeams().get(oldteamID);

						if (newteam!=null){//should always happen
							newteam.addPlayer(userID,userName);
							if (Context.getContext().getUser().getID()==userID){
									Context.getContext().setTeamID(newteam.getTeamID());
							}
							quizRoomModel.updateTeamDetail(newteam.getTeamID());
						}
						if (oldteam!=null){
							oldteam.removePlayer(userID);
						}
					}
					break;
				}
			}
			else System.out.println("received_null_event");
		}
	}

    public QuizRoomController() {
    }

    public QuizRoomController(Quiz quiz){
    	//this.quiz=quiz;
    	//via serverContext rechtstreeks? wordt nu meegegeven via argument maar hoeft niet
    }

    public void addListener(){
    	EventBroker.getEventBroker().addEventListener(quizroomhandler);
    }

    @FXML
    private void initialize() {
        NameColumn.setCellValueFactory(cellData -> cellData.getValue().getTeamName());
        teamTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTeamDetails(newValue));
        showTeamDetails(null);

        CaptainLabel.textProperty().bind(quizRoomModel.getCaptainName());
        TeamnameLabel.textProperty().bind(quizRoomModel.getTeamName());
        teammemberslist.itemsProperty().bind(quizRoomModel.getTeamMembers());


        /*showTeamDetails(null);


       teamTable.getSelectionModel().selectedItemProperty().addListener(
               (observable, oldValue, newValue) -> showTeamDetails(newValue));*/

    }

    public void setMain(MainQuizroom testQuizroom) {
        this.mainQuizRoom = testQuizroom;
        teamTable.setItems(quizRoomModel.getTeams());
    }

    public void setMain(Main main) {
        this.main=main;
    }

    public void showTeamDetails(TeamNameID team){
    	if (team != null){
    		quizRoomModel.updateTeamDetail(team.getTeamID());
    	}
    	else {
    		quizRoomModel=new QuizRoomModel();
    		quizRoomModel.updateTeams();
    	}
    }

    @FXML
    private void handleNewTeam() throws IOException{
    	if (Context.getContext().getQuiz().getAmountOfTeams()<Context.getContext().getQuiz().getMaxAmountOfTeams()){
	    	NewTeamEvent teamevent = new NewTeamEvent(Context.getContext().getQuiz().getQuizID(),"",Color.TRANSPARENT);
	    	boolean okClicked = mainQuizRoom.showNewTeam(teamevent);
	        if (okClicked) {
	        	publishEvent(teamevent);
	        	System.out.println(teamevent.getTeamName());
	        }
    	}

    }

    @FXML
    private void handleReady(){

    }

    @FXML
    private void handleJoin(){
    	TeamNameID selectedTeam = teamTable.getSelectionModel().getSelectedItem();
    	if (selectedTeam!=null){
    		User currUser=Context.getContext().getUser();
    		int currTeamID=Context.getContext().getTeamID();
    		int currCaptainID;

    		if (currTeamID !=-1){
    			currCaptainID=Context.getContext().getQuiz().getTeams().get(currTeamID).getCaptainID();
    		}
    		else {
    			currCaptainID=-1;
    		}

    		if (selectedTeam.getTeamID()!=Context.getContext().getTeamID() && currCaptainID != currUser.getID()){//user nog niet in dit team en user is geen captain (userID never -1)
    			ChangeTeamEvent changeTeamEvent=new ChangeTeamEvent(Context.getContext().getQuiz().getQuizID(),selectedTeam.getTeamID(),currTeamID,currUser.getID());
    			publishEvent(changeTeamEvent);
    			/*selectedTeam.getTeamMembers().put(currUser.getID(),currUser.getUsername());
    			showTeamDetails(selectedTeam);*/
    			//TODO error handling,
    		}
    	}
    }
}
