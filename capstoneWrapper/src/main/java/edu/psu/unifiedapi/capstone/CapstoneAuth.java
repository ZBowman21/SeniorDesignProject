package edu.psu.unifiedapi.capstone;

public class CapstoneAuth {
    private String authID = "a26f44178da53e65d44bff13e1c56431";
    private String AuthRole = "Student";
    private String nounceCode;

    public CapstoneAuth(String nounce) {
        nounceCode = nounce;
    }

    public String BuildAuthString(){
        String temp = "";
        temp = "&AuthenticateID=" + authID + "&AuthenticateRole=" + AuthRole
                + "&NounceCode=" + nounceCode;

        return temp;
    }
}
