package edu.psu.unifiedapi;

/**
 * @author mthwate
 */
public class ReceiveEmailRequest {

	private String username;
	private String password;
	private int start;
	private int finish;

	ReceiveEmailRequest(String username, String password){
		this.username = username;
		this.password = password;
		this.start = 0;
		this.finish = 4;
	}

	ReceiveEmailRequest(String username, String password, int start) {
		this.username = username;
		this.password = password;
		this.start = start;
		this.finish = start + 4;
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

	public int getFinish() {
		return finish;
	}

}