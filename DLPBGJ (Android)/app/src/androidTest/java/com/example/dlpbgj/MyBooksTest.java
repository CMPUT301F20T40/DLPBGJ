package com.example.dlpbgj;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Solo;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.*;
import static org.junit.Assert.*;

public class MyBooksTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);


    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void testMyBooks(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.enterText((EditText) solo.getView(R.id.editUserName),"testusername");
        solo.enterText((EditText) solo.getView(R.id.editUserPassword),"testpassword");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        //On clicking MY BOOKS button a new activity should open
        solo.clickOnButton("My Books");
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

        //Testing Add Book
        View fab = solo.getCurrentActivity().findViewById(R.id.add_book_button);
        solo.clickOnView(fab);
        solo.waitForFragmentByTag("ADD_BOOK",5000);
        solo.enterText((EditText) solo.getView(R.id.book_title_editText),"testTitle");
        solo.enterText((EditText) solo.getView(R.id.book_status_editText),"invalidStatus");
        solo.enterText((EditText) solo.getView(R.id.book_description_editText),"testDescription");
        solo.clickOnButton("OK");
        assertTrue(solo.waitForFragmentByTag("ADD_BOOK"));

        solo.clearEditText((EditText) solo.getView(R.id.book_status_editText));
        solo.enterText((EditText) solo.getView(R.id.book_status_editText),"Available");
        solo.clickOnButton("OK");

        solo.waitForDialogToClose();
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

        Activity bookAct = (Activity) solo.getCurrentActivity();
        final ListView bookList = bookAct.findViewById(R.id.book_list);
        solo.waitForText("testTitle");
        Book book = (Book) bookList.getItemAtPosition(0);
        assertEquals("testTitle",book.getTitle());
        assertEquals("testDescription",book.getDescription());
        assertEquals("Available",book.getStatus());
        assertEquals("Unknown",book.getAuthor());
        assertEquals("Unknown",book.getISBN());


        //Testing edit book
        solo.clickInList(0);
        assertTrue(solo.waitForFragmentByTag("ADD_BOOK"));
        solo.clearEditText((EditText) solo.getView(R.id.book_title_editText));
        solo.enterText((EditText) solo.getView(R.id.book_title_editText),"testTitle2");
        solo.clearEditText((EditText) solo.getView(R.id.book_status_editText));
        solo.enterText((EditText) solo.getView(R.id.book_status_editText),"Borrowed");
        solo.clearEditText((EditText) solo.getView(R.id.book_author_editText));
        solo.enterText((EditText) solo.getView(R.id.book_author_editText),"testAuthor");
        solo.clickOnButton("OK");
        solo.waitForDialogToClose();


        solo.waitForText("testTitle2");
        Book ebook = (Book) bookList.getItemAtPosition(0);
        assertEquals("testTitle2",ebook.getTitle());
        assertEquals("testDescription",ebook.getDescription());
        assertEquals("Borrowed",ebook.getStatus());
        assertEquals("testAuthor",ebook.getAuthor());
        assertEquals("Unknown",ebook.getISBN());



        //Testing filtering
        solo.clickOnView(fab);
        solo.waitForFragmentByTag("ADD_BOOK",5000);
        solo.enterText((EditText) solo.getView(R.id.book_title_editText),"testTitle3");
        solo.enterText((EditText) solo.getView(R.id.book_status_editText),"Available");
        solo.enterText((EditText) solo.getView(R.id.book_description_editText),"testDescription");
        solo.clickOnButton("OK");
        solo.waitForDialogToClose();
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

        solo.clickOnView(fab);
        solo.waitForFragmentByTag("ADD_BOOK",5000);
        solo.enterText((EditText) solo.getView(R.id.book_title_editText),"testTitle4");
        solo.enterText((EditText) solo.getView(R.id.book_status_editText),"Borrowed");
        solo.enterText((EditText) solo.getView(R.id.book_description_editText),"testDescription");
        solo.clickOnButton("OK");
        solo.waitForDialogToClose();
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

        solo.clickOnView(fab);
        solo.waitForFragmentByTag("ADD_BOOK",5000);
        solo.enterText((EditText) solo.getView(R.id.book_title_editText),"testTitle5");
        solo.enterText((EditText) solo.getView(R.id.book_status_editText),"Requested");
        solo.enterText((EditText) solo.getView(R.id.book_description_editText),"testDescription");
        solo.clickOnButton("OK");
        solo.waitForDialogToClose();
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

        solo.waitForText("testTitle3");
        solo.waitForText("testTitle4");
        solo.waitForText("testTitle5");

        solo.clickOnCheckBox(0);
        assertTrue(solo.searchText("testTitle3"));
        assertFalse(solo.searchText("testTitle2"));
        assertFalse(solo.searchText("testTitle4"));
        assertFalse(solo.searchText("testTitle5"));


        for(int i=0;i<bookList.getCount();i++){
            Book b=(Book) bookList.getItemAtPosition(i);
            String str=b.getStatus();
            assertEquals("Available",str);
        }

        solo.clickOnCheckBox(0);
        solo.clickOnCheckBox(1);
        assertTrue(solo.searchText("testTitle2"));
        assertTrue(solo.searchText("testTitle4"));
        assertFalse(solo.searchText("testTitle3"));
        assertFalse(solo.searchText("testTitle5"));

        for(int i=0;i<bookList.getCount();i++){
            Book b=(Book) bookList.getItemAtPosition(i);
            String str=b.getStatus();
            assertEquals("Borrowed",str);
        }

        solo.clickOnCheckBox(1);

        solo.clickOnCheckBox(0);
        solo.clickOnCheckBox(1);
        assertTrue(solo.searchText("testTitle2"));
        assertTrue(solo.searchText("testTitle3"));
        assertTrue(solo.searchText("testTitle4"));
        assertFalse(solo.searchText("testTitle5"));

        for(int i=0;i<bookList.getCount();i++){
            Book b=(Book) bookList.getItemAtPosition(i);
            String str=b.getStatus();
            assertNotEquals("Requested",str);
        }

        solo.clickOnCheckBox(0);
        solo.clickOnCheckBox(1);

        assertTrue(solo.searchText("testTitle2"));
        assertTrue(solo.searchText("testTitle3"));
        assertTrue(solo.searchText("testTitle4"));
        assertTrue(solo.searchText("testTitle5"));


        //Testing deleting books
        solo.clickInList(0);
        assertTrue(solo.waitForFragmentByTag("ADD_BOOK"));
        solo.clickOnButton("delete");
        solo.waitForDialogToClose();
        assertFalse(solo.searchText("testTitle2"));

        solo.clickInList(0);
        assertTrue(solo.waitForFragmentByTag("ADD_BOOK"));
        solo.clickOnButton("delete");
        solo.waitForDialogToClose();
        assertFalse(solo.searchText("testTitle3"));


        solo.clickInList(0);
        assertTrue(solo.waitForFragmentByTag("ADD_BOOK"));
        solo.clickOnButton("delete");
        solo.waitForDialogToClose();
        assertFalse(solo.searchText("testTitle4"));


        solo.clickInList(0);
        assertTrue(solo.waitForFragmentByTag("ADD_BOOK"));
        solo.clickOnButton("delete");
        solo.waitForDialogToClose();
        assertFalse(solo.searchText("testTitle5"));

        assertEquals(0,bookList.getCount());

        //Did not test for SCAN ISBN code as well as uploading a picture since emulator doesn't have camera access and pictures are not same on every device
    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
