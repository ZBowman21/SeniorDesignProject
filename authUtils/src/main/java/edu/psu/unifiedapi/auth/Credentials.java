package edu.psu.unifiedapi.auth;

/**
 * @author mthwate
 */
public class Credentials {

	private String username;

	private String password;

	public Credentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

}
