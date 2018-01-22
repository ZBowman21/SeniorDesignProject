package edu.psu.unifiedapi.account;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;
import com.amazonaws.services.cognitoidp.model.GetUserRequest;
import com.amazonaws.services.cognitoidp.model.GetUserResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.AuthPolicy;
import io.TokenAuthorizerContext;


public class Authenticate implements RequestHandler<TokenAuthorizerContext, AuthPolicy> {

	@Override
	public AuthPolicy handleRequest(TokenAuthorizerContext input, Context context) {

		String token = input.getAuthorizationToken();



		AWSCognitoIdentityProvider identityClient = AWSCognitoIdentityProviderClient.builder().build();

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

		return new AuthPolicy(principalId, AuthPolicy.PolicyDocument.getDenyAllPolicy(region, awsAccountId, restApiId, stage));
	}

}