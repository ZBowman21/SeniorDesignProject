package edu.psu.alexaskill; /**
 * Created by helios on 9/22/16.
 */

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import edu.psu.alexaskill.PennStateSpeechlet;

public final class PennStateSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler
{
    private static final Set<String> supportedApplicationIds;

    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds = new HashSet<String>();
        supportedApplicationIds.add("amzn1.ask.skill.bcafd968-f2fa-42e7-95d4-94cbfc518dea");
    }

    public PennStateSpeechletRequestStreamHandler() {
        super(new PennStateSpeechlet(), supportedApplicationIds);
    }
}
