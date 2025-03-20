package universitymanagementsystem;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class StudentManagement extends Application {

    // Student model
    public static class Student {
        private String id;
        private String name;
        private String email;
        private String password;
        private ObservableList<String> enrolledCourses;
        private File profilePhoto; // New attribute for profile photo

        public Student(String id, String name, String email, String password) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.password = password;
            this.enrolledCourses = FXCollections.observableArrayList();
            this.profilePhoto = null; // Default to no profile photo
        }

        // Getters and Setters
        public String getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) {
            if (name != null && !name.isEmpty()) {
                this.name = name;
            }
        }

        public String getEmail() { return email; }
        public void setEmail(String email) {
            if (email != null && email.contains("@")) {
                this.email = email;
            }
        }

        public String getPassword() { return password; }
        public void setPassword(String password) {
            if (password != null && password.length() >= 6) {
                this.password = password;
            }
        }

        public ObservableList<String> getEnrolledCourses() { return enrolledCourses; }

        public File getProfilePhoto() { return profilePhoto; }
        public void setProfilePhoto(File profilePhoto) { this.profilePhoto = profilePhoto; }
    }

    private ObservableList<Student> students = FXCollections.observableArrayList();
    private boolean isAdmin;

    public StudentManagement(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public StudentManagement() {
        this.isAdmin = false;
    }

    @Override
    public void start(Stage stage) {
        if (isAdmin) {
            showAdminPanel(stage);
        } else {
            showStudentPanel(stage, "S123"); // Replace "S123" with logged-in student ID
        }
    }

    // Admin Panel
    private void showAdminPanel(Stage stage) {
        Label titleLabel = new Label("Student Management (Admin)");
        TextField idField = new TextField();
        idField.setPromptText("Student ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Student Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button addStudentButton = new Button("Add Student");
        Button deleteStudentButton = new Button("Delete Student");
        ListView<String> studentListView = new ListView<>();
        updateStudentListView(studentListView);

        addStudentButton.setOnAction(e -> {
            String id = idField.getText();
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (validateInput(id, name, email, password)) {
                Student newStudent = new Student(id, name, email, password);
                students.add(newStudent);
                updateStudentListView(studentListView);
                clearFields(idField, nameField, emailField, passwordField);
                System.out.println("Added Student: " + id + " - " + name);
            } else {
                showAlert("Invalid Input", "All fields must be filled in correctly.");
            }
        });

        deleteStudentButton.setOnAction(e -> {
            String selectedStudent = studentListView.getSelectionModel().getSelectedItem();
            if (selectedStudent != null) {
                students.removeIf(student -> student.getId().equals(parseStudentId(selectedStudent)));
                updateStudentListView(studentListView);
                System.out.println("Deleted Student: " + selectedStudent);
            } else {
                showAlert("No Selection", "Please select a student to delete.");
            }
        });

        VBox layout = new VBox(10, titleLabel, idField, nameField, emailField, passwordField, addStudentButton, deleteStudentButton, studentListView);
        addReturnToDashboardButton(layout, stage);

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Student Management - Admin");
        stage.show();
    }

    // Student Panel
    private void showStudentPanel(Stage stage, String studentId) {
        Student loggedInStudent = students.stream()
                .filter(student -> student.getId().equals(studentId))
                .findFirst()
                .orElse(null);

        if (loggedInStudent == null) {
            showAlert("Error", "Student not found!");
            return;
        }

        Label titleLabel = new Label("Student Profile");
        TextField nameField = new TextField(loggedInStudent.getName());
        TextField emailField = new TextField(loggedInStudent.getEmail());
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter new password");

        Button updateButton = new Button("Update Profile");
        Button uploadPhotoButton = new Button("Upload Profile Photo");

        uploadPhotoButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                loggedInStudent.setProfilePhoto(file);
                showAlert("Success", "Profile photo updated.");
            }
        });

        updateButton.setOnAction(e -> {
            loggedInStudent.setName(nameField.getText());
            loggedInStudent.setEmail(emailField.getText());
            if (!passwordField.getText().isEmpty()) {
                loggedInStudent.setPassword(passwordField.getText());
            }
            showAlert("Success", "Profile updated successfully!");
        });

        VBox layout = new VBox(10, titleLabel, new Label("Name:"), nameField, new Label("Email:"), emailField, new Label("New Password:"), passwordField, updateButton, uploadPhotoButton);

        addReturnToDashboardButton(layout, stage);

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Student Profile");
        stage.show();
    }

    private void updateStudentListView(ListView<String> studentListView) {
        studentListView.getItems().clear();
        students.forEach(student -> studentListView.getItems().add(student.getId() + " - " + student.getName()));
    }

    private boolean validateInput(String id, String name, String email, String password) {
        return id != null && !id.isEmpty()
                && name != null && !name.isEmpty()
                && email != null && email.contains("@")
                && password != null && password.length() >= 6;
    }

    private String parseStudentId(String display) {
        return display.split(" - ")[0];
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
        launch();
    }
}
