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
    private String curUser;
    public UserDashboard(boolean isAdmin, boolean isFaculty, String curUser) {
        this.isAdmin = isAdmin;
        this.isFaculty = isFaculty;
        this.curUser = curUser;
    }
    public String getPassword(){
        return "2";
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
            new FacultyManagement(isAdmin, isFaculty, curUser).start(new Stage());
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

        menu.getChildren().addAll(title, viewSubjectsBtn, viewFacultyManagementBtn, logoutBtn);

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
