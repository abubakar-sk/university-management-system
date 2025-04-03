package universitymanagementsystem;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FacultyManagement extends Application {
    private ObservableList<Faculty> facultyList = FXCollections.observableArrayList();
    private boolean isAdmin;
    private boolean isFaculty;

    // Constructor to accept role-based access
    public FacultyManagement(boolean isAdmin, boolean isFaculty) {
        this.isAdmin = isAdmin;
        this.isFaculty = isFaculty;
    }

    public FacultyManagement() {
        this.isAdmin = false;
        this.isFaculty = false;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Faculty Management");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        if (isAdmin) {
            setupAdminPanel(primaryStage, layout);
        } else if (isFaculty) {
            setupFacultyPanel(primaryStage, layout);
        }

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Admin Panel
    private void setupAdminPanel(Stage stage, VBox layout) {
        Label titleLabel = new Label("Faculty Management (Admin)");
        TextField nameField = new TextField();
        nameField.setPromptText("Faculty Name");
        TextField degreeField = new TextField();
        degreeField.setPromptText("Faculty Degree");
        TextField researchField = new TextField();
        researchField.setPromptText("Research Interest");

        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        ListView<String> facultyListView = new ListView<>();
        updateFacultyListView(facultyListView);

        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String degree = degreeField.getText();
            String research = researchField.getText();

            if (validateInput(name, degree)) {
                facultyList.add(new Faculty(name, degree, research));
                updateFacultyListView(facultyListView);
                clearFields(nameField, degreeField, researchField);
                System.out.println("Added Faculty: " + name);
            } else {
                showAlert("Invalid Input", "Name and Degree cannot be empty.");
            }
        });

        deleteButton.setOnAction(e -> {
            String selectedFaculty = facultyListView.getSelectionModel().getSelectedItem();
            if (selectedFaculty != null) {
                facultyList.removeIf(faculty -> faculty.getName().equals(parseFacultyName(selectedFaculty)));
                updateFacultyListView(facultyListView);
                System.out.println("Deleted Faculty: " + selectedFaculty);
            } else {
                showAlert("No Selection", "Please select a faculty member to delete.");
            }
        });

        editButton.setOnAction(e -> {
            String selectedFaculty = facultyListView.getSelectionModel().getSelectedItem();
            if (selectedFaculty != null) {
                facultyList.removeIf(faculty -> faculty.getName().equals(parseFacultyName(selectedFaculty)));
                updateFacultyListView(facultyListView);
                System.out.println("Deleted Faculty: " + selectedFaculty);
            } else {
                showAlert("No Selection", "Please select a faculty member to delete.");
            }
        });

        HBox inputFields = new HBox(10, nameField, degreeField, researchField);
        HBox buttons = new HBox(10, addButton, deleteButton, editButton);
        layout.getChildren().addAll(titleLabel, inputFields, buttons, facultyListView);
        addReturnToDashboardButton(layout, stage);
    }

    // Faculty Panel
    private void setupFacultyPanel(Stage stage, VBox layout) {
        Label titleLabel = new Label("Faculty Profile");
        // Logic to display faculty profile info and edit options (e.g., research interest, courses)

        Button returnToDashboardBtn = new Button("Return to Dashboard");
        returnToDashboardBtn.setOnAction(e -> {
            try {
                new UserDashboard().start(new Stage());
                stage.close();
            } catch (Exception ex) {
                System.err.println("Error returning to dashboard: " + ex.getMessage());
            }
        });

        layout.getChildren().addAll(titleLabel, returnToDashboardBtn);
    }

    private void updateFacultyListView(ListView<String> facultyListView) {
        facultyListView.getItems().clear();
        facultyList.forEach(faculty -> facultyListView.getItems().add(faculty.getName() + " - " + faculty.getDegree()));
    }

    private boolean validateInput(String name, String degree) {
        return name != null && !name.isEmpty() && degree != null && !degree.isEmpty();
    }

    private String parseFacultyName(String display) {
        return display.split(" - ")[0];
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
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

    // Faculty Class (Nested for simplicity in this example)
    public static class Faculty {
        private String name;
        private String degree;
        private String researchInterest;

        public Faculty(String name, String degree, String researchInterest) {
            this.name = name;
            this.degree = degree;
            this.researchInterest = researchInterest;
        }

        public String getName() {
            return name;
        }

        public String getDegree() {
            return degree;
        }

        public String getResearchInterest() {
            return researchInterest;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
