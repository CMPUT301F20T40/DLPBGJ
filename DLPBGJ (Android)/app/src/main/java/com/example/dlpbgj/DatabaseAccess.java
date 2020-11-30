package com.example.dlpbgj;

public class DatabaseAccess {
    String Author;
    String Description;
    String ISBN;
    String Status;
    String Borrower;
    String Notifications;
    String Owner;
    String Location;
    String Requests;
    String UID;

    public DatabaseAccess() {
        this.Author = "Book Author";
        this.Description = "Book Description";
        this.ISBN = "Book ISBN";
        this.Status = "Book Status";
        this.Borrower = "Borrower";
        this.Notifications = "Notifications";
        this.Owner = "Owner";
        this.Location = "Pickup Location";
        this.Requests = "Requests";
        this.UID = "Uid";
    }

    public String getAuthor() {
        return Author;
    }

    public String getDescription() {
        return Description;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getStatus() {
        return Status;
    }

    public String getBorrower() {
        return Borrower;
    }

    public String getNotifications() {
        return Notifications;
    }

    public String getOwner() {
        return Owner;
    }

    public String getLocation() {
        return Location;
    }

    public String getRequests() {
        return Requests;
    }

    public String getUID() {
        return UID;
    }
}
