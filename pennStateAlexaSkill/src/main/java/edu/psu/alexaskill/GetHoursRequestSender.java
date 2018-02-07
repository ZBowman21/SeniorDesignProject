package edu.psu.alexaskill;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.opensdk.BaseResult;
import com.amazonaws.opensdk.SdkRequestConfig;
import edu.pennstate.api.model.GetHoursRequest;
import edu.pennstate.api.model.GetHoursResult;

import java.util.Calendar;

public class GetHoursRequestSender extends RequestHandler
{
    private String[] strDays = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday" };

    @Override
    public BaseResult sendRequest(Intent requestIntent, String token) {

        String day = requestIntent.getSlot("time").getValue();
        Calendar c = Calendar.getInstance();

        if(day == null)
        {
            day = "today";
        }

        if(day.equalsIgnoreCase("tomorrow"))
        {
            day = strDays[c.get(Calendar.DAY_OF_WEEK)];
        }
        if(day.equalsIgnoreCase("now") || day.equalsIgnoreCase("today"))
        {
            day = strDays[c.get(Calendar.DAY_OF_WEEK) - 1];
        }
        GetHoursRequest request = new GetHoursRequest();

        request.setDay(day);
        request.setPlace(requestIntent.getSlot("location").getValue());

        request.sdkRequestConfig(
                SdkRequestConfig.builder()
                        .httpRequestTimeout(30000)
                        .totalExecutionTimeout(30000)
                        .build()
        );

        GenerateClient();
        GetHoursResult response = client.getHours(request);
        return response;
    }

    @Override
    public SpeechletResponse parseResponse(BaseResult response) {

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        SimpleCard card = new SimpleCard();

        String outputResponse;

        GetHoursResult result = (GetHoursResult)response;

        String hours = result.getHours();

        if(!hours.isEmpty())
        {
            outputResponse =  hours;
        }
        else
        {
            outputResponse = "I could not retrieve the times for the requested location at this time.";
        }

        card.setContent(outputResponse);
        speech.setText(outputResponse);

        SpeechletResponse speechletResponse = new SpeechletResponse();
        speechletResponse.setCard(card);
        speechletResponse.setOutputSpeech(speech);

        return speechletResponse;
    }

}
