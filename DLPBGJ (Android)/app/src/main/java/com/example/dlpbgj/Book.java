package com.example.dlpbgj;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    String title;
    String author;
    String ISBN;
    String status;
    String description="";
    String owner;
    ArrayList<String> requests;
    String uid;
    String borrower;

    public Book(String title, String author, String ISBN) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
    }

    public Book(String title, String author, String ISBN, String status) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.status = status;
    }

    public Book(String title, String author, String ISBN, String status, String description) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.status = status;
        this.description = description;
    }
    public Book(String title, String author, String ISBN, String status, String description,String owner) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.status = status;
        this.description = description;
        this.owner = owner;
    }

    public Book(String title, String author, String ISBN, String status, String description, String owner, ArrayList<String> req) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.status = status;
        this.description = description;
        this.owner = owner;
        this.requests = req;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<String> requests) {
        this.requests = requests;
    }

    public void removeRequest(String request){
        this.requests.remove(request);
    }

    public void emptyRequests(){
        this.requests.clear();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }
}
