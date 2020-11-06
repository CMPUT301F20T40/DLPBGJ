package com.example.dlpbgj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
        final User user = (User) getIntent().getSerializableExtra("User");
        Button update;
        Button back;
        FirebaseFirestore Userdb;
        final EditText UserFirstName = (EditText) findViewById(R.id.UserFirstName);
        final EditText UserLastName = (EditText) findViewById(R.id.UserLastName);
        final EditText UserBirthDate = (EditText) findViewById(R.id.UserBirthDate);
        final EditText UserPrefrence = (EditText) findViewById(R.id.UserFav);
        final EditText UserName = (EditText) findViewById(R.id.UserName);
        final EditText UserContact = (EditText) findViewById(R.id.ContactInfo);
        back = findViewById(R.id.BackButton);
        update = findViewById(R.id.Update);
        UserFirstName.setText(user.getFirst_name());
        UserLastName.setText(user.getLast_name());
        UserBirthDate.setText(user.getDOB());
        UserName.setText(user.getUsername());
        Userdb = FirebaseFirestore.getInstance();
        final CollectionReference userBookCollectionReference = Userdb.collection("Users");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String FirstName = UserFirstName.getText().toString();
                final String LastName = UserLastName.getText().toString();
                final String BirthDate = UserBirthDate.getText().toString();
                final String Prefrence = UserPrefrence.getText().toString();
                final String User_name = UserName.getText().toString();
                final String ContactInfo = UserContact.getText().toString();
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
        });
    }
}