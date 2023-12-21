package sg.edu.ntu.nutrimate;

import static org.assertj.core.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.ntu.nutrimate.controller.CustomerController;
import sg.edu.ntu.nutrimate.entity.Address;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.exception.CustomerEntityNotUniqueException;
import sg.edu.ntu.nutrimate.repository.AdministratorRepository;
import sg.edu.ntu.nutrimate.repository.CustomerRepository;
import sg.edu.ntu.nutrimate.security.SecurityConfiguration;
import sg.edu.ntu.nutrimate.service.CustomerService;

@WebMvcTest(controllers=CustomerController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
// @AutoConfigureMockMvc(addFilters = false)
public class CustomerControllerTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	CustomerController customerController;

	@MockBean // To mock the service layer
	CustomerService mockService;

	@MockBean
	AdministratorRepository administratorRepository;

	@MockBean
	CustomerRepository customerRepository;

	@MockBean
	PasswordEncoder passwordEncoder;

    @Autowired
	private ObjectMapper objectMapper; //convert java objects to json and vice verse

    Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");

    // @BeforeEach
	// void setup() {
	// 	mockRepo.save(new Customer(1, "Tony", "Stark", "ts@avenger.com", "12345678", "tonny", "ironman", address));
	// 	mockRepo.save(new Customer(2, "Bruse", "Banner", "bban@avenger.com", "12345678", "burise", "hulky", address));
	// }

    @Test
	public void validCustomerAccountCreationTest() throws Exception {
		// Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "blackwin@avenger.com", "12345678", "natrom", "widowblackout", "user", address );
		String newCustomerAsJson = objectMapper.writeValueAsString(newCustomer);
		RequestBuilder request = MockMvcRequestBuilders.post("/nutrimate/public/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newCustomerAsJson);

		// Act and Assert
		mockMvc.perform(request)
				.andExpect(status().isCreated());
				// .andExpect(content().contentType(MediaType.APPLICATION_JSON))
				// .andExpect(jsonPath("$.id").value(3));
				// .andExpect(jsonPath("$.firstName").value("Natasha"))
				// .andExpect(jsonPath("$.lastName").value("romanoff"));
	}

	@Test
	public void InvalidCustomerFirstName() throws Exception {
		// Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "", "romanoff", "blackwin@avenger.com", "12345678", "natrom", "widowblackout", "user", address );
		String newCustomerAsJson = objectMapper.writeValueAsString(newCustomer);
		RequestBuilder request = MockMvcRequestBuilders.post("/nutrimate/public/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newCustomerAsJson);

		// Act and Assert
		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("First Name is mandatory, "));				
	}

	@Test
	public void InvalidCustomerLastName() throws Exception {
		// Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "", "blackwin@avenger.com", "12345678", "natrom", "widowblackout", "user", address );
		String newCustomerAsJson = objectMapper.writeValueAsString(newCustomer);
		RequestBuilder request = MockMvcRequestBuilders.post("/nutrimate/public/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newCustomerAsJson);

		// Act and Assert
		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Last Name is mandatory, "));				
	}

	@Test
	public void InvalidCustomerUserID() throws Exception {
		// Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "blackwin@avenger.com", "12345678", "", "widowblackout", "user", address );
		String newCustomerAsJson = objectMapper.writeValueAsString(newCustomer);
		RequestBuilder request = MockMvcRequestBuilders.post("/nutrimate/public/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newCustomerAsJson);

		// Act and Assert
		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("User ID is required, "));				
	}

	@Test
	public void InvalidCustomerPassword() throws Exception {
		// Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "blackwin@avenger.com", "12345678", "natrom", "", "user", address );
		String newCustomerAsJson = objectMapper.writeValueAsString(newCustomer);
		RequestBuilder request = MockMvcRequestBuilders.post("/nutrimate/public/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newCustomerAsJson);

		// Act and Assert
		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Password must be at least 8 characters, Password is mandatory, "));				
	}

	@Test
	public void illegalCustomerEmailFormat() throws Exception {
		// Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "black|win@avenger.com", "12345678", "natrom", "widowblackout", "user", address );
		String newCustomerAsJson = objectMapper.writeValueAsString(newCustomer);
		RequestBuilder request = MockMvcRequestBuilders.post("/nutrimate/public/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newCustomerAsJson);

		// Act and Assert
		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Valid Email is required, "));				
	}

	@Test
	public void InvalidCustomerEmailLocalName() throws Exception {
		// Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "@avenger.com", "12345678", "natrom", "widowblackout", "user", address );
		String newCustomerAsJson = objectMapper.writeValueAsString(newCustomer);
		RequestBuilder request = MockMvcRequestBuilders.post("/nutrimate/public/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newCustomerAsJson);

		// Act and Assert
		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Valid Email is required, "));				
	}

	@Test
	public void InvalidCustomerEmailDomain() throws Exception {
		// Arrange - Mock the data
        Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
        Customer newCustomer = new Customer(3, "Natasha", "romanoff", "blackwin@", "12345678", "natrom", "widowblackout", "user", address );
		String newCustomerAsJson = objectMapper.writeValueAsString(newCustomer);
		RequestBuilder request = MockMvcRequestBuilders.post("/nutrimate/public/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(newCustomerAsJson);

		// Act and Assert
		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Valid Email is required, "));				
	}
	
}
