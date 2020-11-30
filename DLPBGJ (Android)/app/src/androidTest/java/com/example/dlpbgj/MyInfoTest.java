package com.example.dlpbgj;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.*;

import static org.junit.Assert.assertEquals;

public class MyInfoTest {
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
    public void testMyInfo(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.enterText((EditText) solo.getView(R.id.editUserName),"testusername");
        solo.enterText((EditText) solo.getView(R.id.editUserPassword),"testpassword");
        solo.clickOnButton("LOGIN");
        solo.waitForActivity(HomePage.class);
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        //On clicking MY INFO button a new activity should open
        solo.clickOnView(solo.getView(R.id.MyInfo));
        solo.waitForActivity(UserProfile.class);
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);

        //Activity infoAct = (Activity) solo.getCurrentActivity();
        solo.enterText((EditText) solo.getView(R.id.UserFirstName),"testFN");
        solo.enterText((EditText) solo.getView(R.id.UserLastName),"testLN");
        solo.enterText((EditText) solo.getView(R.id.UserBirthDate),"testBDay");
        solo.enterText((EditText) solo.getView(R.id.phoneNumber),"12345678");
        solo.enterText((EditText) solo.getView(R.id.emailAddress),"testEmail");
        solo.enterText((EditText) solo.getView(R.id.UserFav),"testFavGenre");

        solo.clickOnView(solo.getView(R.id.Update));
        solo.clickOnView(solo.getView(R.id.BackButton));
        solo.waitForActivity(HomePage.class);
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        solo.clickOnView(solo.getView(R.id.MyInfo));
        solo.waitForActivity(UserProfile.class);
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);

        solo.clearEditText((EditText) solo.getView(R.id.UserFirstName));
        solo.clearEditText((EditText) solo.getView(R.id.UserLastName));
        solo.clearEditText((EditText) solo.getView(R.id.UserBirthDate));
        solo.clearEditText((EditText) solo.getView(R.id.emailAddress));
        solo.clearEditText((EditText) solo.getView(R.id.UserFav));
        solo.clearEditText((EditText) solo.getView(R.id.phoneNumber));

        solo.clickOnView(solo.getView(R.id.BackButton));
        solo.waitForActivity(HomePage.class);
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);


    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
