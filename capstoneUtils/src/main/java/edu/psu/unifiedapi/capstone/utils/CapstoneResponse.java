package edu.psu.unifiedapi.capstone.utils;

import com.google.api.client.util.Key;

public class CapstoneResponse {
    @Key
    public ResponseAuth AuthenticateObject;
    @Key
    public boolean valid = true;
}
