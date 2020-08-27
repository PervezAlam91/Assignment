package com.allianz.assignment.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.allianz.assignment.entity.Employee;

/**
 * Service Interface 
 * @author Pervez Alam
 * @version 1.0
 */
public interface EmployeeService {
	
	Employee save(Employee emp);
	
	Employee findByFirstName(String firstName);

	Optional<Employee> getById(Serializable id);

	List<Employee> getAll();

	void delete(Employee employee);
	

}
