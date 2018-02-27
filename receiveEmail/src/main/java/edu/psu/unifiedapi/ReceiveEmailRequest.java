package edu.psu.unifiedapi;

/**
 * @author mthwate
 */
public class ReceiveEmailRequest {
	public String userId;
	public int start;

	ReceiveEmailRequest(){};

	ReceiveEmailRequest(String userId, int start) {
		this.userId = userId;
		this.start = start;
	}
}