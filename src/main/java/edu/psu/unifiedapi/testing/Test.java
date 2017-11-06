package edu.psu.unifiedapi.testing;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

/**
 * @author mthwate
 */
public class Test implements RequestHandler<APIGatewayProxyRequestEvent, String> {

	@Override
	public String handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		return "This is a test!";
	}

}
