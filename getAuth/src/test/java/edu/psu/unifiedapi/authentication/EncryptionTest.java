package edu.psu.unifiedapi.authentication;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author mthwate
 */
public class EncryptionTest {

	@Test
	public void encryptAndDecrypt() throws Exception {

		String[] datas = {"", "Hello world!"};
		String[] passwords = {"", "password", "SuperSecurePassword!"};

		for (String data : datas) {
			for (String password : passwords) {
				byte[] encrypted = Encryption.encrypt(data, password);

				String decrypted = Encryption.decrypt(encrypted, password);

				assertEquals(data, decrypted);
			}
		}

	}

}