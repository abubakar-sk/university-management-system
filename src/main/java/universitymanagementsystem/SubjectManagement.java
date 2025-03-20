package universitymanagementsystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SubjectManagement extends Application {
    private Map<String, String> subjects = new HashMap<>();
    private boolean isAdmin;
    private boolean isFaculty;

    public SubjectManagement(boolean isAdmin, boolean isFaculty) {
        this.isAdmin = isAdmin;
        this.isFaculty = isFaculty;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Subject Management");

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        // Subject List
        ListView<String> subjectList = new ListView<>();
        subjectList.setPrefSize(300, 200);

        // Input Fields
        TextField subjectCodeField = new TextField();
        subjectCodeField.setPromptText("Subject Code");

        TextField subjectNameField = new TextField();
        subjectNameField.setPromptText("Subject Name");

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("Search Subject");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSubjectList(subjectList, newValue);
        });

        // Buttons
        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");

        addButton.setOnAction(e -> {
            String code = subjectCodeField.getText();
            String name = subjectNameField.getText();
            if (!code.isEmpty() && !name.isEmpty()) {
                if (!subjects.containsKey(code)) {
                    subjects.put(code, name);
                    updateSubjectList(subjectList);
                } else {
                    showAlert("Error", "Subject code must be unique.");
                }
            }
        });

        editButton.setOnAction(e -> {
            String selected = subjectList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String code = selected.split(":")[0];
                String name = subjectNameField.getText();
                if (!name.isEmpty()) {
                    subjects.put(code, name);
                    updateSubjectList(subjectList);
                }
            }
        });

        deleteButton.setOnAction(e -> {
            String selected = subjectList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String code = selected.split(":")[0];
                subjects.remove(code);
                updateSubjectList(subjectList);
            }
        });

        // Layout Setup
        HBox inputFields = new HBox(10, subjectCodeField, subjectNameField);
        HBox buttons = new HBox(10, addButton, editButton, deleteButton);

        if (isAdmin) {
            layout.getChildren().addAll(subjectList, searchField, inputFields, buttons);
        } else {
            layout.getChildren().addAll(subjectList, searchField);
        }

        primaryStage.setScene(new Scene(layout, 350, 400));
        primaryStage.show();
    }

    private void updateSubjectList(ListView<String> subjectList) {
        updateSubjectList(subjectList, "");
    }

    private void updateSubjectList(ListView<String> subjectList, String filter) {
        subjectList.getItems().clear();
        subjects.forEach((code, name) -> {
            if (code.contains(filter) || name.contains(filter)) {
                subjectList.getItems().add(code + ": " + name);
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
