package edu.psu.unifiedapi.account;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.GetUserRequest;
import com.amazonaws.services.cognitoidp.model.GetUserResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.AuthPolicy;
import io.TokenAuthorizerContext;


public class Authenticate implements RequestHandler<TokenAuthorizerContext, AuthPolicy> {

	private static AWSCognitoIdentityProvider identityClient = AWSCognitoIdentityProviderClientBuilder.defaultClient();

	@Override
	public AuthPolicy handleRequest(TokenAuthorizerContext input, Context context) {

		String token = input.getAuthorizationToken();

		GetUserRequest req = new GetUserRequest();

		req.setAccessToken(token);

		GetUserResult res = identityClient.getUser(req);



		String principalId = res.getUsername();

		String methodArn = input.getMethodArn();
		String[] arnPartials = methodArn.split(":");
		String region = arnPartials[3];
		String awsAccountId = arnPartials[4];
		String[] apiGatewayArnPartials = arnPartials[5].split("/");
		String restApiId = apiGatewayArnPartials[0];
		String stage = apiGatewayArnPartials[1];
		AuthPolicy.HttpMethod method = AuthPolicy.HttpMethod.valueOf(apiGatewayArnPartials[2]);
		StringBuilder path = new StringBuilder(apiGatewayArnPartials[3]);
		for (int i = 4; i < apiGatewayArnPartials.length; i++) {
			path.append("/").append(apiGatewayArnPartials[i]);
		}

		AuthPolicy policy = new AuthPolicy(principalId, AuthPolicy.PolicyDocument.getAllowOnePolicy(region, awsAccountId, restApiId, stage, method, path.toString()));

		ObjectMapper mapper = new ObjectMapper();

		try {
			System.out.println(mapper.writeValueAsString(policy));
		} catch (JsonProcessingException e) {}

		return policy;
	}

}