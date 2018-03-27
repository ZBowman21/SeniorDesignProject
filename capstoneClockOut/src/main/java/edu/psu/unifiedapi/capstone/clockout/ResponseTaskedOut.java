package edu.psu.unifiedapi.capstone.clockout;

import com.google.api.client.util.Key;
import edu.psu.unifiedapi.capstone.utils.CapstoneResponse;

public class ResponseTaskedOut extends CapstoneResponse {
    @Key
    public TaskedOut response;
}
