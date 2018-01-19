package edu.psu.unifiedapi;

/**
 * @author mthwate
 */
public class MarkEmailRequest {

	private String username;
	private String password;
	private int start;

	MarkEmailRequest(String username, String password){
		this.username = username;
		this.password = password;
		this.start = 0;
	}

	MarkEmailRequest(String username, String password, int start) {
		this.username = username;
		this.password = password;
		this.start = start;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getStart() {
		return start;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}