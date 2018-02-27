package edu.psu.unifiedapi.capstone.tasklist;

import com.google.api.client.util.Key;
import edu.psu.unifiedapi.capstone.utils.CapstoneResponse;

public class ResponseTask extends CapstoneResponse {
    @Key
    public Task[] response;
}
