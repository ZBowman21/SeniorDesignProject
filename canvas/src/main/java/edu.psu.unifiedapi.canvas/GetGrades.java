package edu.psu.unifiedapi.canvas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.canvas_utils.CanvasUtils;
import edu.psu.unifiedapi.canvas_utils.Course;

import java.util.ArrayList;

// get single course grade/grades for all courses
public class GetGrades implements RequestHandler<GetGradesArgs, String>{
    @Override
    public String handleRequest(GetGradesArgs input, Context context) {
        String grades = "";

        CanvasUtils ca = new CanvasUtils("1050~lLxt7V3eH7Gsvk7W8AwnhtqoRkohvuUogKYGKUvDPdhING50KRodQF0vLrDVRfnZ");
        Course[] a = ca.getCourses();

        for(Course c : a){
            grades += c.name + " ";
        }

        return grades;
    }
}
