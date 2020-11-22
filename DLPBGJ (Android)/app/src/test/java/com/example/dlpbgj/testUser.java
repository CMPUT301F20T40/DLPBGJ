package com.example.dlpbgj;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class testUser {

    @Test
    public void testUserClass(){
        //Creating a new User object
        User testUser = new User("testUsername", "testPass");


        //Testing username
        assertEquals("testUsername", testUser.getUsername());
        testUser.setUsername("testUsername1");
        assertEquals("testUsername1", testUser.getUsername());

        //Testing password
        assertEquals("testPass",testUser.getPassword());
        testUser.setPassword("testPass1");
        assertEquals("testPass1",testUser.getPassword());

        //Testing contact
        testUser.setContact("testContact");
        assertEquals("testContact", testUser.getContact());

        //Testing first name
        testUser.setFirst_name("testFN");
        assertEquals("testFN", testUser.getFirst_name());

        //Testing last name
        testUser.setLast_name("testLN");
        assertEquals("testLN", testUser.getLast_name());


    }

}
