package edu.psu.unifiedapi.capstone;

import com.google.api.client.util.Key;
import edu.psu.unifiedapi.capstoneutils.CapResponse;

public class CapstoneResponse {
    @Key
    public CapResponse response;
    @Key
    public ResponseAuth AuthenticateObject;
}
