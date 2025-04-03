package universitymanagementsystem;

import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        // Start the application with the Login screen
        initializeLoginScreen(stage);
    }

    /**
     * Initializes and displays the Login screen.
     * This method is designed for better scalability, making it easier
     * to switch or update the initial screen in the future.
     */
    private void initializeLoginScreen(Stage stage) {
        new LoginApp().start(stage);
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
