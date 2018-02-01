package edu.psu.unifiedapi.capstoneutils;

import com.google.api.client.util.Key;

public class ResponseAuth {
    @Key
    public String AuthenticateID;
    @Key
    public String AuthenticateRole;
    @Key
    public String NounceCode;
}
