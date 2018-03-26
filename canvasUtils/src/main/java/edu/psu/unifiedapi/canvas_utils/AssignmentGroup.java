package edu.psu.unifiedapi.canvas_utils;

import com.google.api.client.util.Key;

public class AssignmentGroup {
    @Key
    public int id;
    @Key
    public String name;
    @Key
    public Assignment[] assignmentts;
}
