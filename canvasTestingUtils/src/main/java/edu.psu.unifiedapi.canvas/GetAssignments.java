package edu.psu.unifiedapi.canvas;

import edu.psu.unifiedapi.canvas_utils.CanvasUtils;
import edu.psu.unifiedapi.canvas_utils.Course;
import edu.psu.unifiedapi.canvas_utils.Assignment;

import java.sql.SQLException;

public class GetAssignments {

    public static void main(String[] args) {
        CanvasUtils ca = null;
        try {
            ca = new CanvasUtils("382d83a9-9d7d-48bb-b117-c4bcfc618637");
            String upcoming = "";
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

    }
}
