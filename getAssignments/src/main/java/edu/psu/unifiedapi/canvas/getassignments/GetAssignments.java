package edu.psu.unifiedapi.canvas.getassignments;

import edu.psu.unifiedapi.canvas_utils.CanvasUtils;
import edu.psu.unifiedapi.canvas_utils.Course;
import edu.psu.unifiedapi.canvas_utils.Assignment;

import java.sql.SQLException;

public class GetAssignments {
    public String getAssignments(GetAssignmentsArgs args){
        String upcoming = "";
        try {
            CanvasUtils ca = new CanvasUtils(args.username);
            Course[] courses = ca.getCourses();
            for (Course c : courses) {
                Assignment[] as = ca.getUpcomingAssignments(c);
                if (as.length != 0) {
                    upcoming += "\nFor " + c.name + ", ";
                }
                for (Assignment a : as) {
                    upcoming += a.name + " Due: " + a.due_at + " ";
                }
            }
            System.out.println(upcoming);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upcoming;
    }
}
