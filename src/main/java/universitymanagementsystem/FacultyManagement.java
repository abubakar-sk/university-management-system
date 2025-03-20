package universitymanagementsystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FacultyManagement extends Application {
    //private Map<String, Faculty> faculties = new HashMap<>();
    private List<Faculty> facultyList = new ArrayList<Faculty>();
    //public ArrayList<String> facultyListNam = new ArrayList<>();
    private boolean isAdmin;
    private boolean isFaculty;

    public FacultyManagement(boolean isAdmin, boolean isFaculty) {
        this.isAdmin = isAdmin;
        this.isFaculty = isFaculty;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Faculty Management");

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        // Subject List
        ListView<String> facultyListNam = new ListView<>();
        facultyListNam.setPrefSize(300, 200);

        // ListView<Faculty> facultyList = new ListView<>();
        //facultyList.setPrefSize(300, 200);

        // Input Fields
        //name
        TextField facultyNameField = new TextField();
        facultyNameField.setPromptText("Faculty Name");

        //profile photo

        //degree
        //    TextField facultyDegreeField = new TextField();
        //  facultyDegreeField.setPromptText("Faculty Degree");

        //research interest

        //degree

        // Buttons
        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        Button interestButton = new Button("Interest");
        Button assignCourseButton = new Button("Assign Course");


        addButton.setOnAction(e -> {
            String code = facultyNameField.getText();
            //          String name = facultyNameField.getText();
            if (!code.isEmpty()) {
                facultyList.add(new Faculty(code));

                updateFacultyList(facultyListNam, " ");

            }
        });

        deleteButton.setOnAction(e -> {
            //       String code = facultyNameField.getText();
            String selected = facultyListNam.getSelectionModel().getSelectedItem();
            //          String name = facultyNameField.getText();
            if (selected != null) {
                //System.out.println(facultyListNam.getItems().indexOf(selected));
                facultyList.remove(facultyListNam.getItems().indexOf(selected));

                updateFacultyList(facultyListNam, " ");

            }
        });


        editButton.setOnAction(e -> {
            String code = facultyNameField.getText();
            String selected = facultyListNam.getSelectionModel().getSelectedItem();
            //          String name = facultyNameField.getText();
            if (selected != null) {
                //System.out.println(facultyListNam.getItems().indexOf(selected));
                facultyList.get(facultyListNam.getItems().indexOf(selected)).SetName(code);

                updateFacultyList(facultyListNam, " ");

            }
        });

        assignCourseButton.setOnAction(e -> {
            String code = facultyNameField.getText();
            String selected = facultyListNam.getSelectionModel().getSelectedItem();
            //          String name = facultyNameField.getText();
            if (selected != null) {
                //System.out.println(facultyListNam.getItems().indexOf(selected));
                facultyList.get(facultyListNam.getItems().indexOf(selected)).SetCourse(code);

                updateFacultyList(facultyListNam, " ");

            }
        });
        interestButton.setOnAction(e -> {
            String code = facultyNameField.getText();
            String selected = facultyListNam.getSelectionModel().getSelectedItem();
            //          String name = facultyNameField.getText();
            if (selected != null) {
                //System.out.println(facultyListNam.getItems().indexOf(selected));
                facultyList.get(facultyListNam.getItems().indexOf(selected)).SetInterest(code);

                updateFacultyList(facultyListNam, " ");

            }
        });

        // Layout Setup
        HBox inputFields = new HBox(10, facultyNameField);//facultyDegreeField
        HBox buttons = new HBox(10, addButton, editButton, deleteButton, assignCourseButton);
        HBox buttonsFac = new HBox(10, assignCourseButton);

        if (isAdmin) {
            layout.getChildren().addAll(facultyListNam, inputFields, buttons);
        }else if(isFaculty){
            layout.getChildren().addAll(interestButton);
        } else {
            layout.getChildren().addAll(facultyListNam);
        }
        //facultyList[0].getList();

        primaryStage.setScene(new Scene(layout, 350, 400));
        primaryStage.show();
    }


    // private void updateSubjectList(ListView<String> subjectList, String filter) {
    //   subjectList.getItems().clear();
    // subjects.forEach((code, name) -> {
    //   if (code.contains(filter) || name.contains(filter)) {
    //     subjectList.getItems().add(code + ": " + name);
    //           }
    //     });
    //}


    private void updateFacultyList(ListView<String> facultyLis, String filter) {
        //   facultyListNam.clear();
        facultyLis.getItems().clear();
        for (int i = 0; i < facultyList.size(); i++){
            //  if (code.contains(filter) || name.contains(filter)) {
            facultyLis.getItems().add(facultyList.get(i).GetName() + " " + facultyList.get(i).getInterest());
            // facaltyListNam;
            //}
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

