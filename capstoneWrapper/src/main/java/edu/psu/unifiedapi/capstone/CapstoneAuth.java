package edu.psu.unifiedapi.capstone;

public class CapstoneAuth {
    private String authID;
    private String AuthRole = "Student";
    private String nounceCode;

    public CapstoneAuth(String nounce, String authID) {
        nounceCode = nounce;
        this.authID = authID;
    }

    public String BuildAuthString(){
        String temp = "";
        temp = "&AuthenticateID=" + authID + "&AuthenticateRole=" + AuthRole
                + "&NounceCode=" + nounceCode;

        return temp;
    }
}
