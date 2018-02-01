package edu.psu.unifiedapi.capstonetasklist;

import com.google.api.client.util.Key;
import edu.psu.unifiedapi.capstoneutils.CapstoneResponse;

public class ResponseTask extends CapstoneResponse {
    @Key
    public Task[] response;
}
