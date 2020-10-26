package com.example.dlpbgj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText user;
    private EditText pass;
    private Button login;
    private Button signUp;
    private ArrayList<User> userDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDataList = new ArrayList<>();
        user = findViewById(R.id.editUserName);
        pass = findViewById(R.id.editUserPassword);
        String userName = user.getText().toString();
        String password = pass.getText().toString();
        User newUser = new User(userName,password);
        login = findViewById(R.id.login_button);
        signUp = findViewById(R.id.signup_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}