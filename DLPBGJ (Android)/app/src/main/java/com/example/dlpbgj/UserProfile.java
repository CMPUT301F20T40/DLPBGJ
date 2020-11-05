package com.example.dlpbgj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class UserProfile extends AppCompatActivity implements Serializable  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("UserObject");
        Button back = findViewById(R.id.Back);

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
        TextView UserName = (TextView) findViewById(R.id.UserName);
        //TextView UserSearch = (TextView) findViewById(R.id.SearchUser);
        TextView UserContact = (TextView) findViewById(R.id.ContactInfo);
        UserFirstName.setText(user.getFirstName());
        UserLastName.setText(user.getLastName());
        UserBirthDate.setText(user.getDob());
        UserName.setText(user.getUsername());
        UserContact.setText(user.getEmail());

        /*
        FirstName = UserFirstName.getText().toString();
        LastName = UserLastName.getText().toString();
        BirthDate = UserBirthDate.getText().toString();
        Prefrence = UserPrefrence.getText().toString();
        Search = UserSearch.getText().toString();
        ContactInfo = UserContact.getText().toString();
        Userdb = FirebaseFirestore.getInstance();

         */

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent oldIntent = new Intent(UserProfile.this,HomePage.class);
                startActivity(oldIntent);
            }
        });
    }
}