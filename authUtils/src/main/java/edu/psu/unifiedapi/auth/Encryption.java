package edu.psu.unifiedapi.auth;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;

/**
 * @author mthwate
 */
public class Encryption {

	private static byte[] SALT = "SuperSaltySalt".getBytes();

	public static byte[] encrypt(String data, String password) throws GeneralSecurityException {
		Cipher aes = Cipher.getInstance("AES");
		aes.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
		return aes.doFinal(data.getBytes());
	}

	public static String decrypt(byte[] data, String password) throws GeneralSecurityException {
		Cipher aes = Cipher.getInstance("AES");
		aes.init(Cipher.DECRYPT_MODE, getSecretKey(password));
		return new String(aes.doFinal(data));
	}

	private static SecretKey getSecretKey(String password) throws GeneralSecurityException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT, 65536, 128);
		SecretKey tmp = factory.generateSecret(spec);
		return new SecretKeySpec(tmp.getEncoded(), "AES");
	}

}
