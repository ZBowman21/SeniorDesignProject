import edu.psu.unifiedapi.canvas_utils.CanvasUtils;
import edu.psu.unifiedapi.canvas_utils.Course;

import java.io.IOException;

// z "1050~lLxt7V3eH7Gsvk7W8AwnhtqoRkohvuUogKYGKUvDPdhING50KRodQF0vLrDVRfnZ"
// c "1050~vxqReycOSH0IbANwWy2Rakw22DY5LdsSLdY5ugzX6sQ0DMXblpY2T8lEM1a33mzS"


public class testCanvas {
    public static void main(String[] args) throws IOException {
        String grades = "";

        CanvasUtils ca = new CanvasUtils("1050~vxqReycOSH0IbANwWy2Rakw22DY5LdsSLdY5ugzX6sQ0DMXblpY2T8lEM1a33mzS");
        Course[] a = ca.getCourses();

        for(Course c : a){
            grades += ca.getCourseGrade(c.name) + "\n";
        }

        System.out.println(grades);
        //ca.getTerm();
    }
}
