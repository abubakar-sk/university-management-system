package universitymanagementsystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserDashboard extends Application {
    private boolean isAdmin;
    private boolean isFaculty;

    public UserDashboard(boolean isAdmin, boolean isFaculty) {
        this.isAdmin = isAdmin;
        this.isFaculty = isFaculty;
    }

    @Override
    public void start(Stage stage) {
        // Left-side navigation menu
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(10));

        Label title = new Label("User Menu");
        Button viewSubjectsBtn = new Button("View Subjects");
        Button viewFacultyManagementBtn = new Button("View Faculty Management");
        Button logoutBtn = new Button("Logout");


        viewFacultyManagementBtn.setOnAction(_ -> {
            new FacultyManagement(isAdmin, isFaculty).start(new Stage());
            stage.close();
        });
        // View Subjects button action
        viewSubjectsBtn.setOnAction(_ -> {
            new SubjectManagement(isAdmin, isFaculty).start(new Stage());
            stage.close();
        });

        // Logout button action
        logoutBtn.setOnAction(_ -> {
            new LoginApp().start(new Stage());
            stage.close(); // Close user dashboard
        });

        menu.getChildren().addAll(title, viewSubjectsBtn, logoutBtn);

        // Main layout
        BorderPane layout = new BorderPane();
        layout.setLeft(menu); // Add menu to the left side

        Scene scene = new Scene(layout, 600, 400);
        stage.setTitle("User Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
