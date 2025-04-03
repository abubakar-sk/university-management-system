package universitymanagementsystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class UserDashboard extends Application {
    private static String currentUser;
    private static String userRole;
    private static boolean isAdmin;

    public static void setUserData(String username, String role) {
        currentUser = username;
        userRole = role;
        isAdmin = "ADMIN".equals(role);
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        VBox menu = createMenu(stage);

        // Header
        Label welcomeLabel = new Label("Welcome, " + currentUser + " (" + userRole + ")");
        welcomeLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        HBox header = new HBox(welcomeLabel);
        header.setPadding(new Insets(15));
        root.setTop(header);

        root.setLeft(menu);
        root.setCenter(new Label("Select an option from the menu"));

        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("University Dashboard - " + currentUser);
        stage.show();
    }

    private VBox createMenu(Stage currentStage) {
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));
        menu.setStyle("-fx-background-color: #f0f0f0;");

        Label menuTitle = new Label(isAdmin ? "Admin Menu" : userRole + " Menu");
        menuTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        menu.getChildren().add(menuTitle);

        // Common buttons for all users
        addMenuButton("View Events", new EventManagement(isAdmin, currentUser), currentStage, menu);
        // Role-specific buttons
        if (isAdmin) {
            addMenuButton("Manage Subjects", new SubjectManagement(isAdmin), currentStage, menu);
            addMenuButton("Manage Courses", new CourseManagement(isAdmin), currentStage, menu);
            addMenuButton("Manage Students", new StudentManagement(isAdmin, false), currentStage, menu);
            addMenuButton("Manage Faculty", new FacultyManagement(isAdmin, false), currentStage, menu);
        }
        else if ("FACULTY".equals(userRole)) {
            addMenuButton("Student Information", new StudentManagement(isAdmin, true), currentStage, menu);
            addMenuButton("Faculty Profile", new FacultyManagement(isAdmin, true), currentStage, menu);
            addMenuButton("Manage Courses", new CourseManagement(isAdmin), currentStage, menu);
        }
        else {
            addMenuButton("Dashboard", new StudentManagement(isAdmin, false), currentStage, menu);
            addMenuButton("Faculty", new FacultyManagement(isAdmin, false), currentStage, menu);
            addMenuButton("View Subjects", new SubjectManagement(isAdmin), currentStage, menu);
            addMenuButton("View Courses", new CourseManagement(isAdmin), currentStage, menu);
        }

        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout(currentStage));
        menu.getChildren().add(logoutButton);

        return menu;
    }

    private void addMenuButton(String label, Application module, Stage currentStage, VBox menu) {
        Button button = new Button(label);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(e -> {
            if (module != null) {
                try {
                    module.start(new Stage());
                    currentStage.close();
                } catch (Exception ex) {
                    showAlert("Error", "Could not open " + label);
                }
            }
        });
        menu.getChildren().add(button);
    }

    private void logout(Stage stage) {
        try {
            new LoginApp().start(new Stage());
            stage.close();
        } catch (Exception e) {
            showAlert("Error", "Failed to return to login screen");
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
