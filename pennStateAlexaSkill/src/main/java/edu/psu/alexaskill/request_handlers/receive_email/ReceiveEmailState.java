package edu.psu.alexaskill.request_handlers.receive_email;

import edu.pennstate.api.model.ReceivedEmail;

public class ReceiveEmailState
{
    public ReceivedEmail email;
    public int currentEmailIndex;
    public int currentUnread;
    public enum State
    {
        Initial,
        FirstUnread,
        NoUnread,
        ReadingEmail,
        NextEmail,
    }

    public State state;

    public ReceiveEmailState()
    {
        email = new ReceivedEmail();
        currentEmailIndex = -1;
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
