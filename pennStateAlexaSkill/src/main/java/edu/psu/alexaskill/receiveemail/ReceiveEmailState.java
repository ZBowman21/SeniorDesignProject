package edu.psu.alexaskill.receiveemail;

import edu.pennstate.api.model.ReceivedEmail;

public class ReceiveEmailState
{
    private ReceivedEmail email;
    private int currentEmailIndex;
    private int currentUnread;
    public enum State
    {
        Initial,
        FirstUnread,
        NoUnread,
        ReadingEmail,
        NextEmail,
    }

    private State state;

    public ReceiveEmailState()
    {
        email = new ReceivedEmail();
        currentEmailIndex = 0;
        state = State.Initial;
    }

    public int getCurrentEmailIndex() {
        return currentEmailIndex;
    }

    public ReceivedEmail getEmail() {
        return email;
    }

    public void setEmail(ReceivedEmail email) {
        this.email = email;
    }

    public void setCurrentEmailIndex(int currentEmailIndex) {
        this.currentEmailIndex = currentEmailIndex;
    }

    public int getCurrentUnread() {
        return currentUnread;
    }

    public void setCurrentUnread(int currentUnread) {
        this.currentUnread = currentUnread;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
