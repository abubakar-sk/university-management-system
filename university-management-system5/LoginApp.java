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
        // Predefined users (Username -> Password)
        users.put("Admin", "Admin1");
        users.put("user1", "password");

        // UI Components
        Label label = new Label("Enter Username:");
        TextField usernameField = new TextField();
        Label passLabel = new Label("Enter Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button manageStudentsBtn = new Button("Manage Students");
        Label message = new Label();

        // Show Student Management only if the user is an admin
        manageStudentsBtn.setVisible(false);

        // Login button action: Validate credentials
        loginButton.setOnAction(_ -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (users.containsKey(username) && users.get(username).equals(password)) {
                isAdmin = "Admin".equals(username);
                manageStudentsBtn.setVisible(isAdmin);

                if (isAdmin) {
                    new UserDashboard(isAdmin).start(new Stage()); // Open UserDashboard with role
                } else {
                    new UserDashboard(isAdmin).start(new Stage()); // Open dashboard for other users
                }

                stage.close(); // Close Login Window
            } else {
                message.setText("Invalid Credentials!"); // Show error
            }
        });

        VBox layout = new VBox(10, label, usernameField, passLabel, passwordField, loginButton, manageStudentsBtn, message);
        Scene scene = new Scene(layout, 300, 250);

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
