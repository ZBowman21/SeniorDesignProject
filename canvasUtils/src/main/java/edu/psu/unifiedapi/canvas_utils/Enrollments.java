package edu.psu.unifiedapi.canvas_utils;

import com.google.api.client.util.Key;

public class Enrollments {
    @Key
    public int id;
    @Key
    public int course_id;
    @Key
    public int user_id;
    @Key
    public Grade grades;
    @Key
    public Double computed_current_score;
    @Key
    public Double computed_final_score;
    @Key
    public String computed_current_grade;
    @Key
    public String computed_final_grade;
    @Key
    public String start_at; // Use to get the right semester?? (is null of course)
    @Key
    public String created_at; // Use this instead
}
