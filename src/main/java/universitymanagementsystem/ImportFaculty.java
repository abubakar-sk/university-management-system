package universitymanagementsystem;
import java.io.BufferedWriter;
import java.math.BigDecimal;
import java.lang.Integer;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class ImportFaculty {
    //static public
    static public List<String[]> getTwo = new ArrayList<String[]>();
    public static int reps = 0;
    List<String> iD = new ArrayList<String>();//{};
    List<String> title = new ArrayList<String>();
    List<String> name = new ArrayList<String>();
    List<String> degree = new ArrayList<String>();
    List<String> researchInterest = new ArrayList<String>();
    List<String> email = new ArrayList<String>();
    List<String> officeLocation = new ArrayList<String>();
    List<String> coursesOffered = new ArrayList<String>();
    List<String> password = new ArrayList<String>();
    List<String> inputGet = new ArrayList<String>();
    List<String> get = new ArrayList<String>();
    DataInputStream input = null;
    String inputName = "C:\\Users\\Brand\\Documents\\intelej10\\university-management-system\\src\\main\\java\\universitymanagementsystem\\facultyStorage.txt";
    //String outputName = "facultyStorage2.txt";
    File inFile = new File(inputName);
    public static void main(String[] args) {
        //public void ba(){
        //ImportFaculty d = new ImportFaculty();
//        int idVal = 498;
        //for(int i = 0; i < 1;){
//            for (int j = 1; j < 6;){
//                if (idVal/Math.pow(10, 4-j)>=Math.pow(10, j)){
//                    double f = idVal * Math.pow(10, 4-j);
//                    System.out.println(f);
//                    j = 90;
//
//                }
//                else{
//                    int u = (10^j);
//                    String h = "u" + u + "1u";
//                    String[] b = h.split("1");
//                    if (b[1].equals("u")){
//                        if (b[1].equals("")){
//
//                        }
//                        else{
//                            b[1] = "";
//                        }
//
//                    }
//                    String f = "F" + b[1] + idVal;// = idVal * Math.pow(10, 4-j);
//                    System.out.println(f);
//                    j = 90;
//
//                }
//            }

        //}


//        float d = (4/10000);
//        String e = ""+(4 / 10000);
//        String[] f = e.split(".");
//        String s = "F" + (4 / 10000);
//        String iD = s;
        //System.out.println(f);

    }

    public ImportFaculty(){

        try {
            input = new DataInputStream(
                    new BufferedInputStream(
                            new FileInputStream(
                                    inFile)));
            String n = input.readLine();
            for (int i = 0; n != null; i++) {


                System.out.println(n);
                get.add(n);
                //numOfStudents++;
                String[] spl = n.split(",");
                String[] splID = spl[0].split(": ");
                iD.add(splID[1]);
                String[] splTitle = spl[1].split("le: ");
                title.add(splTitle[1]);
                String[] splName = spl[2].split(": ");
                name.add(splName[1]);
                String[] splDegree = spl[3].split(": ");
                degree.add(splDegree[1]);
                String[] splResearchInterest = spl[4].split(": ");
                researchInterest.add(splResearchInterest[1]);
                String[] splEmail = spl[5].split(": ");
                email.add(splEmail[1]);
                String[] splOfficeLocation = spl[6].split(": ");
                officeLocation.add(splOfficeLocation[1]);

                String[] splCoursesOffered = spl[7].split(": ");
                coursesOffered.add(splCoursesOffered[1]);


                String[] splPassword = spl[8].split(": ");
                password.add(splPassword[1]);

                String g = iD.get(i) + "," + title.get(i) + "," + name.get(i) + "," + degree.get(i) + "," + researchInterest.get(i) + "," + email.get(i) + "," + officeLocation.get(i) + "," + coursesOffered.get(i) + "," + password.get(i);
                String[] h = g.split(",");
                getTwo.add(h);
                n = input.readLine();
                //get.add(iD + "," + title + "," + name + "," + degree + "," + researchInterest + "," + email + "," + officeLocation + "," + coursesOffered + "" + password);

                //String[] splHi = hi.split(",");


            }
//            for(int i = 0; i < getTwo.size(); i++){
//                System.out.println(getTwo.get(i)[0] +  getTwo.get(i)[1] + getTwo.get(i)[2] + getTwo.get(i)[3] + getTwo.get(i)[4] + getTwo.get(i)[5] + getTwo.get(i)[6] + getTwo.get(i)[7]);
//                //Faculty a = new Faculty(getTwo.get(9)[i], getTwo.get(0)[1], getTwo.get(9)[i], getTwo.get(0)[i], getTwo.get(9)[i], getTwo.get(0)[i], getTwo.get(9)[i], getTwo.get(0)[i]);//iD[0], "", "", "", "", "", "", ""
//                //in.add(a);
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Faculty> GetFac(){
        List<Faculty> in = new ArrayList<Faculty>();
       // String[] b;
        for(int i = 0; i < getTwo.size(); i++){
            String send = getTwo.get(i)[7];
            //System.out.println(getTwo.get(9)[i] +  getTwo.get(0)[1] + getTwo.get(9)[i] + getTwo.get(0)[i] + getTwo.get(9)[i] + getTwo.get(0)[i] + getTwo.get(9)[i] + getTwo.get(0)[i]);
            Faculty a = new Faculty(getTwo.get(i)[0], getTwo.get(i)[1], getTwo.get(i)[2], getTwo.get(i)[3], getTwo.get(i)[4], getTwo.get(i)[5], getTwo.get(i)[6], send, getTwo.get(i)[8]);//iD[0], "", "", "", "", "", "", ""
            //System.out.println(a);
            in.add(a);
        }



        return in;
    }
    //public void ChangeExport(Faculty fac){
  //      in.add
//    }
    public void ExportFacuty(List<Faculty> fac){
                try(BufferedWriter writer = new BufferedWriter(new FileWriter(inputName))){
                    for(int i = 0; i < fac.size(); i++){
                        writer.write(fac.get(i).getAll());// + ", Title: " + fac.get(1) +  ", Name: " + fac.get(2) + ", Degree: " + fac.get(3) + ", Research Interest: " + fac.get(4) + ", Email: " + fac.get(5) + ", Office Location: " + fac.get(6) + ", Courses Offered: " + fac.get(7), Password: default123);
                        if (i < fac.size() - 1){
                            writer.write("\n");
                        }
                    }

        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
}










