package sg.edu.ntu.nutrimate.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import sg.edu.ntu.nutrimate.entity.Course;
import sg.edu.ntu.nutrimate.entity.CourseRegistration;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.exception.CourseNotFoundException;
import sg.edu.ntu.nutrimate.exception.CourseRegistrationNotFoundException;
import sg.edu.ntu.nutrimate.exception.CustomerNotFoundException;
import sg.edu.ntu.nutrimate.repository.CourseRegistrationRepository;
import sg.edu.ntu.nutrimate.repository.CourseRepository;
import sg.edu.ntu.nutrimate.repository.CustomerRepository;
import sg.edu.ntu.nutrimate.security.AuthenticatedUser;
import sg.edu.ntu.nutrimate.repository.RecipeRepository;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;
    private CustomerRepository customerRepository;
    private CourseRegistrationRepository courseRegistrationRepository;

    public CourseServiceImpl(CourseRepository courseRepository, CustomerRepository customerRepository,
            CourseRegistrationRepository courseRegistrationRepository) {
        this.courseRepository = courseRepository;
        this.customerRepository = customerRepository;
        this.courseRegistrationRepository = courseRegistrationRepository;
    }

    // Search for courses
    @Override
    public List<Course> searchCourse(String cuisine, String difficulty) {
        List<Course> findCourse = courseRepository.findAll();
        // ArrayList<Course> findCourse = new ArrayList<>(courses);
        if (cuisine != null) {
            findCourse.removeIf(course -> !course.getCourseCuisine().equals(cuisine));
        }

        if (difficulty != null) {
            findCourse.removeIf(course -> !course.getCourseDifficulty().equals(difficulty));
        }

        return findCourse;
    }

    // Add course to customer
    @Override
    public CourseRegistration addCourseCustomer(int courseId, LocalDate courseDate) {
        Customer retrievedCustomer = retrieveCustomer();

        Course selectedCourse = selectCourse(courseId);

        CourseRegistration newCourseRegistration = new CourseRegistration();

        newCourseRegistration.setCourse(selectedCourse);
        newCourseRegistration.setCustomer(retrievedCustomer);
        newCourseRegistration.setCourseDate(courseDate);

        // selectedCourse.setCourseRegistration(newCourseRegistration);

        return courseRegistrationRepository.save(newCourseRegistration);
    }

    // Get all customer courses
    @Override
    public List<String> getAllCoursesCustomer() {
        Customer retrievedCustomer = retrieveCustomer();

        List<CourseRegistration> customerCourses = courseRegistrationRepository.findAll();

        customerCourses.removeIf(course -> course.getCustomer().getUserID() != retrievedCustomer.getUserID());

        if (customerCourses.size() == 0) {
            throw new CourseNotFoundException();
        }

        ArrayList<String> customerCourseNames = new ArrayList<>();

        for (CourseRegistration course : customerCourses) {
            customerCourseNames.add(course.getCourse().getCourseName());
        }

        return (List<String>) customerCourseNames;
    }

    // Update course for customer
    @Override
    public CourseRegistration updateCourseDate(int courseId, LocalDate courseDate) {
        Customer retrievedCustomer = retrieveCustomer();

        Course selectedCourse = selectCourse(courseId);

        Optional<CourseRegistration> wrappedSelectedCourseRegistration = courseRegistrationRepository
                .findByCustomerAndCourse(retrievedCustomer, selectedCourse);

        if (!wrappedSelectedCourseRegistration.isPresent()) {
            throw new CourseRegistrationNotFoundException(retrievedCustomer.getUserID(), courseId);
        }

        CourseRegistration selectedCourseRegistration = wrappedSelectedCourseRegistration.get();

        selectedCourseRegistration.setCourseDate(courseDate);

        return courseRegistrationRepository.save(selectedCourseRegistration);
    }

    // Delete course from customer
    @Override
    public void withdrawCourseCustomer(int courseId) {
        Customer retrievedCustomer = retrieveCustomer();

        Course selectedCourse = selectCourse(courseId);

        Optional<CourseRegistration> wrappedSelectedCourseRegistration = courseRegistrationRepository
                .findByCustomerAndCourse(retrievedCustomer, selectedCourse);

        if (!wrappedSelectedCourseRegistration.isPresent()) {
            throw new CourseRegistrationNotFoundException(retrievedCustomer.getUserID(), courseId);
        }

        CourseRegistration selectedCourseRegistration = wrappedSelectedCourseRegistration.get();

        courseRegistrationRepository.delete(selectedCourseRegistration);
    }

    // Delete all courses from customer
    @Override
    public void withdrawAllCoursesCustomer() {
        Customer retrievedCustomer = retrieveCustomer();

        List<CourseRegistration> allCourseRegistration = courseRegistrationRepository
                .findAllByCustomer(retrievedCustomer);

        if (allCourseRegistration.isEmpty()) {
            throw new CourseRegistrationNotFoundException(retrievedCustomer.getUserID());
        }

        for (CourseRegistration course : allCourseRegistration) {
            courseRegistrationRepository.delete(course);
        }

    }

    // Admin: Create course
    @Override
    public Course createCourseAdmin(Course course) {
        return courseRepository.save(course);
    }

    // Admin: Get one course
    @Override
    public Course getCourseAdmin(int courseId) {
        return selectCourse(courseId);
    }

    // Admin: Update course
    @Override
    public Course updateCourseAdmin(int courseId, Course course) {
        Course courseToUpdate = selectCourse(courseId);

        courseToUpdate.setCourseName(course.getCourseName());
        courseToUpdate.setCourseMonth(course.getCourseMonth());
        courseToUpdate.setCourseDesc(course.getCourseDesc());
        courseToUpdate.setCourseCuisine(course.getCourseCuisine());
        courseToUpdate.setCourseDifficulty(course.getCourseDifficulty());

        return courseRepository.save(courseToUpdate);
    }

    // Admin: Delete course
    @Override
    public void deleteCourseAdmin(int courseId) {
        courseRepository.deleteById(courseId);
    }

    private Course selectCourse(int courseId) {
        Optional<Course> wrappedSelectedCourse = courseRepository.findById(courseId);

        if (!wrappedSelectedCourse.isPresent()) {
            throw new CourseNotFoundException(courseId);
        }

        return wrappedSelectedCourse.get();
    }

    private Customer retrieveCustomer() {
        String username = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();

        Optional<Customer> customerContainer = customerRepository.findByUserID(username.toLowerCase());

        if (!customerContainer.isPresent()) {
            customerContainer = customerRepository.findByEmail(username.toLowerCase());
        }

        if (!customerContainer.isPresent()) {
            throw new BadCredentialsException("Invalid Username");
        }

        return customerContainer.get();
    }

}