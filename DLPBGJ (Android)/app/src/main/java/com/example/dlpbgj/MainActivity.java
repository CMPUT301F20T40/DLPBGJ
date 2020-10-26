package com.example.dlpbgj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.nfc.Tag;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.proto.TargetGlobal;

import java.util.HashMap;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private EditText user;
    private EditText pass;
    private TextView msg;
    private Button login;
    private Button signUp;
    private ArrayList<User> userDataList;
    private String TAG = "Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDataList = new ArrayList<>();
        user = findViewById(R.id.editUserName);
        pass = findViewById(R.id.editUserPassword);
        msg = findViewById(R.id.displayMessage);
        String userName = user.getText().toString();
        String password = pass.getText().toString();
        final User newUser = new User(userName,password);
        login = findViewById(R.id.login_button);
        signUp = findViewById(R.id.signup_button);


        FirebaseFirestore userDb;

        //Instance of the User db
        userDb = FirebaseFirestore.getInstance();

        // Creating a top level collection for User database
        final CollectionReference collectionReference = userDb.collection("Users");
        final HashMap<String, String> data = new HashMap<>();
        data.put("Password", newUser.getPassword());
        final DocumentReference docRef = userDb.collection("Users").document(newUser.getUsername());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                msg.setText("Login Successful");
                            }
                            else{
                                msg.setText("Please SignUp!");
                            }
                        }
                        else{
                            Log.d(TAG, "get failed with", task.getException());
                        }
                    }
                });
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                msg.setText("Username Already exists!");
                            }
                            else{
                                collectionReference
                                        .document(newUser.getUsername())
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // These are a method which gets executed when the task is succeeded
                                                msg.setText("Successfully Signed Up");
                                                Log.d(TAG, "Data has been added successfully!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // These are a method which gets executed if there’s any problem
                                                msg.setText("Sign Up failed");
                                                Log.d(TAG, "Data could not be added!" + e.toString());
                                            }
                                        });
                            }
                        }
                        else{
                            Log.d(TAG, "get failed with", task.getException());
                        }
                    }

                });
            }
        });

        //for realtime database update
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
            }
        });

    }

}