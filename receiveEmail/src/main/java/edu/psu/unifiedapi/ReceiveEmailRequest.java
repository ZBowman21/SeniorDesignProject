package edu.psu.unifiedapi;

/**
 * @author mthwate
 */
public class ReceiveEmailRequest {

	private String username;
	private String password;
	private int start;

	ReceiveEmailRequest(){

	}
	
	ReceiveEmailRequest(String username, String password, int start) {
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