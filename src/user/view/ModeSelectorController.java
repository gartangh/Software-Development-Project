package user.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.Context;
import main.Main;

public class ModeSelectorController {

	@FXML
	private Button mHost;
	@FXML
	private Button mGuest;

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}
	
	@FXML
	private void initialize() {
		// Empty initialize
	}
	
	@FXML
	private void handleHost() {
		Context.getContext().getUser().castToHost();
		// TODO: Change scene
	}
	
	@FXML
	private void handleGuest() {
		Context.getContext().getUser().castToGuest();
		// TODO: Change scene
	}

}
