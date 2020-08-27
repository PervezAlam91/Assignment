package com.allianz.assignment;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.core.Is.is;
import com.allianz.assignment.entity.Employee;
import com.allianz.assignment.model.JwtRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * EmployeeJunitTests tests according to allianz assignment
 * 
 * @author Pervez Alam
 * @version 1.0
 * @since 26/08/2020
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeJunitTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@LocalServerPort
	private int port;

	// to use random ports
	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {

	}

	// Verify authentication and generates jwt token
	@Test
	public void testAuthentication() {

		HttpHeaders headers = new HttpHeaders();

		// creating authentication request
		JwtRequest req = new JwtRequest();
		req.setUsername("alam");
		req.setPassword("alam");

		ResponseEntity<String> postResponse = restTemplate.postForEntity(getRootUrl() + "/authenticate", req,
				String.class);

		// get generated token
		String generatedToken = postResponse.getBody().replace("{\"jwttoken\":\"", "").replace("\"}", "");

		assertNotNull(generatedToken);
	}

	
	// verify get all employee methods
	@Test
	public void testGetAllEmployees() {
		// step 1::get generated token
		String generatedToken = getGeneratedToken();

		//sending jwt token in header with each request
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		headers.add("Authorization", "Bearer " + generatedToken);

		entity = new HttpEntity<String>(null, headers);

		//send get request
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/employees", HttpMethod.GET, entity,
				String.class);

		// response should not be null
		assertNotNull(response.getBody());
	}
	

	// verify creating employee
	@Test
	public void testCreateEmployee() throws Exception {
		// step 1::get generated token
		String generatedToken = getGeneratedToken();
		
		// create an employee
		Employee emp = new Employee();
		emp.setEmailId("Allianz@demo.com");
		emp.setFirstName("Allianz");
		emp.setLastName("Allianz");

		String jsonRequest = mapper.writeValueAsString(emp);

		// save employee
		// sending jwt token in header with each request
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/employees")
				.header("Authorization", "Bearer " + generatedToken).content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("firstName", is("Allianz")));

	}
	

	// verify getting by id
	@Test
	public void testGetEmployeeById() throws Exception {
		int id = 1000;// id to get employee
		// step 1::get generated token
		String generatedToken = getGeneratedToken();

		//sending jwt token in header with each request
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		headers.add("Authorization", "Bearer " + generatedToken);
		entity = new HttpEntity<String>(null, headers);

		// get employee by id
		ResponseEntity<Employee> response = restTemplate.exchange(getRootUrl() + "/employees/" + id, HttpMethod.GET,
				entity, Employee.class);

		assertNotNull(response.getBody().getFirstName());

	}
	

	// verify getting by id
	@Test
	public void testEmployeeNotExist() throws Exception {
		int id = 5000;// id to get employee
		// step 1::get generated token
		String generatedToken = getGeneratedToken();

		//sending jwt token in header with each request
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		headers.add("Authorization", "Bearer " + generatedToken);
		entity = new HttpEntity<String>(null, headers);

		// get employee by id
		ResponseEntity<Employee> response = restTemplate.exchange(getRootUrl() + "/employees/" + id, HttpMethod.GET,
				entity, Employee.class);

		assertNull(response.getBody().getFirstName());

	}

	
	
	

	// verify updating employee
	@Test
	public void testUpdateEmployee() throws Exception {
		int id = 1001;// id to get employee

		// step 1::get generated token
		String generatedToken = getGeneratedToken();

		//sending jwt token in header with each request
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		headers.add("Authorization", "Bearer " + generatedToken);
		entity = new HttpEntity<String>(null, headers);

		// get employee by id
		ResponseEntity<Employee> response = restTemplate.exchange(getRootUrl() + "/employees/" + id, HttpMethod.GET,
				entity, Employee.class);

		// get curreny employee and update
		Employee employee = response.getBody();
		assertNotEquals("Pervez Alam", employee.getFirstName());
		
		// updating employee
		employee.setFirstName("Pervez Alam");
		employee.setLastName("java");

		// Converting into json
		String jsonRequest = mapper.writeValueAsString(employee);

		// verifying update status and value
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/employees/" + id)
				.header("Authorization", "Bearer " + generatedToken).content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON);
		
		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("firstName", is("Pervez Alam")));

	}
	

	// verify delete employee
	@Test
	public void testDeleteEmployee() throws Exception {
		// id to get employee
		int id = 1002;
		
		// step 1::get generated token
		String generatedToken = getGeneratedToken();

		//sending jwt token in header with each request
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		headers.add("Authorization", "Bearer " + generatedToken);
		entity = new HttpEntity<String>(null, headers);

		// get employee by id
		ResponseEntity<Employee> response = restTemplate.exchange(getRootUrl() + "/employees/" + id, HttpMethod.GET,
				entity, Employee.class);

		// verifying employee exist before delete
		assertNotNull(response.getBody().getFirstName());

		// delete and verify employee using jwt token
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/employees/" + id)
				.header("Authorization", "Bearer " + generatedToken);
		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk());

		// try to get deleted employee,which should return null
		Employee deletedEmployee = restTemplate
				.exchange(getRootUrl() + "/employees/" + id, HttpMethod.GET, entity, Employee.class).getBody();

		// verify result
		assertNull(deletedEmployee.getFirstName());

	}

	
	/**Method used to get generate token to pass security
	 * @author Pervez Alam
	 * @version 1.0
	 * @return generated jwt tokan
	 */
	private String getGeneratedToken() {
		// creating authentication request
		JwtRequest req = new JwtRequest();
		req.setUsername("alam");
		req.setPassword("alam");

		ResponseEntity<String> postResponse = restTemplate.postForEntity(getRootUrl() + "/authenticate", req,
				String.class);

		// step 2::get generated token
		String generatedToken = postResponse.getBody().replace("{\"jwttoken\":\"", "").replace("\"}", "");
		
		return generatedToken;
	}

}
