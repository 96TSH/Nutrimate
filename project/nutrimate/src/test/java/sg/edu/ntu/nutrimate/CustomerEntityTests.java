package sg.edu.ntu.nutrimate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;


import sg.edu.ntu.nutrimate.entity.Address;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.repository.CustomerRepository;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class CustomerEntityTests {

    @Autowired
    CustomerRepository customerRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
    
    @BeforeEach
	void setup() {
		customerRepository.save(new Customer(1, "Tony", "Stark", "ts@avenger.com", "12345678", "tonny", passwordEncoder.encode("ironman"), "user", address));
		customerRepository.save(new Customer(2, "Bruse", "Banner", "bban@avenger.com", "12345678", "burise", passwordEncoder.encode("hulky"), "user", address));
	}

    @Test
    public void CustomerEntityValidationPassed() {
        // Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "blackwin@avenger.com", "12345678", "natashaRoman", passwordEncoder.encode("widowblackout"), "user", address );
  
        // Act and Assert
        customerRepository.save(newCustomer);

    }

    @Test
    public void CustomerEntityFirstNameisBlank() {
        // Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "", "romanoff", "blackwin@avenger.com", "12345678", "natrom", passwordEncoder.encode("widowblackout"), "user", address );
  
        // Act and assert
        // This exception - TransactionSystemException to be thrown by Bean Validation
        // the pre-persist entity lifecycle event triggered the bean validation just before sending the query to the database.
        // out of the box, Hibernate translates the bean validation annotations applied to the entities into the DDL schema metadata.
        // When a JPA entity gets saved by Hibernate, 
        // Bean Validation validates that entity is correct: it is because this validation fails that the entity is not saved, and Hibernate rolls back the transaction

        assertThrows(ConstraintViolationException.class, () -> {
			ValidationInjection(newCustomer);
		});
        
        try {
            ValidationInjection(newCustomer);
        } catch (ConstraintViolationException e) {
            assertEquals("First Name is mandatory, ", e.getMessage());
        }

    }

    @Test
    public void CustomerEntityLastNameisBlank() {
        // Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "", "blackwin@avenger.com", "12345678", "natrom", passwordEncoder.encode("widowblackout"), "user", address );
  
        // Act and assert
        
        assertThrows(ConstraintViolationException.class, () -> {
			ValidationInjection(newCustomer);
		});
        
        try {
            ValidationInjection(newCustomer);
        } catch (ConstraintViolationException e) {
            assertEquals("Last Name is mandatory, ", e.getMessage());
        }
    }

    @Test
    public void CustomerEntityEmailisNotUnique() {
        // Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "ts@avenger.com", "12345678", "tonny", passwordEncoder.encode("widowblackout"), "user", address );
  
        // Act and assert
        assertThrows(DataIntegrityViolationException.class, () ->{
            customerRepository.save(newCustomer);  
        });
           
    }

    @Test
    public void CustomerEntityEmailisInvalid() {
        // Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "Natasha", "12345678", "tonny", passwordEncoder.encode("widowblackout"), "user", address );
  
        // Act and assert
        assertThrows(ConstraintViolationException.class, () -> {
			ValidationInjection(newCustomer);
		});
        
        try {
            ValidationInjection(newCustomer);
        } catch (ConstraintViolationException e) {
            assertEquals("Valid Email is required, ", e.getMessage());
        }
           
    }

    @Test
    public void CustomerEntityEmailPipeCharNotAllowed() {
        // Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "Nata|sha@avenger.co", "12345678", "tonny", passwordEncoder.encode("widowblackout"), "user", address );
  
        // Act and assert
        assertThrows(ConstraintViolationException.class, () -> {
			ValidationInjection(newCustomer);
		});
        
        try {
            ValidationInjection(newCustomer);
        } catch (ConstraintViolationException e) {
            assertEquals("Valid Email is required, ", e.getMessage());
        }
           
    }

    @Test
    public void CustomerEntityEmailSingelQuoteCharNotAllowed() {
        // Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "Natasha@avenger'.co", "12345678", "tonny", passwordEncoder.encode("widowblackout"), "user", address );
  
        // Act and assert
        assertThrows(ConstraintViolationException.class, () -> {
			ValidationInjection(newCustomer);
		});
        
        try {
            ValidationInjection(newCustomer);
        } catch (ConstraintViolationException e) {
            assertEquals("Valid Email is required, ", e.getMessage());
        }
           
    }

    @Test
    public void CustomerEntityEmailInvalidLocalname() {
        // Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "@avenger.co", "12345678", "tonny", passwordEncoder.encode("widowblackout"), "user", address );
  
        // Act and assert
        assertThrows(ConstraintViolationException.class, () -> {
			ValidationInjection(newCustomer);
		});
        
        try {
            ValidationInjection(newCustomer);
        } catch (ConstraintViolationException e) {
            assertEquals("Valid Email is required, ", e.getMessage());
        }
           
    }

    @Test
    public void CustomerEntityEmailInvalidDomain() {
        // Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "natasha@", "12345678", "tonny", passwordEncoder.encode("widowblackout"), "user", address );
  
        // Act and assert
        assertThrows(ConstraintViolationException.class, () -> {
			ValidationInjection(newCustomer);
		});
        
        try {
            ValidationInjection(newCustomer);
        } catch (ConstraintViolationException e) {
            assertEquals("Valid Email is required, ", e.getMessage());
        }           
    }

    @Test
    public void CustomerEntityUserIDisBlank() {
        // Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "blackwin@avenger.com", "12345678", "", passwordEncoder.encode("widowblackout"), "user", address );
  
        // Act and assert
        
        assertThrows(ConstraintViolationException.class, () -> {
			ValidationInjection(newCustomer);
		});
        
        try {
            ValidationInjection(newCustomer);
        } catch (ConstraintViolationException e) {
            assertEquals("User ID is required, ", e.getMessage());
        }

    }

    @Test
    public void CustomerEntityUserIDisNotUnique() {
        // Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "blackwin@avenger.com", "12345678", "tonny", passwordEncoder.encode("widowblackout"), "user", address );
  
        // Act and assert
        assertThrows(DataIntegrityViolationException.class, () ->{
            customerRepository.save(newCustomer);  
        });
           
    }

    @Test
    public void CustomerEntityPasswordisBlank() {
        // Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "blackwin@avenger.com", "12345678", "natrom", "", "user", address );
  
        // Act and assert
        
        assertThrows(ConstraintViolationException.class, () -> {
			ValidationInjection(newCustomer);
		});
        
        try {
            ValidationInjection(newCustomer);
        } catch (ConstraintViolationException e) {
            assertEquals("Password is mandatory, Password must be at least 8 characters, ", e.getMessage());
        }

    }

    @Test
    public void CustomerEntityContactCanBeEmpty() {
        // Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "blackwin@avenger.com", "", "natrom", passwordEncoder.encode("widowblackout"), "user", address );
  
        // Act and Assert
        customerRepository.save(newCustomer);
        
    }

    @Test
    public void CustomerEntityAddressCanBeNull() {
        // Arrange - Mock the data
        Address address = new Address(null, null, null, null, null);
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "blackwin@avenger.com", "", "natrom", passwordEncoder.encode("widowblackout"), "user", address );
  
        // Act and Assert
        customerRepository.save(newCustomer);
        
    }

    private void ValidationInjection (Customer customer) {
        StringBuilder stringBuilder = new StringBuilder();
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        if (!violations.isEmpty()) {
            for(ConstraintViolation<Customer> violation : violations ){
                stringBuilder.append(violation.getMessageTemplate() + ", ");                
            }

            throw new ConstraintViolationException(stringBuilder.toString(), violations);
        }
  }
    
}
