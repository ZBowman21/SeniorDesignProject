package edu.psu.unifiedapi.capstone.utils;

import com.google.api.client.util.Key;

public class ResponseAuth {
    @Key
    public String AuthenticateID;
    @Key
    public String AuthenticateRole;
    @Key
    public String NounceCode;
}
