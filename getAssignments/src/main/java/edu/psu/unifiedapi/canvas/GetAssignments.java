package edu.psu.unifiedapi.canvas;

import edu.psu.unifiedapi.canvas_utils.CanvasUtils;
import edu.psu.unifiedapi.canvas_utils.Course;
import edu.psu.unifiedapi.canvas_utils.Assignment;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

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
                    SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    Date dt = sd1.parse(a.due_at);
                    LocalDateTime ldt = LocalDateTime.ofInstant(dt.toInstant(), ZoneId.of("-05:00"));
                    dt = Date.from(ldt.minusHours(4).atZone(ZoneId.of("-05:00")).toInstant());
                    SimpleDateFormat sd2 = new SimpleDateFormat("EEEEE-MMMM-dd-yyyy'@'hh-mm-a");
                    String nd = sd2.format(dt);
                    temp += a.name + " Due: " + nd + ". ";
                }
                if (as.length != 0) {
                    temp = "For " + c.name + ", " + temp;
                    upcoming.add(temp);
                }
            }
            System.out.println(upcoming);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return upcoming.toArray(new String[upcoming.size()]);
    }
}
