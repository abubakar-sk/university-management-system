package universitymanagementsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;

public class LoginApp extends Application {
    private final HashMap<String, String> users = new HashMap<>();
    private boolean isAdmin;

    @Override
    public void start(Stage stage) {
        // Predefined users (passwords should ideally be hashed in real applications)
        users.put("Admin", "Admin1");
        users.put("user1", "password");

        // UI Components
        Label usernameLabel = new Label("Enter Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Enter Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        // Error message alert box
        loginButton.setOnAction(e -> handleLogin(stage, usernameField.getText(), passwordField.getText()));

        VBox layout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, loginButton);
        Scene scene = new Scene(layout, 300, 250);

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    private void handleLogin(Stage stage, String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            isAdmin = "Admin".equals(username);
            UserDashboard.setIsAdmin(isAdmin); // Pass admin status
            navigateToDashboard(stage);
        } else {
            showAlert("Invalid Credentials", "The username or password you entered is incorrect.");
        }
    }

    private void navigateToDashboard(Stage stage) {
        try {
            new UserDashboard().start(new Stage());
            stage.close(); // Close login window
        } catch (Exception ex) {
            showAlert("Error", "Failed to open the dashboard. Please try again.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
