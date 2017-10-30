package edu.psu.unifiedapi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * @author mthwate
 */
public class TestLambdaFunc implements RequestHandler<TestAPIGateway, String> {

	@Override
	public String handleRequest(TestAPIGateway input, Context context) {
		context.getLogger().log("Input: " + input);
		String output = "Hello, " + input.input + "!";
		return output;
	}

}
