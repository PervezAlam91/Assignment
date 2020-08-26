package com.allianz.assignment.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.allianz.assignment.entity.Employee;


/**
 * Repository for employee
 * @author Pervez Alam
 * @version 1.0
 * @since 26/08/2020
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	 Employee findByFirstName(@Param("firstName") String name);

}
