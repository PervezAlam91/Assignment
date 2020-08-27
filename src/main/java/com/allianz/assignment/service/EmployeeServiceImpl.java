package com.allianz.assignment.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allianz.assignment.entity.Employee;
import com.allianz.assignment.repo.EmployeeRepository;


/**
 * Service class for employee
 * @author Pervez Alam
 * @version v1.0
 * 
 */
@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Employee save(Employee entity) {
		return employeeRepository.save(entity);
	}

	@Override
	public Optional<Employee> getById(Serializable id) {
		return employeeRepository.findById((Long) id);
	}

	@Override
	public List<Employee> getAll() {
		return employeeRepository.findAll();
	}

	@Override
	public void delete(Employee employee) {
		employeeRepository.delete(employee);
	}

	@Override
	public Employee findByFirstName(String firstName) {
		// TODO Auto-generated method stub
		return employeeRepository.findByFirstName(firstName);
	}
	

}
