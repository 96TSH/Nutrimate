package sg.edu.ntu.nutrimate.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.query.Param;

import sg.edu.ntu.nutrimate.entity.Course;
import sg.edu.ntu.nutrimate.entity.CourseRegistration;

public interface CourseService {
    List<Course> searchCourse(String cuisine, String difficulty);

    CourseRegistration addCourseCustomer(int courseId, LocalDate courseDate);
    
    List<String> getAllCoursesCustomer();

    CourseRegistration updateCourseDate(int courseId, LocalDate courseDate);

    void withdrawCourseCustomer(int courseId);

    void withdrawAllCoursesCustomer();

    Course createCourseAdmin(Course course);

    Course getCourseAdmin(int courseId);

    Course updateCourseAdmin(int courseId, Course course);

    void deleteCourseAdmin(int courseId);
}
