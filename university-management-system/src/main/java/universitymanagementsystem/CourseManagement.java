package universitymanagementsystem;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CourseManagement extends Application {
    private boolean isAdmin;
    private ObservableList<Course> courses = FXCollections.observableArrayList();

    // Constructor to accept the isAdmin flag
    public CourseManagement(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public CourseManagement() {
        this.isAdmin = false; // Default to non-admin if no argument provided
    }

    @Override
    public void start(Stage stage) {
        if (isAdmin) {
            showAdminPanel(stage); // Admin view
        } else {
            showUserPanel(stage); // User view
        }
    }

    // Admin Panel
    private void showAdminPanel(Stage stage) {
        Label titleLabel = new Label("Course Management (Admin)");
        TextField courseCodeField = new TextField();
        courseCodeField.setPromptText("Course Code");
        TextField courseNameField = new TextField();
        courseNameField.setPromptText("Course Name");

        Button addCourseButton = new Button("Add Course");
        Button deleteCourseButton = new Button("Delete Course");
        ListView<String> courseListView = new ListView<>();

        updateCourseListView(courseListView);

        addCourseButton.setOnAction(e -> {
            String code = courseCodeField.getText();
            String name = courseNameField.getText();

            if (validateInput(code, name)) {
                if (!isCourseCodeDuplicate(code)) {
                    courses.add(new Course(name, code, "", "", 0, "", "", ""));
                    courseCodeField.clear();
                    courseNameField.clear();
                    updateCourseListView(courseListView);
                    System.out.println("Added Course: " + code + " - " + name);
                } else {
                    showAlert("Duplicate Course Code", "Course code must be unique.");
                }
            }
        });

        deleteCourseButton.setOnAction(e -> {
            String selectedCourse = courseListView.getSelectionModel().getSelectedItem();
            if (selectedCourse != null) {
                courses.removeIf(course -> course.getCourseCode().equals(parseCourseCode(selectedCourse)));
                updateCourseListView(courseListView);
                System.out.println("Deleted Course: " + selectedCourse);
            } else {
                showAlert("No Selection", "Please select a course to delete.");
            }
        });

        VBox layout = new VBox(10, titleLabel, courseCodeField, courseNameField, addCourseButton, deleteCourseButton, courseListView);
        addReturnToDashboardButton(layout, stage);

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Course Management");
        stage.show();
    }

    // User Panel
    private void showUserPanel(Stage stage) {
        Label titleLabel = new Label("Available Courses");
        ListView<String> courseListView = new ListView<>();
        updateCourseListView(courseListView);

        VBox layout = new VBox(10, titleLabel, courseListView);
        addReturnToDashboardButton(layout, stage);

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Course Management");
        stage.show();
    }

    private void updateCourseListView(ListView<String> courseListView) {
        courseListView.getItems().clear();
        courses.forEach(course -> courseListView.getItems().add(course.getCourseCode() + " - " + course.getCourseName()));
    }

    private boolean validateInput(String code, String name) {
        if (code == null || code.isEmpty() || name == null || name.isEmpty()) {
            showAlert("Invalid Input", "Course Code and Name cannot be empty.");
            return false;
        }
        return true;
    }

    private boolean isCourseCodeDuplicate(String code) {
        return courses.stream().anyMatch(course -> course.getCourseCode().equals(code));
    }

    private String parseCourseCode(String courseDisplay) {
        return courseDisplay.split(" - ")[0];
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addReturnToDashboardButton(VBox layout, Stage stage) {
        Button returnToDashboardBtn = new Button("Return to Dashboard");
        returnToDashboardBtn.setOnAction(e -> {
            try {
                new UserDashboard().start(new Stage());
                stage.close();
            } catch (Exception ex) {
                System.err.println("Error returning to dashboard: " + ex.getMessage());
            }
        });
        layout.getChildren().add(returnToDashboardBtn);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
