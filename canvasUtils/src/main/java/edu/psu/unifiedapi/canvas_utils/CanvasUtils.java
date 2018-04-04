package edu.psu.unifiedapi.canvas_utils;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CanvasUtils {
    private final HttpRequestFactory requestFactory;
    private final String BASE_URL = "https://psu.instructure.com/api/v1/";

    public CanvasUtils(String accessToken){
        HttpTransport transport = new NetHttpTransport();
        Credential creds = new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(accessToken);
        requestFactory = transport.createRequestFactory(creds);
    }

    public <T> T read(String resource, Class<T> dataClass) throws IOException {
        JsonObjectParser parser = new JsonObjectParser(new JacksonFactory());
        HttpResponse response = requestFactory.buildGetRequest(new GenericUrl(BASE_URL + resource)).setParser(parser).execute();
        return response.parseAs(dataClass);
    }

    public int getTerm() throws IOException {
        EnrollmentTerm[] terms = read("accounts/:account_id/terms", EnrollmentTerm[].class);

        String s = "";

        for(EnrollmentTerm et : terms){
            s += et.id + " ";
        }

        System.out.println(s);
        return 1;
    }

    public String getCourseGrade(String courseName) {
		String courseGrade = "Unable to get classes.";
		try {
			Course[] ca = read("users/self/courses", Course[].class);//read("courses", Course[].class);
			ArrayList<Course> co = new ArrayList<>();
			for(int i = 0; i < ca.length; i++){
			    if(ca[i].name != null){
			        co.add(ca[i]);
                }
            }
			for (Course c : co) {
				if(c.name.equals(courseName) || c.course_code.equals(courseName)) {
				    int cID;
				    cID = c.id;
				    Enrollments e = getEnrollment(cID);
				    courseGrade = "";
					courseGrade += (e.grades.current_score); //c.enrollments[0].computed_current_grade;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        return courseGrade;
	}

	public Enrollments getEnrollment(int cID) throws IOException {
        // for some reason 1050000000 is before the courseid???? I don't know why
        Enrollments[] e = getEnrollments();
        Enrollments grade = null;

        for(Enrollments c : e){
            if (c.course_id == cID){
                grade = c;
            }
        }
        return grade;
    }

    public Enrollments[] getEnrollments() throws IOException {
        List<Enrollments> el = new ArrayList<Enrollments>();
        Enrollments[] ea;

        Enrollments[] es = read("users/self/enrollments?per_page=40", Enrollments[].class);

        LocalDateTime temp;
        LocalDateTime now = LocalDateTime.now();

        for (Enrollments e : es) {
            temp = LocalDateTime.parse(e.created_at, DateTimeFormatter.ISO_DATE_TIME);

            if(now.getYear() == temp.getYear()
                    && ((now.getMonthValue() < 8 && temp.getMonthValue() == 1)
                    || (now.getMonthValue() > 7 && temp.getMonthValue() >= 8))) {
                el.add(e);
            }
        }
        ea = new Enrollments[el.size()];
        ea = el.toArray(ea);
        return ea;
    }

    public Course[] getCourses() {
        try {
            Course[] courses = read("courses?enrollment_type=student&per_page=25&include[]=total_scores", Course[].class);

            List<Course> coursesList = new ArrayList<>();

            for (Course course : courses) {
                if (course.enrollment_term_id == 10152) { //10148
                    coursesList.add(course);
                }
            }

            return coursesList.toArray(new Course[0]);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Course getCourse(String courseName){

        Course[] ca = getCourses();
        if(ca != null) {
            for (Course c : ca) {
                if (c.name.equals(courseName) || c.name.equals(courseName)) {
                    return c;
                }
            }
        }
        return null;
    }

    public Assignment[] getAssignments(String courseName) {
        Course course = getCourse(courseName);
        if(course != null) {
            try {
                return read("courses/" + course.id + "/assignments?include[]=submissions", Assignment[].class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Assignment[] getUpcomingAssignments(Course course) {
        if(course != null) {
            try {
                return read("courses/" + course.id + "/assignments?bucket=upcoming&per_page=100", Assignment[].class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Assignment[] getUpcomingAssignments(String courseName) {
        return getUpcomingAssignments(getCourse(courseName));
    }

    public Assignment getAssignment(String courseName, String assignmentName) {
        Assignment[] ra = getAssignments(courseName);
        if(ra != null) {
            for(Assignment a : ra) {
                if(a.name.toLowerCase().equals(assignmentName.toLowerCase())) {
                    return a;
                }
            }
        }
        return null;
    }
}
