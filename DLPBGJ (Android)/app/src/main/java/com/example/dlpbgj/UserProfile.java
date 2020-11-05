package com.example.dlpbgj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        final String FirstName;
        final String LastName;
        final String BirthDate;
        final String Prefrence;
        final String Search;
        final String ContactInfo;
        FirebaseFirestore Userdb;
        TextView UserFirstName = (TextView) findViewById(R.id.UserFirstName);
        TextView UserLastName = (TextView) findViewById(R.id.UserLastName);
        TextView UserBirthDate = (TextView) findViewById(R.id.UserBirthDate);
        TextView UserPrefrence = (TextView) findViewById(R.id.UserFav);
        TextView UserSearch = (TextView) findViewById(R.id.SearchUser);
        TextView UserContact = (TextView) findViewById(R.id.ContactInfo);
        FirstName = UserFirstName.getText().toString();
        LastName = UserLastName.getText().toString();
        BirthDate = UserBirthDate.getText().toString();
        Prefrence = UserPrefrence.getText().toString();
        Search = UserSearch.getText().toString();
        ContactInfo = UserContact.getText().toString();
        Userdb = FirebaseFirestore.getInstance();
    }
}