package edu.psu.unifiedapi.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author mthwate
 */
public class Hashing {

	public static byte[] hash(String data) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("SHA-512");
		return md5.digest(data.getBytes());
	}

}
