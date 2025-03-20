package universitymanagementsystem;

import java.util.ArrayList;
import java.util.List;

public class Faculty {
    private String name;
    private String degree;
    private String researchInterest;
    private List<String> coursesOffered;
    private String email;
    private String password;
    private String officeLocation;

    // Constructor with Name Initialization
    public Faculty(String name) {
        this.name = name;
        this.email = generateEmail(name);
        this.coursesOffered = new ArrayList<>();
        this.password = "default123";
        this.degree = "Not Specified";
        this.researchInterest = "Not Specified";
        this.officeLocation = "Not Specified";
    }

    // Default Constructor
    public Faculty() {
        this.name = "Unknown";
        this.email = "unknown@university.com";
        this.coursesOffered = new ArrayList<>();
        this.password = "default123";
        this.degree = "Not Specified";
        this.researchInterest = "Not Specified";
        this.officeLocation = "Not Specified";
    }

    // Getter and Setter for Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
            this.email = generateEmail(name); // Update email based on new name
        }
    }

    // Getter and Setter for Degree
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        if (degree != null && !degree.isEmpty()) {
            this.degree = degree;
        }
    }

    // Getter and Setter for Research Interest
    public String getResearchInterest() {
        return researchInterest;
    }

    public void setResearchInterest(String researchInterest) {
        if (researchInterest != null && !researchInterest.isEmpty()) {
            this.researchInterest = researchInterest;
        }
    }

    // Getter and Setter for Courses Offered
    public List<String> getCoursesOffered() {
        return coursesOffered;
    }

    public void addCourse(String course) {
        if (course != null && !course.isEmpty()) {
            this.coursesOffered.add(course);
        }
    }

    public void removeCourse(String course) {
        this.coursesOffered.remove(course);
    }

    // Getter and Setter for Email (Readonly)
    public String getEmail() {
        return email;
    }

    // Getter and Setter for Password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null && password.length() >= 6) {
            this.password = password;
        }
    }

    // Getter and Setter for Office Location
    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        if (officeLocation != null && !officeLocation.isEmpty()) {
            this.officeLocation = officeLocation;
        }
    }

    // Helper Method to Generate Email
    private String generateEmail(String name) {
        String[] split = name.split(" ");
        if (split.length == 1) {
            return split[0].toLowerCase() + "@university.com";
        } else {
            return split[0].toLowerCase() + "." + split[1].toLowerCase() + "@university.com";
        }
    }

    // toString Method
    @Override
    public String toString() {
        return "Faculty Details:\n" +
                "- Name: " + name + "\n" +
                "- Degree: " + degree + "\n" +
                "- Research Interest: " + researchInterest + "\n" +
                "- Courses Offered: " + coursesOffered + "\n" +
                "- Email: " + email + "\n" +
                "- Office Location: " + officeLocation;
    }
}
