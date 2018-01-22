package edu.psu.unifiedapi;

public class EmailObject {
    public String from;
    public String date;
    public String subject;
    public String body;
    public int unread;

    EmailObject(){}

    EmailObject(String f, String d, String s, String b, int u){
        this.from = f;
        this.date = d;
        this.subject = s;
        this.body = b;
        this.unread = u;
    }
}