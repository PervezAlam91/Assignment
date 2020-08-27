package com.allianz.assignment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Employee is an entity class
 * @author Pervez Alam
 * @version 1.0
 * @since 26/08/2020
 */
@Entity
@Table(name = "EMPLOYEES")
public class Employee {

	private long id;
	private String firstName;
	private String lastName;
	private String emailId;
	
	public Employee() {
		
	}
	
	//constructor to set entity fields values
	public Employee(String firstName, String lastName, String emailId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
	}
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}
	
	
	public void setId(long id) {
		this.id = id;
	}
	
	
	
	@Column(name = "first_name", nullable = false,unique=true)
	public String getFirstName() {
		return firstName;
	}
	
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	
	
	@Column(name = "last_name", nullable = false)
	public String getLastName() {
		return lastName;
	}
	
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	@Column(name = "email_address", nullable = false)
	public String getEmailId() {
		return emailId;
	}
	
	
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	
	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId
				+ "]";
	}
	
}
