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
    private Faculty facUser = new Faculty();
    //public ArrayList<String> facultyListNam = new ArrayList<>();
private List<String> changeName = new ArrayList<>();
    //private ImportFaculty impFac = new ImportFaculty();
    private boolean isAdmin;
    private boolean isFaculty;
    private String curUser = "";
    private int facUserNum;
    private String[] move = new String[]{"         Name         ", "         Title         ", " Courses offered ", "        Degree        ", "Research interest", "         Email         ", " Office Location "};
    private String[] move2 = new String[]{"Name", "Title", "Courses offered", "Degree", "Research interest", "Email", "Office Location", "Password"};
    private String[] move3 = new String[]{"pos 1", "pos 2", "pos 3", "pos 4", "pos 5", "pos 6", "pos 7", "pos 8", "pos 9", "pos 10"};
    private int cur = 0;//current edit/top admin number
    private int cur2 = 0;//current position of _______ bottom admin number
    public FacultyManagement(boolean isAdmin, boolean isFaculty, String currentUser) {
        this.isAdmin = isAdmin;
        this.isFaculty = isFaculty;
        curUser = currentUser;
        System.out.println(curUser);
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ImportFaculty impFac = new ImportFaculty();
        primaryStage.setTitle("Faculty Management");
        //impFac.start();
        for(int i = 0; i < impFac.GetFac().size(); i++){

            facultyList.add(impFac.GetFac().get(i));
            if (facultyList.get(i).iDCheck(curUser)){
                facUser = facultyList.get(i);
                facUserNum = i;
            }
        }

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        // Subject List
        ListView<String> facultyListNam = new ListView<>();
        facultyListNam.setPrefSize(300, 200);
    //    ListView<String> facultyListNamFac = new ListView<>();
      //  facultyListNamFac.setPrefSize(300, 200);
        ListView<String> facultyListCourse = new ListView<>();
        facultyListCourse.setPrefSize(200, 200);

        // ListView<Faculty> facultyList = new ListView<>();
        //facultyList.setPrefSize(300, 200);

        // Input Fields
        //name
        TextField facultyNameField = new TextField();
        facultyNameField.setPromptText("Faculty " + move2[cur]);
        TextField facultyPassword = new TextField();
        facultyPassword.setPromptText("Password");
        TextField facultyCourseField = new TextField();
        facultyCourseField.setPromptText("Add Course");
        TextField search = new TextField();
        facultyCourseField.setPromptText("Search");

        //profile photo

        //degree
        //    TextField facultyDegreeField = new TextField();
        //  facultyDegreeField.setPromptText("Faculty Degree");

        //research interest

        //degree
        changeName.add("null");
        // Buttons
        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        Button interestButton = new Button("Interest");
        Button assignCourseButton = new Button("Assign Course");
        Button viewProfileButton = new Button("View Profile");
        Button facultyProfileViewButton = new Button("View Profile(Admin)");

        Button facultyAdminLeft = new Button("<");
        Button facultyEditSelect = new Button(move[cur]);
        Button facultyAdminRight = new Button(">");

        Button facultyAdminLeft2 = new Button("<");
        Button facultyEditSelect2 = new Button(move3[cur2]);
        Button facultyAdminRight2 = new Button(">");
        Button deleteCourse = new Button("Delete course");
        Button save = new Button("Save changes");
        Button editPasswrd = new Button("Edit Password");



        if(isAdmin){
            updateFacultyList(facultyListNam, "");
        } else if (isFaculty) {
            updateFacultyList(facultyListNam,"");
        }
        else{
            updateFacultyList(facultyListNam, facultyCourseField.getText());
        }




        deleteCourse.setOnAction(e -> {
            String selected = facultyListNam.getSelectionModel().getSelectedItem();
            facultyList.get(facultyListNam.getItems().indexOf(selected)).DeleteACourse(cur2);
        });
        facultyAdminLeft2.setOnAction(e -> {
            String selected = facultyListNam.getSelectionModel().getSelectedItem();
            //int min = (facultyList.get(facultyListNam.getItems().indexOf(selected)).GetCourses().size()>0)? 1 : 0;

            if (cur2 > 0){
                cur2 -=1;
                //facultyNameField.clear();
                //facultyNameField.setPromptText("Faculty " + move2[cur]);

                facultyEditSelect2.setText(move3[cur2]);
                //facultyEditSelect.setAccessibleText(move[cur - 1]);// = new Button();
            }
        });
        facultyAdminRight2.setOnAction(e -> {
            if (cur2 < 9){
                //(move[cur + 1]) -> facultyEditSelect;
                cur2 +=1;
                //facultyNameField.clear();
                //facultyNameField.setPromptText("Faculty " + move2[cur]);
                facultyEditSelect2.setText(move3[cur2]);
                //facultyEditSelect.setAccessibleText(move[cur + 1]);// = new Button();
            }
        });










        facultyAdminLeft.setOnAction(e -> {
            if (cur > 0){
                cur -=1;
                facultyNameField.clear();
                facultyNameField.setPromptText("Faculty " + move2[cur]);

                facultyEditSelect.setText(move[cur]);
                //facultyEditSelect.setAccessibleText(move[cur - 1]);// = new Button();
            }
        });
        facultyAdminRight.setOnAction(e -> {
            if (cur < 6){
                //(move[cur + 1]) -> facultyEditSelect;
                cur +=1;
                facultyNameField.clear();
                facultyNameField.setPromptText("Faculty " + move2[cur]);
                facultyEditSelect.setText(move[cur]);
                //facultyEditSelect.setAccessibleText(move[cur + 1]);// = new Button();
            }
        });







        addButton.setOnAction(e -> {
            String code = facultyNameField.getText();
            //          String name = facultyNameField.getText();
            if (!code.isEmpty()) {
                facultyList.add(new Faculty(code, facultyList.size()));

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
                switch(cur) {
                    case 0:
                        facultyList.get(facultyListNam.getItems().indexOf(selected)).SetName(code);//Name", "Courses offered", "Degree", "Research interest", "Email", "Office Location
                        break;
                    case 1:
                        facultyList.get(facultyListNam.getItems().indexOf(selected)).SetTitle(code);
                        break;
                    case 2:
                        if (facultyList.get(facultyListNam.getItems().indexOf(selected)).GetCourses().size() > cur2) {
                            facultyList.get(facultyListNam.getItems().indexOf(selected)).EditCourse(code, cur2);//, facultyListNam.getItems().indexOf(selected));

                        }
                break;

                    case 3:
                        facultyList.get(facultyListNam.getItems().indexOf(selected)).SetDegree(code);
                        break;
                    case 4:
                        facultyList.get(facultyListNam.getItems().indexOf(selected)).SetInterest(code);
                        break;
                    case 5:
                        facultyList.get(facultyListNam.getItems().indexOf(selected)).SetEmail(code);
                        break;
                    case 6:
                        facultyList.get(facultyListNam.getItems().indexOf(selected)).SetOfficeLocation(code);
                        break;

                }

                updateFacultyList(facultyListNam, " ");

            }
        });

        assignCourseButton.setOnAction(e -> {
            String code = facultyCourseField.getText();
            String selected = facultyListNam.getSelectionModel().getSelectedItem();
            //          String name = facultyNameField.getText();
            if (selected != null && code != "" && code != " ") {
                //System.out.println(facultyListNam.getItems().indexOf(selected));
                facultyList.get(facultyListNam.getItems().indexOf(selected)).AddCourse(code);

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
        facultyProfileViewButton.setOnAction(e ->{
            //facultyEditSelect.setText("Password");
            //facultyNameField.setPromptText("Password");
            String code = facultyPassword.getText();
            String selected = facultyListNam.getSelectionModel().getSelectedItem();
            //
            if (selected != null) {
                //System.out.println(facultyListNam.getItems().indexOf(selected));
                //facultyList.get(facultyListNam.getItems().indexOf(selected)).SetInterest(code);
                facultyList.get(facultyListNam.getItems().indexOf(selected)).SwitchExpanded();
                updateFacultyList(facultyListNam, " ");

            }
                });
        save.setOnAction(e -> {
            impFac.ExportFacuty(facultyList);
            new UserDashboard(isAdmin, isFaculty, "").start(new Stage());
            primaryStage.close(); // Close the Student Management window
        });








        // Back button action: Navigate back to UserDashboard


//        assignCourseButton.setOnAction(e -> {

//            String code = facultyCourseField.getText();
//            String selected = facultyListNam.getSelectionModel().getSelectedItem();
//            String name = facultyNameField.getText();
//            if (selected != null) {
//                //System.out.println(facultyListNam.getItems().indexOf(selected));
//                facultyList.get(facultyListCourse.getItems().indexOf(selected)).SetCourse(code);
//
//                updateFacultyList(facultyListCourse, " ");
//
//            }
//        });


        viewProfileButton.setOnAction(e ->{
      //      String code = facultyNameField.getText();
            String selected = facultyListNam.getSelectionModel().getSelectedItem();
            //
            if (selected != null) {
                System.out.println(facultyListNam.getItems().indexOf(selected));
               // facultyList.get(facultyListNam.getItems().indexOf(selected)).SetInterest(code);
                facultyList.get(facultyListNam.getItems().indexOf(selected)).SwitchExpanded();
                updateFacultyList(facultyListNam, " ");

            } updateFacultyList(facultyListNam,  " ");
        });
        editPasswrd.setOnAction(e ->{
                    String code = facultyNameField.getText();
                    String selected = facultyListNam.getSelectionModel().getSelectedItem();
                    //          String name = facultyNameField.getText();
                    if (selected != null) {
                        //System.out.println(facultyListNam.getItems().indexOf(selected));

                        facultyList.get(facultyListNam.getItems().indexOf(selected)).SetPassword(code);//Name", "Courses offered", "Degree", "Research interest", "Email", "Office Location
                    }
                        updateFacultyList(facultyListNam, facultyCourseField.getText());

                    });



        // Layout Setup
        HBox inputFields = new HBox(10, facultyNameField, facultyCourseField);//facultyDegreeField
        HBox buttons = new HBox(10, addButton, editButton, deleteButton);//, assignCourseButton);
        HBox buttonsLine2 = new HBox(10, assignCourseButton, facultyProfileViewButton);
        HBox buttonsEditChange = new HBox(14, facultyAdminLeft, facultyEditSelect, facultyAdminRight);
        HBox buttonsEditChange2 = new HBox(10, facultyAdminLeft2, facultyEditSelect2, facultyAdminRight2);
        //HBox buttonsEditChange2 = new HBox(facultyAdminLeft2Course, facultyEditSelectCourseNum, facultyAdminRight2Course);
        HBox buttonsFac = new HBox(10, assignCourseButton);

        if (isAdmin) {
            layout.getChildren().addAll(facultyListNam, inputFields, buttons, buttonsLine2, assignCourseButton, buttonsEditChange, buttonsEditChange2, save);
        }else if(isFaculty){
            layout.getChildren().addAll(facultyListNam, viewProfileButton, editPasswrd, save, facultyPassword);
        } else {
            layout.getChildren().addAll(facultyListNam, facultyCourseField, viewProfileButton, save);
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
            if (isAdmin == true && isFaculty == false){
                switch(facultyList.get(i).GetIsExpanded()){
                    case 0:
                        facultyLis.getItems().add(facultyList.get(i).GetName());
                        break;
                    case 1:
                        facultyLis.getItems().add(facultyList.get(i).GetName() + "\nTitle: " + facultyList.get(i).GetTitle() + "\nCourses offered: " + facultyList.get(i).GetCourses() + "\nDegree: " + facultyList.get(i).GetDegree() + "\nInterest: " + facultyList.get(i).GetInterest() + "\nemail: " + facultyList.get(i).GetEmail() + "\nOffice location: " + facultyList.get(i).GetOfficeLocation() + "\nID: " + facultyList.get(i).GetId());//for multiple courses, just write them all in the line
                        break;
                }
                 //+ " " + facultyList.get(i).getCourse());
            }
            else if (isFaculty == true){

            //    if (facultyList.get().iDCheck(curUser)){
                switch(facultyList.get(i).GetIsExpanded()) {
                    case 0:
                        facultyLis.getItems().add(facultyList.get(i).GetName());
                        break;
                    case 1:
                        facultyLis.getItems().add(facUser.GetName() + "\nTitle: " + facUser.GetTitle() + "\nCourses offered: " + facUser.GetCourses() + "\nDegree: " + facUser.GetDegree() + "\nInterest: " + facUser.GetInterest() + "\nemail: " + facUser.GetEmail() + "\nOffice location: " + facUser.GetOfficeLocation() + "\nID: " + facUser.GetId());//for multiple courses, just write them all in the line

                        break;
                }
              //  }
            i = 20;
                // + " " + facultyList.get(i).GetInterest());
            }
            else{
                switch(facultyList.get(i).GetIsExpanded()) {
                    case 0:

                        if (facultyList.get(i).GetCourses().contains(filter) || filter.isEmpty()) {
                            facultyLis.getItems().add(facultyList.get(i).GetName());
                        }
                        break;
                    case 1:
                        if (facultyList.get(i).GetCourses().contains(filter) || filter.isEmpty()) {
                            facultyLis.getItems().add(facUser.GetName() + "\nTitle: " + facUser.GetTitle() + "\nCourses offered: " + facUser.GetCourses() + "\nInterest: " + facUser.GetInterest() + "\nemail: " + facUser.GetEmail() + "\nOffice location: " + facUser.GetOfficeLocation());//for multiple courses, just write them all in the line
                        }
                        break;
                }
            }

            // facaltyListNam;
            //}
        }
    }
    private void updateFacultyList2(ListView<String> facultyLis) {
        //   facultyListNam.clear();
        facultyLis.getItems().clear();
        for (int i = 0; i < facultyList.size(); i++){
            //  if (code.contains(filter) || name.contains(filter)) {
            if (isAdmin == true){
                facultyLis.getItems().add(facultyList.get(i).GetName() + "\nCourses offered: " + facultyList.get(i).GetCourses() + "\nDegree: " + facultyList.get(i).GetDegree() + "\nInterest: " + facultyList.get(i).GetInterest() + "\nemail: " + facultyList.get(i).GetEmail() + "\nOffice location: " + facultyList.get(i).GetOfficeLocation());//for multiple courses, just write them all in the line
            }
            else if (isFaculty == true){
                facultyLis.getItems().add(facultyList.get(i).GetName() + " " + facultyList.get(i).GetInterest());
            }
            else{

            }

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

