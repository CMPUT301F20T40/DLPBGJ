package com.example.dlpbgj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RequestBooks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_books);
        final String RequestUserId;
        final String RequestBookName;
        final String RequestContactInfo;

        EditText UserID = (EditText) findViewById(R.id.UserNameR);
        EditText BookID = (EditText) findViewById(R.id.RequestedBookR);
        EditText ContactInfo = (EditText) findViewById(R.id.PWC);
        Button Send = (Button) findViewById(R.id.SendRequest);
        Button goback = (Button) findViewById(R.id.Return);

        RequestUserId = UserID.getText().toString();
        RequestBookName = BookID.getText().toString();
        RequestContactInfo = ContactInfo.getText().toString();
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent requestIntent = new Intent(view.getContext(),sendNoti.class);
                requestIntent.putExtra("UserID",RequestUserId);
                requestIntent.putExtra("BookName",RequestBookName);
                requestIntent.putExtra("ContactInfo",RequestContactInfo);
                startActivity(requestIntent);
            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent(view.getContext(),HomePage.class);
                startActivity(returnIntent);
            }
        });
    }
}