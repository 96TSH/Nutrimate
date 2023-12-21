package sg.edu.ntu.nutrimate.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "Course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private int courseId;

    @Column(name = "course_name", nullable = false)
    @NotBlank(message = "Course Name is mandatory")
    private String courseName;

    @Column(name = "course_month", nullable = false)
    @NotBlank(message = "Course Month is mandatory")
    private String courseMonth;

    @Column(name = "course_desc", nullable = false)
    @NotBlank(message = "Course Description is mandatory")
    private String courseDesc;

    @Column(name = "course_cuisine", nullable = false)
    @NotBlank(message = "Cuisine is mandatory")
    private String courseCuisine;

    @Column(name = "course_difficulty")
    @NotBlank(message = "Difficulty is mandatory")
    private String courseDifficulty;

    @OneToMany(mappedBy = "course")
    private Set<CourseRegistration> courseRegistration;

    public Course() {
    }

    public Course(int courseId, String courseName, String courseMonth, String courseDesc, String courseCuisine,
            String courseDifficulty) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseMonth = courseMonth;
        this.courseDesc = courseDesc;
        this.courseCuisine = courseCuisine;
        this.courseDifficulty = courseDifficulty;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseMonth() {
        return this.courseMonth;
    }

    public void setCourseMonth(String courseMonth) {
        this.courseMonth = courseMonth;
    }

    public String getCourseDesc() {
        return this.courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    // public ArrayList<String> getCourseCategory() {
    // return this.courseCategory;
    // }

    // public void setCourseCategory(ArrayList<String> courseCategory) {
    // this.courseCategory = courseCategory;
    // }

    public String getCourseCuisine() {
        return this.courseCuisine;
    }

    public void setCourseCuisine(String courseCuisine) {
        this.courseCuisine = courseCuisine;
    }

    public String getCourseDifficulty() {
        return this.courseDifficulty;
    }

    public void setCourseDifficulty(String courseDifficulty) {
        this.courseDifficulty = courseDifficulty;
    }
}