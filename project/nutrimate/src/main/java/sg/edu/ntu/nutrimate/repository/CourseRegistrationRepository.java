package sg.edu.ntu.nutrimate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.ntu.nutrimate.entity.Course;
import sg.edu.ntu.nutrimate.entity.CourseRegistration;
import sg.edu.ntu.nutrimate.entity.Customer;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Integer> {
    Optional<CourseRegistration> findByCustomerAndCourse(Customer customer, Course course);
    List<CourseRegistration> findAllByCustomer(Customer customer);
    void deleteByCustomer(Customer customer);
    void deleteAllByCustomer(Customer customer);   
}
