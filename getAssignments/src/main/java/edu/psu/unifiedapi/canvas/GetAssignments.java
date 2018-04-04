package edu.psu.unifiedapi.canvas;

import edu.psu.unifiedapi.canvas_utils.CanvasUtils;
import edu.psu.unifiedapi.canvas_utils.Course;
import edu.psu.unifiedapi.canvas_utils.Assignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;

public class GetAssignments {

    public String[] getAssignments(GetAssignmentsArgs args){
        ArrayList<String> upcoming = new ArrayList<String>();
        String temp;

        try {
            CanvasUtils ca = new CanvasUtils(args.username);
            Course[] courses = ca.getCourses();
            for (Course c : courses) {
                Assignment[] as = ca.getUpcomingAssignments(c);
                temp = "";
                for (Assignment a : as) {
                    temp += a.name + " Due: " + a.due_at + " ";
                }
                if (as.length != 0) {
                    temp = "For " + c.name + ", " + temp;
                    upcoming.add(temp);
                }
            }
            System.out.println(upcoming);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upcoming.toArray(new String[upcoming.size()]);
    }
}
