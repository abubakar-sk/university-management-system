package universitymanagementsystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class StudentManagement extends Application {
    private Map<String, Student> students = new HashMap<>();
    private boolean isAdmin;
    private boolean isFaculty;

    public StudentManagement(boolean isAdmin, boolean isFaculty) {
        this.isAdmin = isAdmin;
        this.isFaculty = isFaculty;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Management");

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        // Student List
        ListView<String> studentList = new ListView<>();
        studentList.setPrefSize(300, 200);

        // Input Fields
        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Student ID");

        TextField studentNameField = new TextField();
        studentNameField.setPromptText("Student Name");

        TextField studentEmailField = new TextField();
        studentEmailField.setPromptText("Student Email");

        TextField studentAddressField = new TextField();
        studentAddressField.setPromptText("Student Address");

        TextField studentPhoneField = new TextField();
        studentPhoneField.setPromptText("Student Phone");

        // Buttons
        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        Button backButton = new Button("Back"); // Back button

        // Add Button Action
        addButton.setOnAction(e -> {
            String id = studentIdField.getText();
            String name = studentNameField.getText();
            String email = studentEmailField.getText();
            String address = studentAddressField.getText();
            String phone = studentPhoneField.getText();

            if (!id.isEmpty() && !name.isEmpty() && !email.isEmpty() && !address.isEmpty() && !phone.isEmpty()) {
                if (!students.containsKey(id)) {
                    students.put(id, new Student(id, name, email, address, phone));
                    updateStudentList(studentList);
                } else {
                    showAlert("Error", "Student ID must be unique.");
                }
            }
        });

        // Edit Button Action
        editButton.setOnAction(e -> {
            String selected = studentList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String id = selected.split(":")[0];
                String name = studentNameField.getText();
                String email = studentEmailField.getText();
                String address = studentAddressField.getText();
                String phone = studentPhoneField.getText();

                if (!name.isEmpty() && !email.isEmpty() && !address.isEmpty() && !phone.isEmpty()) {
                    students.put(id, new Student(id, name, email, address, phone));
                    updateStudentList(studentList);
                }
            }
        });

        // Delete Button Action
        deleteButton.setOnAction(e -> {
            String selected = studentList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String id = selected.split(":")[0];
                students.remove(id);
                updateStudentList(studentList);
            }
        });

        // Back button action: Navigate back to UserDashboard
        backButton.setOnAction(e -> {
            new UserDashboard(isAdmin, isFaculty).start(new Stage());
            primaryStage.close(); // Close the Student Management window
        });

        // Layout Setup
        HBox inputFields = new HBox(10, studentIdField, studentNameField, studentEmailField, studentAddressField, studentPhoneField);
        HBox buttons = new HBox(10, addButton, editButton, deleteButton, backButton);

        layout.getChildren().addAll(studentList, inputFields, buttons);

        primaryStage.setScene(new Scene(layout, 500, 400));
        primaryStage.show();
    }

    private void updateStudentList(ListView<String> studentList) {
        studentList.getItems().clear();
        students.forEach((id, student) -> studentList.getItems().add(id + ": " + student.getName()));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Simple Student Class
    class Student {
        private String id, name, email, address, phone;

        public Student(String id, String name, String email, String address, String phone) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.address = address;
            this.phone = phone;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getAddress() { return address; }
        public String getPhone() { return phone; }
    }
}
