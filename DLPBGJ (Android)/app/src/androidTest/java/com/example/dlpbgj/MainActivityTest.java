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


public class MainActivityTest {
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
    public void checkLoginLogout(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Enter invalid login details to see if the login is successful or not
        solo.enterText((EditText) solo.getView(R.id.editUserName),"fakeusername");
        solo.enterText((EditText) solo.getView(R.id.editUserPassword),"fakeuserpass");
        solo.clickOnButton("LOGIN");

        //Activity should be the main activity only after entering invalid login details
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clearEditText((EditText) solo.getView(R.id.editUserName));
        solo.clearEditText((EditText) solo.getView(R.id.editUserPassword));

        //Enter valid login details to see if the login is successful or not
        solo.enterText((EditText) solo.getView(R.id.editUserName),"testusername");
        solo.enterText((EditText) solo.getView(R.id.editUserPassword),"testpassword");
        solo.clickOnButton("LOGIN");

        //After successful login the current activity should be the home page activity
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnButton("SIGN OUT");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}