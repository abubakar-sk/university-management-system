package universitymanagementsystem;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class StudentManagement extends Application {
    private boolean isAdmin;
    private Stage returnStage;

    public void setReturnStage(Stage returnStage) {
        this.returnStage = returnStage;
    }

    public static class Student {
        private String id;
        private String name;
        private String email;
        private String password;
        private File profilePhoto;
        private String address;
        private String telephone;
        private String academicLevel;
        private double tuitionFee;
        private boolean paymentStatus;
        private Map<String, Map<String, String>> grades;
        private ObservableList<String> enrolledCourses;
        private int currentSemester;
        private String thesisTitle;
        private double progress;

        public Student(String id, String name, String email, String password, String academicLevel) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.password = password;
            this.academicLevel = academicLevel;
            this.tuitionFee = calculateTuitionFee(academicLevel);
            this.paymentStatus = false;
            this.grades = new HashMap<>();
            this.enrolledCourses = FXCollections.observableArrayList();
            this.currentSemester = 1;
            this.progress = 0.0;
        }

        private double calculateTuitionFee(String level) {
            return switch (level) {
                case "Undergraduate" -> 5000;
                case "Graduate" -> 4000;
                default -> 3000;
            };
        }

        // Getters and setters...
        public String getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public File getProfilePhoto() { return profilePhoto; }
        public void setProfilePhoto(File profilePhoto) { this.profilePhoto = profilePhoto; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getTelephone() { return telephone; }
        public void setTelephone(String telephone) { this.telephone = telephone; }
        public String getAcademicLevel() { return academicLevel; }
        public void setAcademicLevel(String academicLevel) {
            this.academicLevel = academicLevel;
            this.tuitionFee = calculateTuitionFee(academicLevel);
        }
        public double getTuitionFee() { return tuitionFee; }
        public boolean getPaymentStatus() { return paymentStatus; }
        public void updatePayment(boolean status) { paymentStatus = status; }
        public Map<String, Map<String, String>> getGrades() { return grades; }
        public ObservableList<String> getEnrolledCourses() { return enrolledCourses; }
        public int getCurrentSemester() { return currentSemester; }
        public void setCurrentSemester(int currentSemester) { this.currentSemester = currentSemester; }
        public String getThesisTitle() { return thesisTitle; }
        public void setThesisTitle(String thesisTitle) { this.thesisTitle = thesisTitle; }
        public double getProgress() { return progress; }
        public void setProgress(double progress) { this.progress = progress; }

        public void addGrade(String semester, String course, String grade) {
            grades.computeIfAbsent(semester, k -> new HashMap<>()).put(course, grade);
        }
    }

    public static class StudentIDGenerator {
        private static int counter = 1000;

        public static String generateID(String academicLevel) {
            String prefix = switch (academicLevel) {
                case "Undergraduate" -> "UG";
                case "Graduate" -> "GR";
                default -> "PH";
            };
            return String.format("%s%04d%04d", prefix, LocalDate.now().getYear() % 1000, counter++);
        }
    }

    public class AdminController {
        private final ObservableList<Student> students;
        private final ListView<String> studentListView;

        public AdminController(ObservableList<Student> students, ListView<String> studentListView) {
            this.students = students;
            this.studentListView = studentListView;
        }

        public void handleAddStudent(String name, String email, String academicLevel) {
            String generatedID = StudentIDGenerator.generateID(academicLevel);
            String defaultPassword = "Pass123";
            students.add(new Student(generatedID, name, email, defaultPassword, academicLevel));
            updateStudentListView();
        }

        public void handleEditStudent(String studentId, String newName, String newEmail,
                                      String newAddress, String newTelephone, String newAcademicLevel) {
            students.stream()
                    .filter(s -> s.getId().equals(studentId))
                    .findFirst()
                    .ifPresent(student -> {
                        student.setName(newName);
                        student.setEmail(newEmail);
                        student.setAddress(newAddress);
                        student.setTelephone(newTelephone);
                        student.setAcademicLevel(newAcademicLevel);
                    });
            updateStudentListView();
        }

        public void handleDeleteStudent(String selectedStudent) {
            if (selectedStudent != null) {
                students.removeIf(student -> student.getId().equals(parseStudentId(selectedStudent)));
                updateStudentListView();
            }
        }

        public void manageEnrollment(String studentId, String courseCode, boolean enroll) {
            Student student = students.stream()
                    .filter(s -> s.getId().equals(studentId))
                    .findFirst()
                    .orElse(null);

            if (student != null) {
                if (enroll) {
                    student.getEnrolledCourses().add(courseCode);
                } else {
                    student.getEnrolledCourses().remove(courseCode);
                }
                updateProgress(student);
            }
        }

        public void addGrade(String studentId, String semester, String course, String grade) {
            Student student = students.stream()
                    .filter(s -> s.getId().equals(studentId))
                    .findFirst()
                    .orElse(null);

            if (student != null) {
                student.addGrade(semester, course, grade);
                updateProgress(student);
            }
        }

        private void updateProgress(Student student) {
            int totalCourses = 40;
            int completed = student.getGrades().values().stream()
                    .mapToInt(Map::size)
                    .sum();
            student.setProgress((double) completed / totalCourses * 100);
        }

        private void updateStudentListView() {
            studentListView.getItems().clear();
            students.forEach(student -> studentListView.getItems().add(
                    student.getId() + " - " + student.getName()
            ));
        }

        public String parseStudentId(String display) {
            return display.split(" - ")[0];
        }

        public Student getStudentById(String id) {
            return students.stream()
                    .filter(s -> s.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }
    }

    public class AdminUI {
        private final AdminController controller;
        private final VBox layout;
        private TextField nameField;
        private TextField emailField;
        private ComboBox<String> levelCombo;
        private TextField addressField;
        private TextField telephoneField;

        public AdminUI() {
            ListView<String> studentListView = new ListView<>();
            this.controller = new AdminController(students, studentListView);
            this.layout = createUI(studentListView);
        }

        private VBox createUI(ListView<String> studentListView) {
            Label titleLabel = new Label("Student Management (Admin)");
            titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

            nameField = new TextField();
            emailField = new TextField();
            levelCombo = new ComboBox<>(FXCollections.observableArrayList(
                    "Undergraduate", "Graduate", "PhD"));
            addressField = new TextField();
            telephoneField = new TextField();

            nameField.setPromptText("Student Name");
            emailField.setPromptText("Email");
            levelCombo.setPromptText("Academic Level");
            addressField.setPromptText("Address");
            telephoneField.setPromptText("Telephone");

            Button addButton = new Button("Add Student");
            Button editButton = new Button("Edit Student");
            Button deleteButton = new Button("Delete Student");
            Button manageCoursesButton = new Button("Manage Courses");
            Button addGradeButton = new Button("Add Grade");
            Button paymentButton = new Button("Tuition Payment");

            addButton.setOnAction(e -> {
                controller.handleAddStudent(
                        nameField.getText(),
                        emailField.getText(),
                        levelCombo.getValue()
                );
                clearFields();
            });

            editButton.setOnAction(e -> {
                String selected = studentListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    controller.handleEditStudent(
                            controller.parseStudentId(selected),
                            nameField.getText(),
                            emailField.getText(),
                            addressField.getText(),
                            telephoneField.getText(),
                            levelCombo.getValue()
                    );
                }
            });

            deleteButton.setOnAction(e -> {
                controller.handleDeleteStudent(
                        studentListView.getSelectionModel().getSelectedItem()
                );
                clearFields();
            });

            manageCoursesButton.setOnAction(e -> {
                String selected = studentListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showCourseManagementDialog(controller.parseStudentId(selected));
                }
            });

            addGradeButton.setOnAction(e -> {
                String selected = studentListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showAddGradeDialog(controller.parseStudentId(selected));
                }
            });

            paymentButton.setOnAction(e -> {
                String selected = studentListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showPaymentDialog(controller.parseStudentId(selected));
                }
            });

            studentListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    Student selectedStudent = controller.getStudentById(controller.parseStudentId(newVal));
                    if (selectedStudent != null) {
                        nameField.setText(selectedStudent.getName());
                        emailField.setText(selectedStudent.getEmail());
                        levelCombo.setValue(selectedStudent.getAcademicLevel());
                        addressField.setText(selectedStudent.getAddress());
                        telephoneField.setText(selectedStudent.getTelephone());
                    }
                } else {
                    clearFields();
                }
            });

            studentListView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    String selected = studentListView.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        showStudentDetails(controller.parseStudentId(selected));
                    }
                }
            });

            GridPane buttonGrid = new GridPane();
            buttonGrid.setHgap(10);
            buttonGrid.setVgap(10);
            buttonGrid.addRow(0, addButton, editButton, deleteButton);
            buttonGrid.addRow(1, manageCoursesButton, addGradeButton, paymentButton);

            VBox formLayout = new VBox(10, titleLabel, nameField, emailField, levelCombo,
                    addressField, telephoneField, buttonGrid, studentListView);
            formLayout.setPadding(new Insets(15));
            addReturnButton(formLayout);
            controller.updateStudentListView();
            return formLayout;
        }

        private void showStudentDetails(String studentId) {
            Student student = controller.getStudentById(studentId);
            if (student == null) return;

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Student Details: " + student.getName());

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 20, 10, 10));

            grid.add(new Label("ID:"), 0, 0);
            grid.add(new Label(student.getId()), 1, 0);
            grid.add(new Label("Name:"), 0, 1);
            grid.add(new Label(student.getName()), 1, 1);
            grid.add(new Label("Email:"), 0, 2);
            grid.add(new Label(student.getEmail()), 1, 2);
            grid.add(new Label("Academic Level:"), 0, 3);
            grid.add(new Label(student.getAcademicLevel()), 1, 3);
            grid.add(new Label("Address:"), 0, 4);
            grid.add(new Label(student.getAddress()), 1, 4);
            grid.add(new Label("Telephone:"), 0, 5);
            grid.add(new Label(student.getTelephone()), 1, 5);
            grid.add(new Label("Tuition Status:"), 0, 6);
            grid.add(new Label(student.getPaymentStatus() ? "Paid" : "Unpaid"), 1, 6);
            grid.add(new Label("Progress:"), 0, 7);
            grid.add(new Label(String.format("%.1f%%", student.getProgress())), 1, 7);

            grid.add(new Label("Enrolled Courses:"), 0, 8);
            ListView<String> coursesList = new ListView<>(student.getEnrolledCourses());
            coursesList.setPrefHeight(100);
            grid.add(coursesList, 1, 8);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.showAndWait();
        }

        private void showCourseManagementDialog(String studentId) {
            Dialog<Pair<String, Boolean>> dialog = new Dialog<>();
            dialog.setTitle("Manage Enrollments");

            ButtonType enrollButton = new ButtonType("Enroll", ButtonBar.ButtonData.OK_DONE);
            ButtonType unenrollButton = new ButtonType("Unenroll", ButtonBar.ButtonData.OTHER);
            dialog.getDialogPane().getButtonTypes().addAll(enrollButton, unenrollButton, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 20, 10, 10));

            TextField courseField = new TextField();
            courseField.setPromptText("Course Code");

            grid.add(new Label("Student ID:"), 0, 0);
            grid.add(new Label(studentId), 1, 0);
            grid.add(new Label("Course Code:"), 0, 1);
            grid.add(courseField, 1, 1);

            dialog.getDialogPane().setContent(grid);
            dialog.setResultConverter(buttonType -> {
                if (buttonType == enrollButton) {
                    return new Pair<>(courseField.getText(), true);
                } else if (buttonType == unenrollButton) {
                    return new Pair<>(courseField.getText(), false);
                }
                return null;
            });

            Optional<Pair<String, Boolean>> result = dialog.showAndWait();
            result.ifPresent(pair -> {
                controller.manageEnrollment(studentId, pair.getKey(), pair.getValue());
            });
        }

        private void showAddGradeDialog(String studentId) {
            Dialog<Map<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Add Grade");

            ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 20, 10, 10));

            TextField semesterField = new TextField();
            TextField courseField = new TextField();
            TextField gradeField = new TextField();

            semesterField.setPromptText("Semester (e.g., Fall 2023)");
            courseField.setPromptText("Course Code");
            gradeField.setPromptText("Grade (A, B, C, etc.)");

            grid.add(new Label("Semester:"), 0, 0);
            grid.add(semesterField, 1, 0);
            grid.add(new Label("Course:"), 0, 1);
            grid.add(courseField, 1, 1);
            grid.add(new Label("Grade:"), 0, 2);
            grid.add(gradeField, 1, 2);

            dialog.getDialogPane().setContent(grid);
            dialog.setResultConverter(buttonType -> {
                if (buttonType == addButton) {
                    Map<String, String> result = new HashMap<>();
                    result.put("semester", semesterField.getText());
                    result.put("course", courseField.getText());
                    result.put("grade", gradeField.getText());
                    return result;
                }
                return null;
            });

            Optional<Map<String, String>> result = dialog.showAndWait();
            result.ifPresent(gradeInfo -> {
                controller.addGrade(studentId, gradeInfo.get("semester"),
                        gradeInfo.get("course"), gradeInfo.get("grade"));
            });
        }

        private void showPaymentDialog(String studentId) {
            Student student = controller.getStudentById(studentId);
            if (student == null) return;

            Dialog<Boolean> dialog = new Dialog<>();
            dialog.setTitle("Tuition Payment");

            ButtonType payButton = new ButtonType("Mark as Paid", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(payButton, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 20, 10, 10));

            grid.add(new Label("Student:"), 0, 0);
            grid.add(new Label(student.getName()), 1, 0);
            grid.add(new Label("Amount Due:"), 0, 1);
            grid.add(new Label(String.format("$%.2f", student.getTuitionFee())), 1, 1);
            grid.add(new Label("Current Status:"), 0, 2);
            grid.add(new Label(student.getPaymentStatus() ? "Paid" : "Unpaid"), 1, 2);

            dialog.getDialogPane().setContent(grid);
            dialog.setResultConverter(buttonType -> buttonType == payButton);

            Optional<Boolean> result = dialog.showAndWait();
            result.ifPresent(status -> {
                student.updatePayment(status);
                showAlert("Payment Updated", "Payment status updated successfully");
            });
        }

        private void clearFields() {
            nameField.clear();
            emailField.clear();
            levelCombo.getSelectionModel().clearSelection();
            addressField.clear();
            telephoneField.clear();
        }

        public VBox getLayout() { return layout; }

        private void addReturnButton(VBox layout) {
            Button returnBtn = new Button("Return to Dashboard");
            returnBtn.setOnAction(e -> {
                try {
                    // Get reference to current window
                    Stage currentStage = (Stage) returnBtn.getScene().getWindow();

                    // Open new dashboard
                    new UserDashboard().start(new Stage());

                    // Close current window
                    currentStage.close();
                } catch (Exception ex) {
                    System.err.println("Error returning to dashboard: " + ex.getMessage());
                }
            });
            layout.getChildren().add(returnBtn);
        }

        private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    public class StudentController {
        private final Student student;

        public StudentController(Student student) {
            this.student = student;
        }

        public void updatePassword(String newPassword) {
            if (newPassword.length() >= 6) {
                student.setPassword(newPassword);
                showAlert("Success", "Password updated successfully");
            } else {
                showAlert("Error", "Password must be at least 6 characters");
            }
        }

        public void updateProfilePhoto(File photo) {
            if (photo != null) {
                student.setProfilePhoto(photo);
                showAlert("Success", "Profile photo updated");
            }
        }

        private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    public class StudentUI {
        private final Student student;
        private final StudentController controller;

        public StudentUI(Student student) {
            this.student = student;
            this.controller = new StudentController(student);
        }

        public VBox createStudentDashboard() {
            TabPane tabPane = new TabPane();

            Tab profileTab = new Tab("Profile", createProfileView());
            Tab coursesTab = new Tab("Courses", createCoursesView());
            Tab gradesTab = new Tab("Grades", createGradesView());
            Tab tuitionTab = new Tab("Tuition", createTuitionView());

            tabPane.getTabs().addAll(profileTab, coursesTab, gradesTab, tuitionTab);
            VBox root = new VBox(createHeader(), tabPane);
            addReturnButton(root);
            return root;
        }

        private HBox createHeader() {
            HBox header = new HBox(10);
            header.setPadding(new Insets(10));
            header.setStyle("-fx-background-color: #f0f0f0;");

            ImageView photoView = new ImageView();
            if (student.getProfilePhoto() != null) {
                photoView.setImage(new Image(student.getProfilePhoto().toURI().toString()));
            }
            photoView.setFitHeight(50);
            photoView.setFitWidth(50);

            VBox infoBox = new VBox(5);
            infoBox.getChildren().addAll(
                    new Label(student.getName()),
                    new Label("ID: " + student.getId()),
                    new Label("Academic Level: " + student.getAcademicLevel())
            );

            header.getChildren().addAll(photoView, infoBox);
            return header;
        }

        private VBox createProfileView() {
            VBox profileBox = new VBox(10);
            profileBox.setPadding(new Insets(10));

            HBox photoBox = new HBox(10);
            ImageView photoView = new ImageView();
            if (student.getProfilePhoto() != null) {
                photoView.setImage(new Image(student.getProfilePhoto().toURI().toString()));
            }
            photoView.setFitHeight(100);
            photoView.setFitWidth(100);

            Button uploadPhotoBtn = new Button("Upload Profile Photo");
            uploadPhotoBtn.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Profile Photo");
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    controller.updateProfilePhoto(file);
                    photoView.setImage(new Image(file.toURI().toString()));
                }
            });

            photoBox.getChildren().addAll(photoView, uploadPhotoBtn);

            GridPane passwordGrid = new GridPane();
            passwordGrid.setHgap(10);
            passwordGrid.setVgap(10);

            PasswordField newPasswordField = new PasswordField();
            newPasswordField.setPromptText("New Password");

            Button updatePassBtn = new Button("Update Password");
            updatePassBtn.setOnAction(e ->
                    controller.updatePassword(newPasswordField.getText()));

            passwordGrid.add(new Label("Change Password:"), 0, 0);
            passwordGrid.add(newPasswordField, 1, 0);
            passwordGrid.add(updatePassBtn, 1, 1);

            GridPane infoGrid = new GridPane();
            infoGrid.setHgap(10);
            infoGrid.setVgap(10);

            infoGrid.add(new Label("Name:"), 0, 0);
            infoGrid.add(new Label(student.getName()), 1, 0);
            infoGrid.add(new Label("Email:"), 0, 1);
            infoGrid.add(new Label(student.getEmail()), 1, 1);
            infoGrid.add(new Label("Address:"), 0, 2);
            infoGrid.add(new Label(student.getAddress()), 1, 2);
            infoGrid.add(new Label("Telephone:"), 0, 3);
            infoGrid.add(new Label(student.getTelephone()), 1, 3);
            infoGrid.add(new Label("Academic Level:"), 0, 4);
            infoGrid.add(new Label(student.getAcademicLevel()), 1, 4);

            profileBox.getChildren().addAll(
                    new Label("Profile Information"),
                    photoBox,
                    new Separator(),
                    infoGrid,
                    new Separator(),
                    passwordGrid
            );

            return profileBox;
        }

        private VBox createCoursesView() {
            VBox coursesBox = new VBox(10);
            coursesBox.setPadding(new Insets(10));

            Label title = new Label("Enrolled Courses");
            title.setStyle("-fx-font-weight: bold;");

            ListView<String> coursesList = new ListView<>(student.getEnrolledCourses());
            coursesList.setPrefHeight(200);

            Label semesterLabel = new Label("Current Semester: " + student.getCurrentSemester());
            Label progressLabel = new Label(String.format("Program Progress: %.1f%%", student.getProgress()));

            coursesBox.getChildren().addAll(title, coursesList, semesterLabel, progressLabel);
            return coursesBox;
        }

        private VBox createGradesView() {
            VBox gradesBox = new VBox(10);
            gradesBox.setPadding(new Insets(10));

            Label title = new Label("Academic Grades");
            title.setStyle("-fx-font-weight: bold;");

            TableView<Map.Entry<String, Map<String, String>>> semesterTable = new TableView<>();
            TableColumn<Map.Entry<String, Map<String, String>>, String> semesterCol = new TableColumn<>("Semester");
            semesterCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getKey()));
            semesterTable.getColumns().add(semesterCol);

            TableView<Map.Entry<String, String>> gradesTable = new TableView<>();
            TableColumn<Map.Entry<String, String>, String> courseCol = new TableColumn<>("Course");
            courseCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getKey()));
            TableColumn<Map.Entry<String, String>, String> gradeCol = new TableColumn<>("Grade");
            gradeCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getValue()));
            gradesTable.getColumns().addAll(courseCol, gradeCol);

            semesterTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(
                            newVal.getValue().entrySet()
                    );
                    gradesTable.setItems(items);
                }
            });

            ObservableList<Map.Entry<String, Map<String, String>>> semesterItems =
                    FXCollections.observableArrayList(student.getGrades().entrySet());
            semesterTable.setItems(semesterItems);

            gradesBox.getChildren().addAll(title, semesterTable, gradesTable);
            return gradesBox;
        }

        private VBox createTuitionView() {
            VBox tuitionBox = new VBox(10);
            tuitionBox.setPadding(new Insets(10));

            Label title = new Label("Tuition Information");
            title.setStyle("-fx-font-weight: bold;");

            GridPane infoGrid = new GridPane();
            infoGrid.setHgap(10);
            infoGrid.setVgap(10);

            infoGrid.add(new Label("Tuition Fee:"), 0, 0);
            infoGrid.add(new Label(String.format("$%.2f", student.getTuitionFee())), 1, 0);
            infoGrid.add(new Label("Payment Status:"), 0, 1);
            infoGrid.add(new Label(student.getPaymentStatus() ? "Paid" : "Unpaid"), 1, 1);
            infoGrid.add(new Label("Academic Level:"), 0, 2);
            infoGrid.add(new Label(student.getAcademicLevel()), 1, 2);

            tuitionBox.getChildren().addAll(title, infoGrid);
            return tuitionBox;
        }

        private void addReturnButton(VBox layout) {
            Button returnBtn = new Button("Return to Dashboard");
            returnBtn.setOnAction(e -> {
                try {
                    // Get reference to current window
                    Stage currentStage = (Stage) returnBtn.getScene().getWindow();

                    // Open new dashboard
                    new UserDashboard().start(new Stage());

                    // Close current window
                    currentStage.close();
                } catch (Exception ex) {
                    System.err.println("Error returning to dashboard: " + ex.getMessage());
                }
            });
            layout.getChildren().add(returnBtn);
        }

        private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    private final ObservableList<Student> students = FXCollections.observableArrayList();

    public StudentManagement() { this(false); }
    public StudentManagement(boolean isAdmin) { this.isAdmin = isAdmin; }

    @Override
    public void start(Stage stage) {
        initializeSampleData();

        if (isAdmin) {
            AdminUI adminUI = new AdminUI();
            Scene scene = new Scene(adminUI.getLayout(), 600, 600);
            stage.setScene(scene);
            stage.setTitle("Student Management - Admin");
            stage.show();
        } else {
            if (!students.isEmpty()) {
                StudentUI studentUI = new StudentUI(students.get(0));
                VBox root = studentUI.createStudentDashboard();
                Scene scene = new Scene(root, 600, 500);
                stage.setScene(scene);
                stage.setTitle("Student Dashboard - " + students.get(0).getName());
                stage.show();
            }
        }
    }

    private void initializeSampleData() {
        Student student1 = new Student(StudentIDGenerator.generateID("Undergraduate"),
                "John Doe", "john@university.edu", "password123", "Undergraduate");
        student1.setAddress("123 University Ave");
        student1.setTelephone("555-1234");
        student1.updatePayment(true);
        student1.getEnrolledCourses().addAll("CS101", "MATH201", "ENG102");
        student1.addGrade("Fall 2025", "CS101", "A");
        student1.addGrade("Fall 2025", "MATH201", "B+");
        student1.setProgress(15.0);

        Student student2 = new Student(StudentIDGenerator.generateID("Graduate"),
                "Jane Smith", "jane@university.edu", "password456", "Graduate");
        student2.setAddress("456 College St");
        student2.setTelephone("555-5678");
        student2.getEnrolledCourses().addAll("CS501", "MATH601");
        student2.addGrade("Spring 2023", "CS501", "A-");
        student2.setProgress(30.0);

        students.addAll(student1, student2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}