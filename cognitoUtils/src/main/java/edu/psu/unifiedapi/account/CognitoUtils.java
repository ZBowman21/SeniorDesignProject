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

	public static boolean hasPassphrase(String token) throws UserNotFoundException {
		return getPassphrase(token) != null;
	}

	public static boolean checkPassphrase(String token, String passphrase) throws UserNotFoundException {
		String currentPassphrase = getPassphrase(token);
		if(currentPassphrase == null){
			return passphrase == null;
		}
		else
		{
			return currentPassphrase.equals(passphrase);
		}
	}

	public static String getPassphrase(String token) throws UserNotFoundException {
		return getAttribute(token, "custom:passphrase");
	}

	public static void setPassphrase(String token, String passphrase) throws UserNotFoundException {
		setAttribute(token, "custom:passphrase", passphrase);
	}

	public static String getEncryptionKey(String userId) throws UserNotFoundException {
		String encryptionKey = getAttributeAdmin(userId, "custom:encryptionKey");

		if (encryptionKey == null) {

			Random rand = new Random();

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < 128; i++) {
				sb.append(rand.nextInt(10));
			}

			encryptionKey = sb.toString();
			setAttributeAdmin(userId, "custom:encryptionKey", encryptionKey);
		}

		return encryptionKey;

	}

	private static String getAttribute(String token, String name) {

		GetUserRequest getReq = new GetUserRequest();
		getReq.setAccessToken(token);

		GetUserResult getRes = identityClient.getUser(getReq);

		Optional<AttributeType> first = getRes.getUserAttributes().stream().filter(a -> a.getName().equals(name)).findFirst();

		if (first.isPresent()) {
			return first.get().getValue();
		} else {
			return null;
		}

	}

	private static void setAttribute(String token, String name, String value) {
		if (value == null) {
			deleteAttribute(token, name);
		} else {

			UpdateUserAttributesRequest updateReq = new UpdateUserAttributesRequest();

			List<AttributeType> attributes = new ArrayList<>();

			AttributeType attr = new AttributeType();
			attr.setName(name);
			attr.setValue(value);

			attributes.add(attr);

			updateReq.setAccessToken(token);
			updateReq.setUserAttributes(attributes);

			identityClient.updateUserAttributes(updateReq);
		}
	}

	private static void deleteAttribute(String token, String name) {

		DeleteUserAttributesRequest delReq = new DeleteUserAttributesRequest();

		List<String> attributes = new ArrayList<>();
		attributes.add(name);

		delReq.setAccessToken(token);
		delReq.setUserAttributeNames(attributes);

		identityClient.deleteUserAttributes(delReq);
	}

	private static String getAttributeAdmin(String userId, String name) throws UserNotFoundException {

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

	private static void setAttributeAdmin(String userId, String name, String value) throws UserNotFoundException {
		if (value == null) {
			deleteAttributeAdmin(userId, name);
		} else {

			AdminUpdateUserAttributesRequest updateReq = new AdminUpdateUserAttributesRequest();

			List<AttributeType> attributes = new ArrayList<>();

			AttributeType attr = new AttributeType();
			attr.setName(name);
			attr.setValue(value);

			attributes.add(attr);

			updateReq.setUserPoolId(POOL_ID);
			updateReq.setUsername(userId);
			updateReq.setUserAttributes(attributes);

			identityClient.adminUpdateUserAttributes(updateReq);
		}
	}

	private static void deleteAttributeAdmin(String userId, String name) throws UserNotFoundException {

		AdminDeleteUserAttributesRequest delReq = new AdminDeleteUserAttributesRequest();

		List<String> attributes = new ArrayList<>();
		attributes.add(name);

		delReq.setUserPoolId(POOL_ID);
		delReq.setUsername(userId);
		delReq.setUserAttributeNames(attributes);

		identityClient.adminDeleteUserAttributes(delReq);
	}

}