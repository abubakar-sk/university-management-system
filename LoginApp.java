package universitymanagementsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;

public class LoginApp extends Application {
    private final HashMap<String, User> users = new HashMap<>();

    // User types
    public enum UserType {
        ADMIN, FACULTY, STUDENT
    }

    // User class to store credentials and type
    private static class User {
        String password;
        UserType type;

        public User(String password, UserType type) {
            this.password = password;
            this.type = type;
        }
    }

    @Override
    public void start(Stage stage) {
        // Predefined users (passwords should ideally be hashed in real applications)
        users.put("Admin", new User("Admin1", UserType.ADMIN));
        users.put("faculty1", new User("teach123", UserType.FACULTY));
        users.put("user1", new User("password", UserType.STUDENT));

        // Add faculty from your Excel data
        users.put("turing", new User("default123", UserType.FACULTY));  // Dr. Alan Turing
        users.put("bronte", new User("default123", UserType.FACULTY));  // Prof. Emily Brontë
        users.put("hopper", new User("default123", UserType.FACULTY));  // Dr. Grace Hopper
        users.put("copeland", new User("default123", UserType.FACULTY)); // Dr. Lakyn Copeland
        users.put("gharabaghi", new User("default123", UserType.FACULTY)); // Albozr Gharabaghi

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

        stage.setTitle("University Login");
        stage.setScene(scene);
        stage.show();
    }

    private void handleLogin(Stage stage, String username, String password) {
        if (users.containsKey(username) && users.get(username).password.equals(password)) {
            UserType userType = users.get(username).type;
            UserDashboard.setUserType(userType); // Pass user type to dashboard
            UserDashboard.setCurrentUsername(username); // Pass username to dashboard
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