package sg.edu.ntu.nutrimate.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.ntu.nutrimate.entity.Course;
import sg.edu.ntu.nutrimate.entity.CourseRegistration;
import sg.edu.ntu.nutrimate.service.CourseService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/nutrimate")
public class CourseController {
    
    @Autowired
    private CourseService courseService;

    // Search for courses
    @GetMapping("/public/course")
    public ResponseEntity<List<Course>> searchCourse(@RequestParam(required = false) String cuisine, @RequestParam(required = false) String difficulty) {
        List<Course> searchCourse = courseService.searchCourse(cuisine, difficulty);
        return new ResponseEntity<>(searchCourse, HttpStatus.OK);
    }

    // Add course to customer
    @PostMapping("/customers/course/{courseId}")
    public ResponseEntity<String> addCourseCustomer(@PathVariable int courseId, @Valid @RequestBody CourseDate courseDate) {
        CourseRegistration newCourseRegistration = courseService.addCourseCustomer(courseId, courseDate.getCourseDate());
        return new ResponseEntity<>("Course registration successful: " + newCourseRegistration.getCourse().getCourseName(), HttpStatus.CREATED);
    }

    // Get all customer courses
    @GetMapping("/customers/course")
    public ResponseEntity<List<String>> getAllCoursesCustomer() {
        List<String> allCourses = courseService.getAllCoursesCustomer();
        return new ResponseEntity<>(allCourses, HttpStatus.OK);
    }

    // Update course for customer
    @PutMapping("/customers/course/{courseId}")
    public ResponseEntity<String> updateCourseCustomer(@PathVariable int courseId, @Valid @RequestBody CourseDate courseDate) {
        CourseRegistration updateCourse = courseService.updateCourseDate(courseId, courseDate.getCourseDate());
        return new ResponseEntity<>("Course date successfully changed to: " + updateCourse.getCourseDate(), HttpStatus.OK);
    }

    // Delete course from customer
    @DeleteMapping("/customers/course/{courseId}")
    public ResponseEntity<HttpStatus> withdrawCourseCustomer(@PathVariable int courseId) {
        courseService.withdrawCourseCustomer(courseId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Delete all courses from customer
    @DeleteMapping("/customers/course")
    public ResponseEntity<HttpStatus> withdrawAllCoursesCustomer() {
        courseService.withdrawAllCoursesCustomer();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Admin: Create course
    @PostMapping("/admin/course")
    public ResponseEntity<Course> createCourseAdmin(@Valid @RequestBody Course course) {
        Course newCourse = courseService.createCourseAdmin(course);
        return new ResponseEntity<>(newCourse, HttpStatus.OK);
    }

    // Admin: Get one course
    @GetMapping("/admin/course/{courseId}")
    public ResponseEntity<Course> getCourseAdmin(@PathVariable int courseId) {
        Course selectedCourse = courseService.getCourseAdmin(courseId);
        return new ResponseEntity<>(selectedCourse, HttpStatus.OK);
    }

    // Admin: Update course
    @PutMapping("/admin/course/{courseId}")
    public ResponseEntity<Course> updateCourseAdmin(@PathVariable int courseId, @Valid @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourseAdmin(courseId, course);
        return new ResponseEntity<Course>(updatedCourse, HttpStatus.OK);
    }

    // Admin: Delete course
    @DeleteMapping("/admin/course/{courseId}")
    public ResponseEntity<HttpStatus> deleteCourseAdmin(@PathVariable int courseId) {
        courseService.deleteCourseAdmin(courseId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

class CourseDate {
    LocalDate courseDate;

    public LocalDate getCourseDate() {
        return this.courseDate;
    }

    public void setCourseDate(LocalDate courseDate) {
        this.courseDate = courseDate;
    }

}