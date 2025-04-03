package universitymanagementsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.util.HashMap;

public class LoginApp extends Application {
    private final HashMap<String, String> userCredentials = new HashMap<>();
    private final HashMap<String, String> userRoles = new HashMap<>();

    @Override
    public void start(Stage stage) {
        initializeCredentials();

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (authenticate(username, password)) {
                String role = userRoles.get(username);
                // Set user data before opening dashboard
                UserDashboard.setUserData(username, role);
                navigateToDashboard(stage);
            } else {
                showAlert("Login Failed", "Invalid username or password");
            }
        });

        VBox layout = new VBox(10, usernameLabel, usernameField,
                passwordLabel, passwordField, loginButton);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 300, 250));
        stage.setTitle("University Login");
        stage.show();
    }

    private void initializeCredentials() {
        // Admin
        userCredentials.put("admin", "admin123");
        userRoles.put("admin", "ADMIN");

        // Students
        userCredentials.put("student1", "student123");
        userRoles.put("student1", "STUDENT");

        // Faculty
        userCredentials.put("turing", "default123");
        userRoles.put("turing", "FACULTY");
        userCredentials.put("bronte", "default123");
        userRoles.put("bronte", "FACULTY");
        userCredentials.put("copeland", "default123");
        userRoles.put("copeland", "FACULTY");
        userCredentials.put("gharabaghi", "default123");
        userRoles.put("gharabaghi", "FACULTY");
    }

    private boolean authenticate(String username, String password) {
        return userCredentials.containsKey(username) &&
                userCredentials.get(username).equals(password);
    }

    private void navigateToDashboard(Stage stage) {
        try {
            new UserDashboard().start(new Stage());
            stage.close();
        } catch (Exception e) {
            showAlert("Error", "Failed to open dashboard");
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