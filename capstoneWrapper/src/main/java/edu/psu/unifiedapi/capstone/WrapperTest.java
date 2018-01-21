package edu.psu.unifiedapi.capstone;

public class WrapperTest {
    public static void main(String[] args) {
        CapstoneWrapperArgs cargs = new CapstoneWrapperArgs();
        cargs.username = "cuz126";
        cargs.url = "/AgileTask/EGetCurrentSprint";
        cargs.params = "csid=4";

        CapstoneWrapper cap = new CapstoneWrapper();
        String response = cap.handleRequest(cargs, null);

        System.out.println("Response:" + response);
    }
}
