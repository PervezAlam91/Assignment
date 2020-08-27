package com.allianz.assignment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.allianz.assignment.entity.Employee;
import com.allianz.assignment.exception.ResourceNotFoundException;
import com.allianz.assignment.repo.EmployeeRepository;
import com.allianz.assignment.service.EmployeeService;


/**
* EmployeeController defines all employees request mapping here 
* @author  Pervez Alam
* @version 1.0
* @since   26/08/2020 
*/
@RestController
public class EmployeeController {

	@Autowired
	EmployeeService empService;

	//Mapping to get all employee's 
	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {
		return empService.getAll();
	}
	

	/**
	 * It will return emploee based on id
	 * @param employeeId
	 * @return Employee object
	 * @throws ResourceNotFoundException
	 */
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		Employee employee = empService.getById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
		return ResponseEntity.ok().body(employee);
	}
	
	
	/**
	 * Used to create employee data
	 * @param employee
	 * @return saved employee
	 */
	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee employee) {
		return empService.save(employee);
	}
	

	/**
	 * Used to update existing employee
	 * @param employeeId
	 * @param employeeDetails
	 * @return Updated employee
	 * @throws ResourceNotFoundException
	 */
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
			 @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
		Employee employee = empService.getById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		employee.setEmailId(employeeDetails.getEmailId());
		employee.setLastName(employeeDetails.getLastName());
		employee.setFirstName(employeeDetails.getFirstName());
		Employee updatedEmployee = empService.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}
	
	
	/**
	 * Used to delete employee
	 * @param employeeId
	 * @return
	 * @throws ResourceNotFoundException
 	*/
	@DeleteMapping("/employees/{id}")
	public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId)
			throws ResourceNotFoundException {
		Employee employee = empService.getById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		empService.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
