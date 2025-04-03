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
    private static LoginApp.UserType userType;
    private static String currentUsername;

    // Setter methods
    public static void setUserType(LoginApp.UserType type) {
        UserDashboard.userType = type;
    }

    public static void setCurrentUsername(String username) {
        UserDashboard.currentUsername = username;
    }

    @Override
    public void start(Stage stage) {
        // Create main layout
        BorderPane layout = new BorderPane();
        VBox menu = setupMenu(stage);
        layout.setLeft(menu);

        // Display the dashboard view with personalized welcome
        String welcomeMessage = String.format("Welcome %s!\nYou are logged in as: %s",
                currentUsername,
                userType.toString().charAt(0) + userType.toString().substring(1).toLowerCase());

        Label dashboardContent = new Label(welcomeMessage);
        dashboardContent.setPadding(new Insets(20));
        dashboardContent.setStyle("-fx-font-size: 16px;");
        layout.setCenter(dashboardContent);

        // Setup the scene
        Scene scene = new Scene(layout, 800, 600);
        stage.setTitle("University Dashboard - " + currentUsername);
        stage.setScene(scene);
        stage.show();
    }

    private VBox setupMenu(Stage stage) {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));
        menu.setStyle("-fx-background-color: #f0f0f0;");

        String menuTitle = "";
        switch (userType) {
            case ADMIN:
                menuTitle = "Admin Menu";
                break;
            case FACULTY:
                menuTitle = "Faculty Menu";
                break;
            case STUDENT:
                menuTitle = "Student Menu";
                break;
        }

        Label title = new Label(menuTitle);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        menu.getChildren().add(title);

        // Common buttons for all users
        addMenuButton("View Events", new EventManagement(userType == LoginApp.UserType.ADMIN, currentUsername), stage, menu);

        // Role-specific buttons
        switch (userType) {
            case ADMIN:
                addMenuButton("Manage Subjects", new SubjectManagement(true), stage, menu);
                addMenuButton("Manage Courses", new CourseManagement(true), stage, menu);
                addMenuButton("Manage Students", new StudentManagement(true), stage, menu);
                addMenuButton("Manage Faculty", new FacultyManagement(true, false), stage, menu);
                break;

            case FACULTY:
                addMenuButton("My Courses", new FacultyManagement(false, true), stage, menu);
                addMenuButton("View Students", new StudentManagement(false), stage, menu);
                break;

            case STUDENT:
                addMenuButton("View Subjects", new SubjectManagement(false), stage, menu);
                addMenuButton("View Courses", new CourseManagement(false), stage, menu);
                addMenuButton("My Profile", new StudentProfile(currentUsername), stage, menu);
                break;
        }

        // Logout button (common to all roles)
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-base: #ff6b6b;");
        logoutButton.setOnAction(e -> logout(stage));
        menu.getChildren().add(logoutButton);

        return menu;
    }

    private void addMenuButton(String label, Application module, Stage currentStage, VBox menu) {
        Button button = new Button(label);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-alignment: center-left;");
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
        // For testing purposes only
        setUserType(LoginApp.UserType.ADMIN);
        setCurrentUsername("Admin");
        launch(args);
    }
}
