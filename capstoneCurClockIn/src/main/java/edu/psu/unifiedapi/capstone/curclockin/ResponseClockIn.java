package edu.psu.unifiedapi.capstone.curclockin;

import edu.psu.unifiedapi.capstone.utils.CapstoneResponse;
import com.google.api.client.util.Key;

public class ResponseClockIn extends CapstoneResponse {
    @Key
    public ClockedInTask response;
}
