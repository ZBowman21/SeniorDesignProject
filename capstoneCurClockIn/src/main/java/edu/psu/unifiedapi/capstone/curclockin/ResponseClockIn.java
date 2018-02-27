package edu.psu.unifiedapi.capstone.curclockin;

import edu.psu.unifiedapi.capstoneutils.CapstoneResponse;
import com.google.api.client.util.Key;

public class ResponseClockIn extends CapstoneResponse {
    @Key
    public ClockedInTask response;
}
