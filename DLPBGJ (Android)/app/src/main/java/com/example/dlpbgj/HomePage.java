package com.example.dlpbgj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//As soon as the user successfully logs in, this activity gets invoked. This is the home page of the user.
public class HomePage extends AppCompatActivity {
    public static final String EXTRA_MESSAGE2 = "com.example.dlpbgj.MESSAGE2";
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        currentUser = (User)getIntent().getSerializableExtra(MainActivity.EXTRA_MESSAGE1);  //Catching the user object given by the MainActivity
        setContentView(R.layout.activity_home_page);
        Button myBooksButton=findViewById(R.id.MyBooks);


        myBooksButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {        //When user clicks this button, a list of all the books that the owner owns is shown
                Intent intent = new Intent(getApplicationContext(),MyBooks.class);
                intent.putExtra(EXTRA_MESSAGE2, currentUser);   //Sending the current user as a parameter to the MyBooks activity
                startActivity(intent);
            }


        });



    }
}