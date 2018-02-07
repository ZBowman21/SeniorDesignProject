package edu.psu.unifiedapi.getdiningtimes;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class GetPSUDiningTimes implements RequestHandler<DiningRequest, String> {


    @Override
    public String handleRequest(DiningRequest input, Context context) {
        int day = 0;
        int place = 0;
        String timeString = "CLOSED!";

        //parse day into int
        switch (input.day.toLowerCase()){
            case "sunday":
                day = 1;
                break;
            case "monday":
                day = 2;
                break;
            case "tuesday":
                day = 3;
                break;
            case "wednesday":
                day = 4;
                break;
            case "thursday":
                day = 5;
                break;
            case "friday":
                day = 6;
                break;
            case "saturday":
                day = 7;
                break;
            default:
                //throw exception, future error handling
                break;
        }

        //parse place into int
        switch (input.place.toLowerCase()){
            case "dobbins":
                place = 1;
                break;
            case "brunos":
                place = 2;
                break;
            case "paws":
                place = 3;
                break;
            case "clark":
                place = 4;
                break;
            case "the galley":
                place = 5;
                break;
            case "elements":
                place = 6;
                break;
            case "clipper":
                place = 7;
                break;
            default:
                //throw exception, future error handling
                break;
        }

        //begin csv file search
        ArrayList<String> csv = new ArrayList<>();

        //access csv file
        try {
            URL url = new URL("http://psuunifiedwebsite.s3-website-us-east-1.amazonaws.com/PSUDiningTimes.csv");
            URLConnection urlC = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlC.getInputStream()));
            String line;
            while((line = br.readLine()) != null){
                csv.add(line);
            }
            br.close();
        }
        catch(Exception e){
            context.getLogger().log(e.toString());
        }

        //browse to correct row
        timeString = csv.get(place);

        //browse to correct column
        String[] temp = null;
        temp = timeString.split(",");
        timeString = temp[day];

        //return
        return timeString;
    }
}
