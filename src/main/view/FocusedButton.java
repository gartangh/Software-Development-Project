package main.view;

public class FocusedButton extends javafx.scene.control.Button {

	public FocusedButton() {
		setFocus();
	}

	public FocusedButton(String text) {
		super(text);
		setFocus();
	}

	private void setFocus() {
		defaultButtonProperty().bind(focusedProperty());
	}

}