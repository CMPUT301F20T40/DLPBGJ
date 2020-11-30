package com.example.dlpbgj;

public class Statuses {
    String Accepted;
    String Requested;
    String Borrowed;
    String Available;
    String Returned;
    String Declined;

    public Statuses() {
        this.Accepted = "Accepted";
        this.Requested = "Requested";
        this.Borrowed = "Borrowed";
        this.Returned = "Returned";
        this.Available = "Available";
        this.Declined = "Declined";

    }

    public String getAccepted() {
        return Accepted;
    }

    public String getRequested() {
        return Requested;
    }

    public String getBorrowed() {
        return Borrowed;
    }

    public String getAvailable() {
        return Available;
    }

    public String getReturned() {
        return Returned;
    }

    public String getDeclined() {
        return Declined;
    }
}
