package universitymanagementsystem;

import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        // Start with the login screen
        new LoginApp().start(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}


