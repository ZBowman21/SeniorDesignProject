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

	private static String getAttribute(String userId, String attribute) {

		AdminGetUserRequest getReq = new AdminGetUserRequest();
		getReq.setUserPoolId(POOL_ID);
		getReq.setUsername(userId);

		AdminGetUserResult getRes = identityClient.adminGetUser(getReq);

		Optional<AttributeType> first = getRes.getUserAttributes().stream().filter(a -> a.getName().equals(attribute)).findFirst();

		if (first.isPresent()) {
			return first.get().getValue();
		} else {
			return null;
		}

	}

}