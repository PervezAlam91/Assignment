package com.allianz.assignment.exception;

import java.util.Date;


/**
 * ErrorDetails is used to give meaning full error response 
 * @author Pervez Alam
 * @version 1.0
 * @since 26/08/2020
 */
public class ErrorDetails {
	private Date timestamp;
	private String message;
	private String details;

	public ErrorDetails(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}
}
