package edu.psu.unifiedapi.capstone.cursprintid;

import edu.psu.unifiedapi.capstone.utils.CapstoneCurSprintIdArgs;

public class SprintTest {

    public static void main(String[] args) {
        CapstoneCurSprintIdArgs arg = new CapstoneCurSprintIdArgs();
        arg.username = "cuz126";

        CapstoneCurSprintId ccsi = new CapstoneCurSprintId();

        String test = ccsi.handleRequest(arg, null);

        System.out.println(test);
    }
}
