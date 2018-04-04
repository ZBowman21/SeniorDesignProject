package edu.psu.unifiedapi.canvas.getassignments;

import edu.psu.unifiedapi.canvas_utils.CanvasUtils;
import edu.psu.unifiedapi.canvas_utils.Course;
import edu.psu.unifiedapi.canvas_utils.Assignment;

public class GetAssignments {
    public String getAssignments(){
        CanvasUtils ca = new CanvasUtils("1050~lLxt7V3eH7Gsvk7W8AwnhtqoRkohvuUogKYGKUvDPdhING50KRodQF0vLrDVRfnZ");
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
        return upcoming;
    }
}
