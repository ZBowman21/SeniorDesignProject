package edu.psu.unifiedapi;

/**
 * @author mthwate
 */
public class MarkEmailRequest {

	public String userId;
	public int start;

	MarkEmailRequest(){}

	MarkEmailRequest(String userId, int start) {
		this.userId = userId;
		this.start = start;
	}
}