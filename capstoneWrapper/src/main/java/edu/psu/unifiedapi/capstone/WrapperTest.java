package edu.psu.unifiedapi.capstone;

import edu.psu.unifiedapi.capstoneutils.CapstoneWrapperArgs;

public class WrapperTest {
    public static void main(String[] args) {
        CapstoneWrapperArgs cargs = new CapstoneWrapperArgs();
        cargs.username = "cuz126";
        cargs.url = "/AgileTask/EGetCurrentSprint";
        cargs.params = "csid=4";

        CapstoneWrapper cap = new CapstoneWrapper();
        Object response = cap.handleRequest(cargs, null);

        System.out.println("Response:" + response);
    }
}
