package com.example.dlpbgj;// LocationDetails

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LocationDetails extends AppCompatActivity {
    Button Run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        final EditText AddressFeed = findViewById(R.id.AddressChanel);
        Run = findViewById(R.id.LookUP);
        final String[] Address = {""};
        Run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserLocation.class);
                Address[0] = AddressFeed.getText().toString();
                intent.putExtra("Address", Address[0]);
                startActivity(intent);
            }
        });
    }
}