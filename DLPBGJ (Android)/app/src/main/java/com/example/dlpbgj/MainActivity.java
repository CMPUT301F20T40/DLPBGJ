package com.example.dlpbgj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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


//Main activity is the login activity. Yet to add comments (Loyal)
public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE1 = "com.example.dlpbgj.MESSAGE1";
    EditText user;
    EditText pass;
    TextView msg;
    Button login;
    Button signUp;
    Button MyInfo;
    String TAG = "Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.editUserName);
        pass = findViewById(R.id.editUserPassword);
        login = findViewById(R.id.login_button);
        signUp = findViewById(R.id.signup_button);
        MyInfo = findViewById(R.id.MyInfo);
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
            public void onClick(final View v) {
                final String userName = user.getText().toString();
                final String userPass = pass.getText().toString();
                final User newUser = new User(userName,userPass);
                DocumentReference docRef = collectionReference.document(userName); //If username does not exist then prompt for a sign-up
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                Map<String,Object> data = document.getData();
                                final String temp = (String)data.get("Password");
                                if (Objects.equals(temp, userPass)){

                                    Toast toast = Toast.makeText(v.getContext(), sucess, Toast.LENGTH_SHORT);
                                    toast.show();
                                    //TODO Initialize new activity after login Successful and pass user object in it
                                    Intent intent = new Intent(getApplicationContext(),HomePage.class);
                                    intent.putExtra(EXTRA_MESSAGE1, newUser);
                                    startActivity(intent);

                                }
                                else {
                                    Toast toast = Toast.makeText(v.getContext(), fail, Toast.LENGTH_SHORT);
                                    toast.show();
                                     }
                            }
                            else{
                                Toast toast = Toast.makeText(v.getContext(), noExist, Toast.LENGTH_SHORT);
                                toast.show();
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
            public void onClick(final View v) {
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
                                Toast toast = Toast.makeText(v.getContext(), exist, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else {
                                collectionReference
                                        .document(userName)
                                        .set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG,"Data has been added succesfully");
                                                Toast toast = Toast.makeText(v.getContext(), signUpS, Toast.LENGTH_SHORT);
                                                toast.show();
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
        MyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent UserIntent = new Intent(view.getContext(),UserProfile.class);
                startActivity(UserIntent);
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