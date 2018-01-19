package edu.psu.unifiedapi;

/**
 * @author mthwate
 */
public class ReceiveEmailRequest {
	public String username;
	public String password;
	public int start;
	
	ReceiveEmailRequest(String username, String password, int start) {
		this.username = username;
		this.password = password;
		this.start = start;
	}
}