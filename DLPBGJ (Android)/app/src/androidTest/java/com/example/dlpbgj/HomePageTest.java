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
        solo.clickOnView(solo.getView(R.id.MyBooks));
        solo.assertCurrentActivity("Wrong Activity", MyBooks.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnView(solo.getView(R.id.MyInfo));
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnView(solo.getView(R.id.Search));
        solo.assertCurrentActivity("Wrong Activity", Search_by_descr.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnView(solo.getView(R.id.Requests));
        solo.assertCurrentActivity("Wrong Activity", View_Requests.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnView(solo.getView(R.id.BookRequests));
        solo.assertCurrentActivity("Wrong Activity", BookRequests.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnView(solo.getView(R.id.Return));
        solo.assertCurrentActivity("Wrong Activity", ReturnBook.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnView(solo.getView(R.id.Accept));
        solo.assertCurrentActivity("Wrong Activity", AcceptBook.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnView(solo.getView(R.id.Borrowed));
        solo.assertCurrentActivity("Wrong Activity", View_Borrowed.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnView(solo.getView(R.id.getLocation));
        solo.assertCurrentActivity("Wrong Activity", UserLocation.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnView(solo.getView(R.id.viewNotifications));
        solo.assertCurrentActivity("Wrong Activity", ViewNotifications.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnView(solo.getView(R.id.UserProfiles));
        solo.assertCurrentActivity("Wrong Activity", allUserProfiles.class);

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);



    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
