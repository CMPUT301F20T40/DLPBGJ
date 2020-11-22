package com.example.dlpbgj;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

/*
 * Reference taken from this website for uploading the photo inside the database.
 * https://www.geeksforgeeks.org/android-how-to-upload-an-image-on-firebase-storage/
 */


public class UserProfile extends AppCompatActivity {

    private Button PhotoSelect;
    private  Button PhotoUpload;
    private ImageView imageView;
    private Uri path;
    private final int REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        final User user = (User) getIntent().getSerializableExtra("User");
        Button update;
        Button back;
        final FirebaseFirestore Userdb;
        final EditText UserFirstName = (EditText) findViewById(R.id.UserFirstName);
        final EditText UserLastName = (EditText) findViewById(R.id.UserLastName);
        final EditText UserBirthDate = (EditText) findViewById(R.id.UserBirthDate);
        final EditText UserPrefrence = (EditText) findViewById(R.id.UserFav);
        final EditText UserName = (EditText) findViewById(R.id.UserName);
        final EditText UserContact = (EditText) findViewById(R.id.ContactInfo);
        Userdb = FirebaseFirestore.getInstance();
        back = findViewById(R.id.BackButton);
        update = findViewById(R.id.Update);

        PhotoSelect = findViewById(R.id.button1);
        PhotoUpload = findViewById(R.id.button2);
        imageView = findViewById(R.id.imgView);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        PhotoSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectPhoto();
            }
        });

        PhotoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                uploadPhoto(user);
            }
        });

        final CollectionReference userBookCollectionReference = Userdb.collection("Users");
        DocumentReference docRef = userBookCollectionReference.document(user.getUsername()); //If username does not exist then prompt for a sign-up
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        Map<String,Object> data = document.getData();
                        final String temp = (String)data.get("Password");
                        user.setFirst_name((String)data.get("First Name"));
                        user.setLast_name((String)data.get("Last Name"));
                        user.setDOB((String)data.get("Date of Birth"));
                        user.setContact((String)data.get("Email"));
                        UserFirstName.setText(user.getFirst_name());
                        UserLastName.setText(user.getLast_name());
                        UserBirthDate.setText(user.getDOB());
                        UserName.setText(user.getUsername());
                        UserContact.setText(user.getContact());

                        StorageReference imagesRef = storageReference.child("images/"+user.getUsername());
                        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri downloadUrl)
                            {
                                Glide
                                        .with(getApplicationContext())
                                        .load(downloadUrl.toString())
                                        .centerCrop()
                                        .into(imageView);
                            }
                        });
                    }
                }
                else{
                    Log.d("Param","get failed with ",task.getException());
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FirstName = UserFirstName.getText().toString();
                String LastName = UserLastName.getText().toString();
                String BirthDate = UserBirthDate.getText().toString();
                String Prefrence = UserPrefrence.getText().toString();
                String User_name = UserName.getText().toString();
                String ContactInfo = UserContact.getText().toString();
                User user1 = (User) getIntent().getSerializableExtra("User");
                user1.setFirst_name(FirstName);
                user1.setLast_name(LastName);
                user1.setContact(ContactInfo);
                user1.setDOB(BirthDate);
                Intent intent = new Intent(view.getContext(),HomePage.class);
                intent.putExtra(MainActivity.EXTRA_MESSAGE1,user1);
                startActivity(intent);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String FirstName = UserFirstName.getText().toString();
                final String LastName = UserLastName.getText().toString();
                final String BirthDate = UserBirthDate.getText().toString();
                final String Prefrence = UserPrefrence.getText().toString();
                final String User_name = UserName.getText().toString();
                final String ContactInfo = UserContact.getText().toString();
                HashMap<String,Object> data = new HashMap<>();
                data.put("First Name",FirstName);
                data.put("Last Name",LastName);
                data.put("Date of Birth", BirthDate);
                data.put("Email",ContactInfo);
                userBookCollectionReference
                        .document(user.getUsername())
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Sample", "Data has been updated successfully!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Sample", "Failed to update the values!");
                            }
                        });
            }
        });
    }

    private void SelectPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            path = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadPhoto(User user) {
        if (path != null) {
            final ProgressDialog statusDialog = new ProgressDialog(this);
            statusDialog.setTitle("Uploading");
            statusDialog.show();
            StorageReference ref = storageReference.child("images/" + user.getUsername());
            ref.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    statusDialog.dismiss();
                                    Toast.makeText(UserProfile.this, "Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            statusDialog.dismiss();
                            Toast.makeText(UserProfile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    statusDialog.setMessage("Uploaded " + (int)progress + "%");
                                }
                            });
        }
    }
}