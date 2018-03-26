package edu.psu.unifiedapi.canvas_utils;

import com.google.api.client.util.Key;

public class Course {
    @Key
    public int id;
    @Key
    public int sis_course_id;
    @Key
    public String name;
    @Key
    public String course_code;
    @Key
    public String public_description;
    @Key
    public Enrollments[] enrollments;
    @Key
    public int enrollment_term_id;

    @Override
    public String toString() {
        return name;
    }
}
