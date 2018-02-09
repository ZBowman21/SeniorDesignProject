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

	public static String getPassphrase(String userId) {

		AdminGetUserRequest getReq = new AdminGetUserRequest();
		getReq.setUserPoolId(POOL_ID);
		getReq.setUsername(userId);

		AdminGetUserResult getRes = identityClient.adminGetUser(getReq);

		Optional<AttributeType> first = getRes.getUserAttributes().stream().filter(a -> a.getName().equals("passphrase")).findFirst();

		if (first.isPresent()) {
			return first.get().getValue();
		} else {

			AdminUpdateUserAttributesRequest updateReq = new AdminUpdateUserAttributesRequest();

			Random rand = new Random();

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < 128; i++) {
				sb.append(rand.nextInt(10));
			}

			List<AttributeType> attributes = new ArrayList<>();

			AttributeType attr = new AttributeType();
			attr.setName("passphrase");
			attr.setValue(sb.toString());

			attributes.add(attr);

			updateReq.setUserPoolId(POOL_ID);
			updateReq.setUserAttributes(attributes);

			identityClient.adminUpdateUserAttributes(updateReq);

			return sb.toString();
		}

	}

}