package com.example.dlpbgj;// LocationDetails
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dlpbgj.R;

public class LocationDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        final EditText AddressFeed = (EditText)findViewById(R.id.AddressChanel);
        Button Run = (Button)findViewById(R.id.LookUP);
        final String[] Address = {""};
        Run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserLocation.class);
                Address[0] = AddressFeed.getText().toString();
                intent.putExtra("Address", Address[0]);
                startActivity(intent);
            }
        });
    }
}