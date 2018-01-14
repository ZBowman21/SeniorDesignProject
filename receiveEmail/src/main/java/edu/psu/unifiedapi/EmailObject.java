package edu.psu.unifiedapi;

public class EmailObject {
    String from;
    String date;
    String subject;
    String body;

    EmailObject(String f, String d, String s, String b){
        this.from = f;
        this.date = d;
        this.subject = s;
        this.body = b;
    }
}
