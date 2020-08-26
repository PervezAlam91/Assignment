package com.allianz.assignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;

import com.allianz.assignment.entity.Employee;
import com.allianz.assignment.model.JwtRequest;
import com.allianz.assignment.model.JwtResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.status.Status;

/**
 * EmployeeIntegrationTest tests according to allianz assignment 
 * @author Pervez Alam
 * @version 1.0
 * @since 26/08/2020
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTest {
	
	@Autowired
	private TestRestTemplate restTemplate;

	private static String generatedToken = null;
	
	private static int tokenCount=0;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@LocalServerPort
	private int port;

	//to use random ports
	private String getRootUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void contextLoads() {

	}

	
	//Verify authentication and generates jwt token
	@Test
	public void testAuthentication() {
		
		//tokenCount is used to generate token once only
		if(tokenCount==0) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		//creating authentication request
		JwtRequest req = new JwtRequest();
		req.setUsername("alam");
		req.setPassword("alam");

		ResponseEntity<String> postResponse = restTemplate.postForEntity(getRootUrl() + "/authenticate", req,
				String.class);
		
		String generatedToken = postResponse.getBody().replace("{\"jwttoken\":\"", "").replace("\"}", "");;
		
		//Set token to used by other tests 
		this.generatedToken = generatedToken;
		tokenCount++;
		}
		assertNotNull(generatedToken);
	}

	
	//verify get all employee methods
	@Test
	public void testGetAllEmployees() {
		//if token is not yet generated than generate token first
		if (this.generatedToken == null) {
			testAuthentication();
		}
		
		//get all employees
		HttpHeaders headers = new HttpHeaders();
		
		//sending jwt token in header with each request
		headers.add("Authorization", "Bearer " + this.generatedToken);
		
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/employees", HttpMethod.GET, entity,
				String.class);

		//response should not be null
		assertNotNull(response.getBody());
	}


	//verify creating employee
	@Test
	public void testCreateEmployee() throws Exception {
		//if token is not yet generated than generate token first
		if (this.generatedToken == null) {
			testAuthentication();
		}
		
		//create an employee
		Employee emp = new Employee();
		emp.setEmailId("Allianz@demo.com");
		emp.setFirstName("Allianz");
		emp.setLastName("Allianz");

		String jsonRequest = mapper.writeValueAsString(emp);

		//save employee
		//sending jwt token in header with each request
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/employees")
				.header("Authorization", "Bearer " + this.generatedToken).content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON);
		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk());

	}


	//verify getting by id
	@Test
	public void testGetEmployeeById() throws Exception {
		//if token is not yet generated than generate token first
		if (this.generatedToken == null) {
			testAuthentication();
		}
		HttpHeaders headers = new HttpHeaders();
		//sending jwt token in header with each request
		headers.add("Authorization", "Bearer " + this.generatedToken);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		//get employee by id
		ResponseEntity<Employee> response = restTemplate.exchange(getRootUrl() + "/employees/1000", HttpMethod.GET,
				entity, Employee.class);

		assertNotNull(response.getBody().getFirstName());

	}


	//verify updating employee
	@Test
	public void testUpdateEmployee() throws Exception {
		//if token is not yet generated than generate token first
		if (this.generatedToken == null) {
			testAuthentication();
		}
		
		//call to create a employee
		testCreateEmployee();
		
		int id = 1;
		HttpHeaders headers = new HttpHeaders();
		//setting jwt token with each request
		headers.add("Authorization", "Bearer " + this.generatedToken);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		//Verifying  employee already exist
		ResponseEntity<Employee> response = restTemplate.exchange(getRootUrl() + "/employees/" + id, HttpMethod.GET,
				entity, Employee.class);

		Employee employee = response.getBody();
		assertNotEquals("Palam", employee.getFirstName());
		//updating employee
		employee.setFirstName("Palam");
		employee.setLastName("java");
		
		//Converting into json
		String jsonRequest = mapper.writeValueAsString(employee);
		
		//verifying update status
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/employees/" + id)
				.header("Authorization", "Bearer " + this.generatedToken).content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON);
		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk());

		//Get again updated employee object
		ResponseEntity<Employee> Updatedresponse = restTemplate.exchange(getRootUrl() + "/employees/" + id,
				HttpMethod.GET, entity, Employee.class);

		//double check with updated values
		assertEquals("Palam", Updatedresponse.getBody().getFirstName());
		assertEquals("java", Updatedresponse.getBody().getLastName());
	}


	//verify delete employee
	@Test
	public void testDeleteEmployee() throws Exception {
		//if token is not yet generated than generate token first
		if (this.generatedToken == null) {
			testAuthentication();
		}
		
		//inserted through data.sql
		int id = 1001;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + this.generatedToken);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		//Getting employee
		ResponseEntity<Employee> response = restTemplate.exchange(getRootUrl() + "/employees/" + id, HttpMethod.GET,
				entity, Employee.class);

		//verifying employee exist before delete
		assertNotNull(response.getBody().getFirstName());

		//delete and verify employee using jwt token
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/employees/" + id)
				.header("Authorization", "Bearer " + this.generatedToken);
		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk());

		//try to get deleted employee,which should return null
		Employee deletedEmployee = restTemplate
				.exchange(getRootUrl() + "/employees/" + id, HttpMethod.GET, entity, Employee.class).getBody();

		//verify result
		assertNull(deletedEmployee.getFirstName());

	}

}
