package universitymanagementsystem;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SubjectManagement extends Application {
    private boolean isAdmin;
    private ObservableList<Subject> subjects = FXCollections.observableArrayList();

    public SubjectManagement(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public SubjectManagement() {
        this.isAdmin = false;
    }

    @Override
    public void start(Stage stage) {
        if (isAdmin) {
            showAdminPanel(stage);
        } else {
            showUserPanel(stage);
        }
    }

    // Admin Panel
    private void showAdminPanel(Stage stage) {
        Label titleLabel = new Label("Subject Management (Admin)");
        TextField codeField = new TextField();
        codeField.setPromptText("Subject Code");
        TextField nameField = new TextField();
        nameField.setPromptText("Subject Name");

        Button addSubjectButton = new Button("Add Subject");
        Button deleteSubjectButton = new Button("Delete Subject");
        ListView<String> subjectListView = new ListView<>();
        updateSubjectListView(subjectListView);

        addSubjectButton.setOnAction(e -> {
            String code = codeField.getText();
            String name = nameField.getText();

            if (validateInput(code, name)) {
                if (!isSubjectCodeDuplicate(code)) {
                    subjects.add(new Subject(code, name));
                    codeField.clear();
                    nameField.clear();
                    updateSubjectListView(subjectListView);
                    System.out.println("Added Subject: " + code + " - " + name);
                } else {
                    showAlert("Duplicate Code", "Subject code must be unique.");
                }
            }
        });

        deleteSubjectButton.setOnAction(e -> {
            String selectedSubject = subjectListView.getSelectionModel().getSelectedItem();
            if (selectedSubject != null) {
                subjects.removeIf(subject -> subject.getCode().equals(parseSubjectCode(selectedSubject)));
                updateSubjectListView(subjectListView);
                System.out.println("Deleted Subject: " + selectedSubject);
            } else {
                showAlert("No Selection", "Please select a subject to delete.");
            }
        });

        VBox layout = new VBox(10, titleLabel, codeField, nameField, addSubjectButton, deleteSubjectButton, subjectListView);
        addReturnToDashboardButton(layout, stage);

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Subject Management");
        stage.show();
    }

    // User Panel
    private void showUserPanel(Stage stage) {
        Label titleLabel = new Label("Available Subjects");
        TextField searchField = new TextField();
        searchField.setPromptText("Search Subjects");
        ListView<String> subjectListView = new ListView<>();
        updateSubjectListView(subjectListView);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            subjectListView.getItems().clear();
            subjects.stream()
                    .filter(subject -> subject.getName().toLowerCase().contains(newValue.toLowerCase()))
                    .forEach(subject -> subjectListView.getItems().add(subject.getCode() + " - " + subject.getName()));
        });

        VBox layout = new VBox(10, titleLabel, searchField, subjectListView);
        addReturnToDashboardButton(layout, stage);

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Subject Management");
        stage.show();
    }

    private void updateSubjectListView(ListView<String> subjectListView) {
        subjectListView.getItems().clear();
        subjects.forEach(subject -> subjectListView.getItems().add(subject.getCode() + " - " + subject.getName()));
    }

    private boolean validateInput(String code, String name) {
        if (code == null || code.isEmpty() || name == null || name.isEmpty()) {
            showAlert("Invalid Input", "Subject code and name cannot be empty.");
            return false;
        }
        return true;
    }

    private boolean isSubjectCodeDuplicate(String code) {
        return subjects.stream().anyMatch(subject -> subject.getCode().equals(code));
    }

    private String parseSubjectCode(String subjectDisplay) {
        return subjectDisplay.split(" - ")[0];
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

    // Subject Class
    public static class Subject {
        private String code;
        private String name;

        public Subject(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
