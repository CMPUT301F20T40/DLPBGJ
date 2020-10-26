package com.example.dlpbgj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.nfc.Tag;
import android.os.Build;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.proto.TargetGlobal;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    EditText user;
    EditText pass;
    TextView msg;
    Button login;
    Button signUp;
    String TAG = "Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.editUserName);
        pass = findViewById(R.id.editUserPassword);
        msg = findViewById(R.id.displayMessage);
        login = findViewById(R.id.login_button);
        signUp = findViewById(R.id.signup_button);
        final String sucess = "Login Successful!";
        final String fail = "Invalid Login Details";
        final String noExist = "Please Sign Up";
        final String exist = "User already exists";
        final String signUpS = "Successfully Signed Up";
        final FirebaseFirestore userDb;

        //Instance of the User db
        userDb = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = userDb.collection("Users");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = user.getText().toString();
                final String userPass = pass.getText().toString();
                User newUser = new User(userName,userPass);
                DocumentReference docRef = collectionReference.document(userName);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                Map<String,Object> data = document.getData();
                                final String temp = (String)data.get("Password");
                                System.out.println(temp);
                                System.out.println(userPass);
                                if (Objects.equals(temp, userPass)){
                                    System.out.println("done");
                                    msg.setText(sucess);
                                    //TODO Initialize new activity after login Successful and pass user object in it
                                }
                                else {
                                    msg.setText(fail);}
                            }
                            else{
                                msg.setText(noExist);
                            }
                        }
                        else{
                            Log.d(TAG,"get failed with ",task.getException());
                        }
                    }
                });
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = user.getText().toString();
                final String userPass = pass.getText().toString();
                final User newUser = new User(userName,userPass);
                final HashMap<String,Object> data = new HashMap<>();
                data.put("Password",userPass);
                DocumentReference docRef = collectionReference.document(userName);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                msg.setText(exist);
                            }
                            else {
                                collectionReference
                                        .document(userName)
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG,"Data has been added succesfully");
                                                msg.setText(signUpS);
                                                //TODO Initialize new activity after login Successful and pass user object in it
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG,"Data has been not been added");

                                            }
                                        });
                            }
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