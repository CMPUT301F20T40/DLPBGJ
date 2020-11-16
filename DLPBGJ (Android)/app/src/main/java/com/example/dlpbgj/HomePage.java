package com.example.dlpbgj;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Map;

//As soon as the user successfully logs in, this activity gets invoked. This is the home page of the user.
public class HomePage extends AppCompatActivity implements ImageFragement.OnFragmentInteractionListener{
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
        Button requests = findViewById(R.id.Requests);
        Button signOut = findViewById(R.id.SignOut);
        Button bookRequests = findViewById(R.id.BookRequests);
        final ImageView profile = findViewById(R.id.Profile);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference();
        final TextView userName = findViewById(R.id.MyName);
        FirebaseFirestore Userdb = FirebaseFirestore.getInstance();

        final String success = "Signed Out!";




        final CollectionReference userBookCollectionReference = Userdb.collection("Users");
        DocumentReference docRef = userBookCollectionReference.document(currentUser.getUsername()); //If username does not exist then prompt for a sign-up
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        Map<String,Object> data = document.getData();
                        String name = (String)data.get("First Name");
                        name += " " + (String)data.get("Last Name") + "'s Library";
                        userName.setText(name);
                        StorageReference imagesRef = storageReference.child("images/"+currentUser.getUsername());
                        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri downloadUrl)
                            {
                                Glide
                                        .with(getApplicationContext())
                                        .load(downloadUrl.toString())
                                        .centerCrop()
                                        .into(profile);
                            }
                        });
                    }
                }
                else{
                    Log.d("Param","get failed with ",task.getException());
                }
            }
        });


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

        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(), View_Requests.class);
                intent.putExtra("User", currentUser);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageFragement fragement = ImageFragement.newInstance(currentUser);
                fragement.show(getSupportFragmentManager(),"Profile Picture");
            }
        });

        bookRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),BookRequests.class);
                intent.putExtra("User",currentUser);
                startActivity(intent);
            }
        });


    }
    @Override
    public void onBackPressed(){
        //do nothing
    }
}