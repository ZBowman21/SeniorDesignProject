import edu.psu.unifiedapi.canvas.GetGrades;
import edu.psu.unifiedapi.canvas.GetGradesArgs;

import java.io.IOException;

public class testCanvas {
    public static void main(String[] args) throws IOException {

        GetGrades gg = new GetGrades();
        String[] grades = gg.handleRequest(new GetGradesArgs(), null);

        for(String s : grades) {
            System.out.println(s);
        }
    }
}
