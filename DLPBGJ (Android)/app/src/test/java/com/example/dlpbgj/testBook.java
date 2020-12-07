package com.example.dlpbgj;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class testBook {
    ArrayList<String> testReq = new ArrayList<String>();
    private void createRequests() {
        testReq.add("R1");
        testReq.add("R2");
        testReq.add("R3");
        testReq.add("R4");
    }

    @Test
    void testBookClass() {
        //Creating qn array list of strings containing requests
        createRequests();

        //Creating a new Book object
        final Book testBook = new Book("testTitle", "testAuthor", "testISBN", "Available", "testDescr", "testOwner", testReq);

        //Testing title
        assertEquals("testTitle", testBook.getTitle());
        testBook.setTitle("testTitle1");
        assertEquals("testTitle1", testBook.getTitle());

        //Testing requests
        assertEquals(testReq, testBook.getRequests());
        assertEquals(4,testBook.getRequests().size());
        assertThrows(IllegalArgumentException.class, () -> {
            testBook.removeRequest("R5");
        });
        testBook.removeRequest("R4");
        assertFalse(testBook.getRequests().containsValue("R4"));
        assertEquals(3,testBook.getRequests().size());
        testBook.emptyRequests();
        assertEquals(0,testBook.getRequests().size());

        //Testing UID
        testBook.setUid("testUID");
        assertEquals("testUID", testBook.getUid());

        //Testing author
        assertEquals("testAuthor",testBook.getAuthor());
        testBook.setAuthor("testAuthor1");
        assertEquals("testAuthor1",testBook.getAuthor());

        //Testing ISBN
        assertEquals("testISBN",testBook.getISBN());
        testBook.setISBN("testISBN1");
        assertEquals("testISBN1",testBook.getISBN());

        //Testing status
        assertEquals("Available", testBook.getStatus());
        testBook.setStatus("Borrowed");
        assertEquals("Borrowed",testBook.getStatus());

        //Testing descriptions
        assertEquals("testDescr",testBook.getDescription());
        testBook.setDescription("testDescr1");
        assertEquals("testDescr1",testBook.getDescription());

        //Testing owner
        assertEquals("testOwner", testBook.getOwner());
        testBook.setOwner("testOwner1");
        assertEquals("testOwner1",testBook.getOwner());

        //Testing borrower
        testBook.setBorrower("testBorrower");
        assertEquals("testBorrower", testBook.getBorrower());


    }
}