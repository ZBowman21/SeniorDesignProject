package edu.psu.unifiedapi.canvas_utils;

import com.google.api.client.util.Key;

// maybe not needed yet
public class Submission {
    @Key
    int assignment_id;
    @Key
    public String grade;
    @Key
    Double score;
    @Key
    String submission_comments; // why not lol
    @Key
    boolean late;
}
