package edu.psu.unifiedapi;

/**
 * @author mthwate
 */
public class MarkEmailRequest {

	public String username;
	public String password;
	public int start;

	MarkEmailRequest(){}

	MarkEmailRequest(String username, String password, int start) {
		this.username = username;
		this.password = password;
		this.start = start;
	}
}