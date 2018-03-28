package edu.psu.unifiedapi.canvas_utils;

import com.google.api.client.util.Key;

public class Grade {
    @Key
    public String current_grade;
    @Key
    public String final_grade;
    @Key
    public Double current_score = 0.0;
    @Key
    public Double final_score;
}
