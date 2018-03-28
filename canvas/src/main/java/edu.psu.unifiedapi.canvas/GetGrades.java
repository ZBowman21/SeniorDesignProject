package edu.psu.unifiedapi.canvas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.canvas_utils.CanvasUtils;
import edu.psu.unifiedapi.canvas_utils.Course;
import edu.psu.unifiedapi.canvas_utils.Enrollments;

import java.io.IOException;
import java.util.ArrayList;

// get single course grade/grades for all courses
public class GetGrades implements RequestHandler<GetGradesArgs, String>{
    @Override
    public String handleRequest(GetGradesArgs input, Context context) {
        String grades = "";

        CanvasUtils ca = new CanvasUtils("1050~lLxt7V3eH7Gsvk7W8AwnhtqoRkohvuUogKYGKUvDPdhING50KRodQF0vLrDVRfnZ");
        try {
            Enrollments[] a = ca.getEnrollments();
            Course[] c = ca.getCourses();
            for(Enrollments e : a){
                for(Course t : c) {
                    if(t.id == e.course_id)
                    grades += t.name + " " + e.computed_current_score + "\n";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return grades;
    }
}
