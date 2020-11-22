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
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        //On clicking MY INFO button a new activity should open
        solo.clickOnButton("My INFO");
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);

        //Activity infoAct = (Activity) solo.getCurrentActivity();
        solo.enterText((EditText) solo.getView(R.id.UserFirstName),"testFN");
        solo.enterText((EditText) solo.getView(R.id.UserLastName),"testLN");
        solo.enterText((EditText) solo.getView(R.id.UserBirthDate),"testBDay");
        solo.enterText((EditText) solo.getView(R.id.ContactInfo),"testEmail");
        solo.enterText((EditText) solo.getView(R.id.UserFav),"testFavGenre");

        solo.clickOnButton("UPDATE");
        solo.clickOnButton("BACK");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

        final TextView tv = (TextView) solo.getCurrentActivity().findViewById(R.id.MyName);
        System.out.println(tv.getText().toString());
        //assertEquals("testFN testLN's Library",tv.getText().toString());

        solo.clickOnButton("My INFO");
        solo.assertCurrentActivity("Wrong Activity", UserProfile.class);
        EditText firstName = solo.getCurrentActivity().findViewById(R.id.UserFirstName);
        EditText lastName = solo.getCurrentActivity().findViewById(R.id.UserLastName);
        EditText bDay = solo.getCurrentActivity().findViewById(R.id.UserBirthDate);
        EditText contactInfo = solo.getCurrentActivity().findViewById(R.id.ContactInfo);
        EditText favGenre = solo.getCurrentActivity().findViewById(R.id.UserFav);
        System.out.println(firstName.getText().toString());
        System.out.println(lastName.getText().toString());
        System.out.println(bDay.getText().toString());
        System.out.println(contactInfo.getText().toString());
        System.out.println(favGenre.getText().toString());
/*
        assertEquals("testFN", firstName.getText().toString());
        assertEquals("testLN", lastName.getText().toString());
        assertEquals("testBDay", bDay.getText().toString());
        assertEquals("testEmail", contactInfo.getText().toString());
        assertEquals("testFavGenre", favGenre.getText().toString());
*/

        solo.clickOnButton("BACK");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);


    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
