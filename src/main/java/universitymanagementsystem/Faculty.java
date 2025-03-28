package universitymanagementsystem;

import java.util.ArrayList;
import java.util.List;

public class Faculty {
    private String name;
    //private profile photo
    private String degree;
    private String researchIntrest;
    //private List<String> coursesOffered = new ArrayList<String>();
    private String coursesOffered;
    private String email;
    private String password;
    private String officeLocation;

    public Faculty(String name){
        this.name = name;
        String[] split = name.split(" ");
        if (split.length == 1){
            email = split[0].toLowerCase() + "@university.com";
        }
        else{
            email = split[1].toLowerCase() + "@university.com";
        }
        coursesOffered = "null";
        password = "default123";
        degree = "null";
        researchIntrest = "null";
        officeLocation = "null";

    }
    public Faculty(){

    }
    public String GetName(){
        return name;
    }
    public void SetName(String newName){
        name = newName;
    }
    public void SetInterest(String newInterest){
        researchIntrest = newInterest;
    }
    public String getInterest(){
        return researchIntrest;
    }
    public void SetCourse(String newCourse){
        coursesOffered = newCourse;
    }
    public String getCourse(){return coursesOffered;}
    public void SetDegree(String newDeg){degree = newDeg;}
    public String GetDegree(){return degree;}
    public String GetEmail(){return email;}
    public String GetPassword(){return password;}
    public String GetOfficeLocation(){return officeLocation;}
    public void SetOfficeLocation(String officeLoc){officeLocation = officeLoc;}

}
