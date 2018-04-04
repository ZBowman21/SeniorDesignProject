package edu.psu.unifiedapi.canvas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.psu.unifiedapi.canvas_utils.CanvasUtils;
import edu.psu.unifiedapi.canvas_utils.Course;

import java.util.ArrayList;

// get single course grade/grades for all courses
public class GetGrades implements RequestHandler<GetGradesArgs, String[]>{
    @Override
    public String[] handleRequest(GetGradesArgs input, Context context) {
        String[] grades = {};
        ArrayList<Course> courses = new ArrayList<>();

        try {
            CanvasUtils ca = new CanvasUtils(input.username);
            courses = ca.getCourseGrades();

            ArrayList<String> cg = new ArrayList<>();
            String temp;

            for(Course c : courses) {
                temp = c.name + " Grade: " + c.enrollments[0].computed_current_score;
                cg.add(temp);
            }

            grades = cg.toArray(grades);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return grades;
    }
}
