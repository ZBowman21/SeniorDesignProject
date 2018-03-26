package edu.psu.unifiedapi.canvas_utils;

import com.google.api.client.util.Key;

public class Assignment {
    @Key
    public int id;
    @Key
    public String name;
    @Key
    public String description;
    @Key
    public String due_at;
    @Key
    public int course_id;
    @Key
    public Submission submission;
}
