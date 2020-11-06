package com.example.dlpbgj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        User user = (User) getIntent().getSerializableExtra("User");
        final String FirstName;
        final String LastName;
        final String BirthDate;
        final String Prefrence;
        final String User_name;
        final String ContactInfo;
        FirebaseFirestore Userdb;
        EditText UserFirstName = (EditText) findViewById(R.id.UserFirstName);
        EditText UserLastName = (EditText) findViewById(R.id.UserLastName);
        EditText UserBirthDate = (EditText) findViewById(R.id.UserBirthDate);
        EditText UserPrefrence = (EditText) findViewById(R.id.UserFav);
        EditText UserName = (EditText) findViewById(R.id.UserName);
        EditText UserContact = (EditText) findViewById(R.id.ContactInfo);
        FirstName = UserFirstName.getText().toString();
        LastName = UserLastName.getText().toString();
        BirthDate = UserBirthDate.getText().toString();
        Prefrence = UserPrefrence.getText().toString();
        User_name = UserName.getText().toString();
        ContactInfo = UserContact.getText().toString();
        Userdb = FirebaseFirestore.getInstance();
        CollectionReference userBookCollectionReference = Userdb.collection("Users");
        HashMap<String,Object> data = new HashMap<>();
        data.put("First Name",FirstName);
        data.put("Last Name",LastName);
        data.put("Date of Birth", BirthDate);
        data.put("Email",ContactInfo);
        userBookCollectionReference
                .document(user.getUsername())
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Sample", "Data has been updated successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Sample", "Failed to update the values!");
                    }
                });
    }
}