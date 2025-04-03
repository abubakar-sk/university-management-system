package universitymanagementsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {

    // Reference to the welcome message label in the UI
    @FXML
    private Label welcomeMessageLabel;

    /**
     * Handles the "Hello" button click event.
     * Sets a welcome message on the label.
     */
    @FXML
    protected void handleHelloButtonClick() {
        if (welcomeMessageLabel != null) {
            welcomeMessageLabel.setText("Welcome to the University Management System!");
        } else {
            System.err.println("Error: welcomeMessageLabel is not initialized.");
        }
    }
}
