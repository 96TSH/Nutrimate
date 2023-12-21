package sg.edu.ntu.nutrimate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import sg.edu.ntu.nutrimate.entity.Address;
import sg.edu.ntu.nutrimate.entity.Course;
import sg.edu.ntu.nutrimate.entity.CourseRegistration;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.repository.CourseRegistrationRepository;
import sg.edu.ntu.nutrimate.repository.CourseRepository;
import sg.edu.ntu.nutrimate.repository.CustomerRepository;
import sg.edu.ntu.nutrimate.service.CourseServiceImpl;

@SpringBootTest
public class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CourseRegistrationRepository courseRegistrationRepository;

    @InjectMocks
    CourseServiceImpl courseService;

    private Customer customer1;

    @BeforeEach
    public void setUp() throws Exception {
        Address address = new Address("123", "Hogwarts Drive", "01-01", "Hogwarts School", "123456");
        customer1 = new Customer(1, "Harry", "Potter", "harrypotter@hogwarts.com", "81234567", "harrypotter", "harrypw",
                "user",
                address);
        customerRepository.save(customer1);
    }

    @Test
    public void addCourseCustomerTest() {

        // Arrange - mock the data
        Course selectedCourse = new Course(1, "Mala", "January", "All things Mala", "Chinese", "Intermediate");

        CourseRegistration newCourseRegistration = new CourseRegistration(customer1, selectedCourse, LocalDate.of(2023, 01, 21));

        when(courseRepository.findById(1)).thenReturn(Optional.of(selectedCourse));
        when(courseRegistrationRepository.save(newCourseRegistration)).thenReturn(newCourseRegistration);

        // Act
        CourseRegistration savedCourseRegistration = courseService.addCourseCustomer(1, LocalDate.of(2023, 01, 21));
        
        // Assert
        verify(courseRegistrationRepository, times(1)).save(newCourseRegistration);
        assertEquals(newCourseRegistration, savedCourseRegistration);
    }

}
