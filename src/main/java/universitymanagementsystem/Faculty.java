package universitymanagementsystem;

import java.util.ArrayList;
import java.util.List;

public class Faculty {
    private String iD;
    private String name;
    //private profile photo
    private String degree;
    private String researchIntrest;
    //private List<String> coursesOffered = new ArrayList<String>();
    private List<String> coursesOffered = new ArrayList<String>();
    private String email;
    private String password;
    private String officeLocation;
    private String title;
    private int isExpanded = 0;

    public Faculty(String iDStr){
        iD = iDStr;
        name = "null";

        degree = "null";//   gengre;
        researchIntrest = "null";
        //coursesOffered = new ArrayList<String>();
        email = "null";
        password = "null";
        officeLocation = "null";
        title = "null";

    }
    public Faculty(String name, int idVal){
//        if (idVal >= 1000){
//            d = "F" + idVal;
//        }
        String d = "";///idVal /10000;
        if (idVal + 1 >= 100 && idVal + 1 < 1000){
            d = "F0" + idVal;
        }
        if (idVal + 1 >= 10 && idVal + 1 < 100){
            d = "F00" + idVal;
        }
        if (idVal + 1 >= 1 && idVal + 1 < 10){
            d = "F000" + idVal;
        }


        iD = d;
        this.name = name;
        String[] split = name.split(" ");
      //  if (split.length == 1){
        ///    email = split[1].toLowerCase() + "@university.com";
    //    }
  //      {
            email = split[split.length - 1].toLowerCase() + "@university.edu";
//        }
        coursesOffered.add("null");
        password = "default123";
        degree = "null";
        researchIntrest = "null";
        officeLocation = "null";
        title = "null";


    }
    public Faculty(){

    }
    public Faculty(String idVal, String titel, String nam, String deg, String resInt, String ema, String offLoc, String cL, String pasD){//String ema, String titel, String nam, String crsOfrd, String psswrd, String deg, String resInt, String offLoc, int idVal){
        iD = idVal;//
        email = ema;
        name = nam;
        //email = split[split.length - 1].toLowerCase() + "@university.edu";
//        }
        title = titel;
        String dt = "/";
        dt = "e";
        String[] cF = cL.split("/");
        for(int i = 0; i < cF.length; i++){
            coursesOffered.add(cF[i]);
        }

        password = pasD;
        degree = deg;
        researchIntrest = resInt;
        officeLocation = offLoc;
        title = titel;
    }
    public boolean iDCheck(String sampleId){
        if (sampleId.equals(iD)){
            return true;
        }
        return false;
    }
    public String GetName(){
        return name;
    }
    public void SetName(String newName) {
        String[] split2 = newName.split(" ");
        if (email == split2 + "@university.edu") {


            String[] split = newName.split(" ");
            email = split[split.length - 1].toLowerCase() + "@university.edu";
        }

        name = newName;
    }
    public void SetInterest(String newInterest){
        researchIntrest = newInterest;
    }
    public String GetInterest(){
        return researchIntrest;
    }
    public void AddCourse(String newCourse){
        if (coursesOffered.get(0) == "null"){
            coursesOffered.set(0, newCourse);
        }
        else{
            coursesOffered.add(newCourse);
        }

    }
    public void EditCourse(String newCourse, int index){

            coursesOffered.set(index, newCourse);


    }
    public String GetCourse(int index){
        return coursesOffered.get(index);
    }
    public List<String> GetCourses(){
        return coursesOffered;
    }
    public void SetDegree(String newDeg){degree = newDeg;}
    public String GetDegree(){return degree;}
    public String GetEmail(){return email;}
    public void SetEmail(String newEmail){
        if (!email.contains("@")){
            email = newEmail + "@university.edu";
        }
        else{
            email = newEmail;
        }
    }
    public String GetPassword(){return password;}
    public void SetPassword(String newPswrd){
        password = newPswrd;
    }
    public String GetTitle(){return  title;}
    public void  SetTitle(String titel){title = titel;}
    public String GetId(){return iD;}
    public String GetOfficeLocation(){return officeLocation;}
    public void SetOfficeLocation(String officeLoc){officeLocation = officeLoc;}
    public void SwitchExpanded(){
        isExpanded++;
        isExpanded = isExpanded%2;
    }
    public int GetIsExpanded(){
        return isExpanded;
    }


    public void DeleteACourse(int index){
        List<String> newList = new ArrayList<String>();
        for(int i = 0; i < coursesOffered.size(); i++){
            if (i != index){
                newList.add(coursesOffered.get(i));
            }
        }
        coursesOffered = newList;
    }
    public String getAll(){
        String coursesOffere = "";
        for(int i = 0; i < coursesOffered.size(); i++){
            coursesOffere = coursesOffere + coursesOffered.get(i);
        }

        return "ID: " + iD + ", Title: " + title + ", Name: " + name + ", Degree: " + degree + ", Research Interest: " + researchIntrest + ", Email: " + email + ", Office Location: " + officeLocation + ", Courses Offered: " + coursesOffere + ", Password: " + password;
    }
}
