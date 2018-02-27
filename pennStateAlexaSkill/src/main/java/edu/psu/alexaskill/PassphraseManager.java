package edu.psu.alexaskill;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import edu.psu.unifiedapi.account.CognitoUtils;

public class PassphraseManager
{
    public static boolean checkPassphrase(String passphrase, String token)
    {
        return CognitoUtils.checkPassphrase(token, passphrase);
    }

    public static SpeechletResponse setupPassphrase(Intent intent, String token)
    {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        String spokenPassphrase = intent.getSlot("newpassphrase").getValue();
        CognitoUtils.setPassphrase(token, spokenPassphrase);

        String output = "Your new passphrase has been set successfully.";
        speech.setText(output);

        SpeechletResponse response = new SpeechletResponse();
        response.setOutputSpeech(speech);
        return response;
    }

    public static SpeechletResponse deletePassphrase(String token)
    {
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        String outputSpeech;

        CognitoUtils.setPassphrase(token, null);
        outputSpeech = "Your passphrase has been removed successfully.";

        speech.setText(outputSpeech);

        SpeechletResponse response = new SpeechletResponse();
        response.setOutputSpeech(speech);

        return response;
    }
}
