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

public class HomePageTest {
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
    public void testHomePage(){

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.enterText((EditText) solo.getView(R.id.editUserName),"testusername");
        solo.enterText((EditText) solo.getView(R.id.editUserPassword),"testpassword");
        solo.clickOnButton("LOGIN");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        //On clicking MY BOOKS button a new activity should open
        solo.clickOnButton("My Books");
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnButton("My INFO");
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnButton("Search");
        solo.assertCurrentActivity("Wrong Activity", Search_by_descr.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnButton("Book Requests");
        solo.assertCurrentActivity("Wrong Activity", BookRequests.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnButton("My Requests");
        solo.assertCurrentActivity("Wrong Activity", View_Requests.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnButton("Return a Book");
        solo.assertCurrentActivity("Wrong Activity", ReturnBook.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnButton("Borrowed Books");
        solo.assertCurrentActivity("Wrong Activity", View_Borrowed.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);



    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
