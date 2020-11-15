package com.example.dlpbgj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//As soon as the user successfully logs in, this activity gets invoked. This is the home page of the user.
public class HomePage extends AppCompatActivity {
    public static final String EXTRA_MESSAGE2 = "com.example.dlpbgj.MESSAGE2";
    private User currentUser;

    /**
     * Activity is launched when a user successfully signs in.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Intent intent = getIntent();
        currentUser = (User)getIntent().getSerializableExtra(MainActivity.EXTRA_MESSAGE1);//Catching the user object given by the MainActivity
        Button info_button = findViewById(R.id.MyInfo);
        Button myBooksButton=findViewById(R.id.MyBooks);
        Button search = findViewById(R.id.Search);
        Button signOut = findViewById(R.id.SignOut);
        Button request = findViewById(R.id.RequestABook);


        final String success = "Signed Out!";


        myBooksButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {        //When user clicks this button, a list of all the books that the owner owns is shown
                Intent intent = new Intent(getApplicationContext(),MyBooks.class);
                intent.putExtra(EXTRA_MESSAGE2, currentUser);   //Sending the current user as a parameter to the MyBooks activity
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nIntent = new Intent(view.getContext(),MainActivity.class);
                Toast toast = Toast.makeText(view.getContext(), success, Toast.LENGTH_SHORT);
                toast.show();
                startActivity(nIntent);
            }
        });

       info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),UserProfile.class);
                intent.putExtra("User",currentUser);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(), Search_by_descr.class);
                intent.putExtra("User", currentUser);
                startActivity(intent);
            }
        });

    }
}