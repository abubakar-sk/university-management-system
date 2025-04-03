package universitymanagementsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
    private String courseName;
    private String courseCode;
    private String subjectName;
    private List<String> sectionNumbers;
    private String teacherName;
    private int capacity;
    private String lectureTime;
    private String finalExamDateTime;
    private String location;

    // Constructor
    public Course(String courseName, String courseCode, String subjectName, String teacherName, int capacity, String lectureTime, String finalExamDateTime, String location) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.subjectName = subjectName;
        this.sectionNumbers = new ArrayList<>();
        this.teacherName = teacherName;
        this.capacity = capacity > 0 ? capacity : 0; // Ensure non-negative capacity
        this.lectureTime = lectureTime;
        this.finalExamDateTime = finalExamDateTime;
        this.location = location;
    }

    // Getters and Setters with Validation
    public String getCourseName() { return courseName; }

    public void setCourseName(String courseName) {
        if (courseName != null && !courseName.isEmpty()) {
            this.courseName = courseName;
        }
    }

    public String getCourseCode() { return courseCode; }

    public void setCourseCode(String courseCode) {
        if (courseCode != null && !courseCode.isEmpty()) {
            this.courseCode = courseCode;
        }
    }

    public String getSubjectName() { return subjectName; }

    public void setSubjectName(String subjectName) {
        if (subjectName != null && !subjectName.isEmpty()) {
            this.subjectName = subjectName;
        }
    }

    public List<String> getSectionNumbers() { return sectionNumbers; }

    public void addSectionNumber(String sectionNumber) {
        if (sectionNumber != null && !sectionNumber.isEmpty()) {
            this.sectionNumbers.add(sectionNumber);
        }
    }

    public String getTeacherName() { return teacherName; }

    public void setTeacherName(String teacherName) {
        if (teacherName != null && !teacherName.isEmpty()) {
            this.teacherName = teacherName;
        }
    }

    public int getCapacity() { return capacity; }

    public void setCapacity(int capacity) {
        if (capacity >= 0) { // Ensure non-negative capacity
            this.capacity = capacity;
        }
    }

    public String getLectureTime() { return lectureTime; }

    public void setLectureTime(String lectureTime) {
        if (lectureTime != null && !lectureTime.isEmpty()) {
            this.lectureTime = lectureTime;
        }
    }

    public String getFinalExamDateTime() { return finalExamDateTime; }

    public void setFinalExamDateTime(String finalExamDateTime) {
        if (finalExamDateTime != null && !finalExamDateTime.isEmpty()) {
            this.finalExamDateTime = finalExamDateTime;
        }
    }

    public String getLocation() { return location; }

    public void setLocation(String location) {
        if (location != null && !location.isEmpty()) {
            this.location = location;
        }
    }

    // Overriding equals and hashCode for better comparison and usage in collections
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseCode, course.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode);
    }

    // Improved toString Method
    @Override
    public String toString() {
        return "Course Details:\n" +
                "- Name: " + courseName + "\n" +
                "- Code: " + courseCode + "\n" +
                "- Subject: " + subjectName + "\n" +
                "- Sections: " + sectionNumbers + "\n" +
                "- Instructor: " + teacherName + "\n" +
                "- Capacity: " + capacity + "\n" +
                "- Lecture Time: " + lectureTime + "\n" +
                "- Final Exam: " + finalExamDateTime + "\n" +
                "- Location: " + location;
    }
}
