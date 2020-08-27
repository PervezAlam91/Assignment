package com.allianz.assignment.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.allianz.assignment.entity.Employee;
import com.allianz.assignment.repo.EmployeeRepository;

/**
 * Repository for employee
 * @author Pervez Alam
 * @version 1.0
 * @since 26/08/2020
 */

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	@Autowired
    private EmployeeService empservice;

	
	/**
	 * Overriding loadUserByUsername
	 * Ignoring roles ,not needed for this assignment
	 * @param username
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = empservice.findByFirstName(username);
				
		
		if (employee!=null) {
			return new User(employee.getFirstName(), employee.getLastName(),
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

}