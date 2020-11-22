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

public class SearchTest {
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
    public void testSearch(){

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.enterText((EditText) solo.getView(R.id.editUserName),"testusername");
        solo.enterText((EditText) solo.getView(R.id.editUserPassword),"testpassword");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnButton("My Books");
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);
        View fab = solo.getCurrentActivity().findViewById(R.id.add_book_button);

        solo.clickOnView(fab);
        solo.waitForFragmentByTag("ADD_BOOK",5000);
        solo.enterText((EditText) solo.getView(R.id.book_title_editText),"testTitle1");
        solo.enterText((EditText) solo.getView(R.id.book_status_editText),"Available");
        solo.enterText((EditText) solo.getView(R.id.book_description_editText),"testDescription1");
        solo.clickOnButton("OK");
        solo.waitForDialogToClose();
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);


        solo.clickOnView(fab);
        solo.waitForFragmentByTag("ADD_BOOK",5000);
        solo.enterText((EditText) solo.getView(R.id.book_title_editText),"testTitle2");
        solo.enterText((EditText) solo.getView(R.id.book_status_editText),"Available");
        solo.enterText((EditText) solo.getView(R.id.book_description_editText),"testDescription2");
        solo.clickOnButton("OK");
        solo.waitForDialogToClose();
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);


        solo.clickOnView(fab);
        solo.waitForFragmentByTag("ADD_BOOK",5000);
        solo.enterText((EditText) solo.getView(R.id.book_title_editText),"testTitle3");
        solo.enterText((EditText) solo.getView(R.id.book_status_editText),"Borrowed");
        solo.enterText((EditText) solo.getView(R.id.book_description_editText),"testDescription1");
        solo.clickOnButton("OK");
        solo.waitForDialogToClose();
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);


        solo.waitForText("testTitle1");
        solo.waitForText("testTitle2");
        solo.waitForText("testTitle3");

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnButton("SIGN OUT");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.enterText((EditText) solo.getView(R.id.editUserName),"testusername2");
        solo.enterText((EditText) solo.getView(R.id.editUserPassword),"testpassword2");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnButton("My Books");
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);
        View fab2 = solo.getCurrentActivity().findViewById(R.id.add_book_button);

        solo.clickOnView(fab2);
        solo.waitForFragmentByTag("ADD_BOOK",5000);
        solo.enterText((EditText) solo.getView(R.id.book_title_editText),"testTitle4");
        solo.enterText((EditText) solo.getView(R.id.book_status_editText),"Borrowed");
        solo.enterText((EditText) solo.getView(R.id.book_description_editText),"testDescription2");
        solo.clickOnButton("OK");
        solo.waitForDialogToClose();
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

        solo.clickOnView(fab2);
        solo.waitForFragmentByTag("ADD_BOOK",5000);
        solo.enterText((EditText) solo.getView(R.id.book_title_editText),"testTitle5");
        solo.enterText((EditText) solo.getView(R.id.book_status_editText),"Available");
        solo.enterText((EditText) solo.getView(R.id.book_description_editText),"testDescription1");
        solo.clickOnButton("OK");
        solo.waitForDialogToClose();
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

        solo.waitForText("testTitle4");
        solo.waitForText("testTitle5");

        solo.goBack();

        solo.clickOnButton("Search");
        solo.assertCurrentActivity("Wrong Activity", Search_by_descr.class);
        Activity searchAct = (Activity) solo.getCurrentActivity();
        final ListView searchList = searchAct.findViewById(R.id.book_list);

        solo.enterText((EditText) solo.getView(R.id.description), "testDescription2");
        solo.clickOnButton("SEARCH");
        solo.waitForText("testTitle2");
        for(int i=0;i<searchList.getCount();i++){
            Book b=(Book) searchList.getItemAtPosition(i);
            String str=b.getDescription();
            assertEquals("testDescription2",str);
        }


        solo.clearEditText((EditText) solo.getView(R.id.description));
        solo.enterText((EditText) solo.getView(R.id.description), "testDescription1");
        solo.clickOnButton("SEARCH");
        solo.waitForText("testTitle1");
        for(int i=0;i<searchList.getCount();i++){
            Book b=(Book) searchList.getItemAtPosition(i);
            String str=b.getDescription();
            assertEquals("testDescription1",str);
        }


        solo.clickOnCheckBox(0);
        assertTrue(solo.searchText("testTitle1"));
        assertTrue(solo.searchText("testTitle5"));
        assertFalse(solo.searchText("testTitle3"));

        for(int i=0;i<searchList.getCount();i++){
            Book b=(Book) searchList.getItemAtPosition(i);
            String str=b.getStatus();
            assertEquals("Available",str);
        }

        solo.clickOnCheckBox(0);
        solo.clickOnCheckBox(1);
        assertTrue(solo.searchText("testTitle3"));
        assertFalse(solo.searchText("testTitle1"));
        assertFalse(solo.searchText("testTitle5"));


        for(int i=0;i<searchList.getCount();i++){
            Book b=(Book) searchList.getItemAtPosition(i);
            String str=b.getStatus();
            assertEquals("Borrowed",str);
        }

        solo.clickOnCheckBox(1);

        solo.clickOnCheckBox(0);
        solo.clickOnCheckBox(1);
        assertTrue(solo.searchText("testTitle1"));
        assertTrue(solo.searchText("testTitle3"));
        assertTrue(solo.searchText("testTitle5"));

        for(int i=0;i<searchList.getCount();i++){
            Book b=(Book) searchList.getItemAtPosition(i);
            String str=b.getStatus();
            assertNotEquals("Requested",str);
        }

        solo.clickOnCheckBox(0);
        solo.clickOnCheckBox(1);




        //Deleting the books for testusername2
        solo.goBack();
        solo.clickOnButton("My Books");
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

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

        Activity bookAct = (Activity) solo.getCurrentActivity();
        final ListView bookList = bookAct.findViewById(R.id.book_list);
        assertEquals(0,bookList.getCount());

        //Logging out
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);
        solo.clickOnButton("SIGN OUT");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Logging in for testusername
        solo.enterText((EditText) solo.getView(R.id.editUserName),"testusername");
        solo.enterText((EditText) solo.getView(R.id.editUserPassword),"testpassword");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnButton("My Books");
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

        //Deleting books for testusername
        solo.clickInList(0);
        assertTrue(solo.waitForFragmentByTag("ADD_BOOK"));
        solo.clickOnButton("delete");
        solo.waitForDialogToClose();
        assertFalse(solo.searchText("testTitle1"));


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

        Activity bookAct1 = (Activity) solo.getCurrentActivity();
        final ListView bookList1 = bookAct1.findViewById(R.id.book_list);
        assertEquals(0,bookList1.getCount());

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);
        solo.clickOnButton("SIGN OUT");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
