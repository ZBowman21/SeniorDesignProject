package edu.psu.alexaskill.request_handlers.capstone;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.SdkRequestConfig;
import edu.pennstate.api.model.CapstoneClockOutRequest;
import edu.pennstate.api.model.CapstoneClockOutResult;
import edu.pennstate.api.model.PennStateUnifiedException;
import edu.psu.alexaskill.request_handlers.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CapstoneClockOutRequestSender extends RequestHandler {

    public CapstoneClockOutRequestSender(String token) {
        super(token);
    }
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public BaseResult sendRequest(Intent requestIntent) {
        int statusCode = 200;
        CapstoneClockOutResult result = new CapstoneClockOutResult();

        CapstoneClockOutRequest request = new CapstoneClockOutRequest();
        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .build()
        );

        try
        {
            result = client.capstoneClockOut(request);
        }
        catch(PennStateUnifiedException e)
        {
            logger.info("Capstone clockout exception");
            statusCode = e.sdkHttpMetadata().httpStatusCode();
        }

        if(statusCode == 200)
        {
            return result;
        }
        else
        {
            return null;
        }
    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response) {

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        SimpleCard card = new SimpleCard();
        String outputResponse;

        if(response == null)
        {
            outputResponse = "You could not be clocked out of your task in Capstone. Please visit the Capstone website in order to clock out.";
        }
        else
        {
            outputResponse = "You have been clocked out of your task in Capstone.";
        }

        card.setContent(outputResponse);
        speech.setText(outputResponse);

        SpeechletResponse speechletResponse = new SpeechletResponse();
        speechletResponse.setCard(card);
        speechletResponse.setOutputSpeech(speech);

        return speechletResponse;
    }
}
