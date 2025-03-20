package universitymanagementsystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserDashboard extends Application {
    private static boolean isAdmin;

    // Setter method for isAdmin
    public static void setIsAdmin(boolean isAdmin) {
        UserDashboard.isAdmin = isAdmin;
    }

    @Override
    public void start(Stage stage) {
        // Create main layout
        BorderPane layout = new BorderPane();
        VBox menu = setupMenu(stage);
        layout.setLeft(menu);

        // Display the dashboard view
        Label dashboardContent = new Label("Welcome to the User Dashboard!");
        dashboardContent.setPadding(new Insets(20));
        layout.setCenter(dashboardContent);

        // Setup the scene
        Scene scene = new Scene(layout, 600, 400);
        stage.setTitle("User Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private VBox setupMenu(Stage stage) {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(10));
        Label title = new Label(isAdmin ? "Admin Menu" : "User Menu");
        menu.getChildren().add(title);

        // Dynamically populate menu items based on role
        if (isAdmin) {
            addMenuButton("Manage Subjects", new SubjectManagement(isAdmin), stage, menu);
            addMenuButton("Manage Courses", new CourseManagement(isAdmin), stage, menu);
            addMenuButton("Manage Students", new StudentManagement(isAdmin), stage, menu);
            addMenuButton("Manage Faculty", new FacultyManagement(isAdmin, false), stage, menu);
            addMenuButton("Manage Events", new EventManagement(isAdmin), stage, menu);
        } else {
            addMenuButton("View Subjects", new SubjectManagement(isAdmin), stage, menu);
            addMenuButton("View Courses", new CourseManagement(isAdmin), stage, menu);
            addMenuButton("View Events", new EventManagement(isAdmin), stage, menu);
        }

        // Logout button (common to both roles)
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout(stage));
        menu.getChildren().add(logoutButton);

        return menu;
    }

    private void addMenuButton(String label, Application module, Stage currentStage, VBox menu) {
        Button button = new Button(label);
        button.setOnAction(e -> openModule(module, currentStage));
        menu.getChildren().add(button);
    }

    private void openModule(Application module, Stage currentStage) {
        try {
            module.start(new Stage());
            currentStage.close();
        } catch (Exception ex) {
            System.err.println("Error opening module: " + ex.getMessage());
            showAlert("Navigation Error", "Failed to open the module. Please try again.");
        }
    }

    private void logout(Stage stage) {
        try {
            new LoginApp().start(new Stage());
            stage.close();
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            showAlert("Logout Error", "Failed to return to the login screen. Please restart the application.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        // Example: Set user role (true for Admin, false for regular User)
        setIsAdmin(true);
        launch(args);
    }
}
