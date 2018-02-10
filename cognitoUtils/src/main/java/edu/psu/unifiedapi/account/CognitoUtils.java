package edu.psu.unifiedapi.account;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


public class CognitoUtils {

	private static AWSCognitoIdentityProvider identityClient = AWSCognitoIdentityProviderClientBuilder.defaultClient();

	private static String POOL_ID = System.getenv("POOL_ID");

	public static boolean hasPassphrase(String userId) {
		return getPassphrase(userId) != null;
	}

	public static boolean checkPassphrase(String userId, String passphrase) {
		return getPassphrase(userId).equals(passphrase);
	}

	public static String getPassphrase(String userId) {
		return getAttribute(userId, "passphrase");
	}

	public static void setPassphrase(String userId, String passphrase) {
		setAttribute(userId, "passphrase", passphrase);
	}

	public static String getEncryptionKey(String userId) {
		String encryptionKey = getAttribute(userId, "encryptionKey");

		if (encryptionKey == null) {

			Random rand = new Random();

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < 128; i++) {
				sb.append(rand.nextInt(10));
			}

			encryptionKey = sb.toString();
			setAttribute(userId, "encryptionKey", encryptionKey);
		}

		return encryptionKey;

	}

	private static String getAttribute(String userId, String name) {

		AdminGetUserRequest getReq = new AdminGetUserRequest();
		getReq.setUserPoolId(POOL_ID);
		getReq.setUsername(userId);

		AdminGetUserResult getRes = identityClient.adminGetUser(getReq);

		Optional<AttributeType> first = getRes.getUserAttributes().stream().filter(a -> a.getName().equals(name)).findFirst();

		if (first.isPresent()) {
			return first.get().getValue();
		} else {
			return null;
		}

	}

	private static void setAttribute(String userId, String name, String value) {

		AdminUpdateUserAttributesRequest updateReq = new AdminUpdateUserAttributesRequest();

		List<AttributeType> attributes = new ArrayList<>();

		AttributeType attr = new AttributeType();
		attr.setName(name);
		attr.setValue(value);

		attributes.add(attr);

		updateReq.setUserPoolId(POOL_ID);
		updateReq.setUserAttributes(attributes);

		identityClient.adminUpdateUserAttributes(updateReq);
	}

}