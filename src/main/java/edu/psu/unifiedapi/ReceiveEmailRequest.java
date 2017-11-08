package edu.psu.unifiedapi;

/**
 * @author mthwate
 */
public class ReceiveEmailRequest {

	private String username;

	private String password;

	ReceiveEmailRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}