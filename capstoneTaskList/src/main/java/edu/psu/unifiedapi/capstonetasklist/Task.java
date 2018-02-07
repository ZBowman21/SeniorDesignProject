package edu.psu.unifiedapi.capstonetasklist;


import com.google.api.client.util.Key;

public class Task {
    @Key
    public int taskid;
    @Key
    public String start_sprint_id;
    @Key
    public String task_desp;
    @Key
    public String task_type;
    @Key
    public String task_subtype;
    @Key
    public String priority;
    @Key
    public String complete_time;    // can be null
    @Key
    public String complete_status;  // can be null
    @Key
    public String task_label;
}
