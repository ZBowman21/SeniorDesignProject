package edu.psu.unifiedapi.capstonecursprintid;

import edu.psu.unifiedapi.capstoneutils.CapstoneCurSprintIdArgs;

public class sprintTest {

    public static void main(String[] args) {
        CapstoneCurSprintIdArgs arg = new CapstoneCurSprintIdArgs();
        arg.username = "cuz126";

        CapstoneCurSprintId ccsi = new CapstoneCurSprintId();

        String test = ccsi.handleRequest(arg, null);

        System.out.println(test);
    }
}
