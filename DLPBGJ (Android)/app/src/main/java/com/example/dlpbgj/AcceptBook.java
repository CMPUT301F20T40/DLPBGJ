package com.example.dlpbgj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class AcceptBook extends AppCompatActivity {
    String bookISBN;
    User currentUser;
    TextView ISBN;
    Spinner spinner;
    ArrayList<String> users;
    ArrayList<String> bookNames;
    String user;
    String book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);
        Button scan = findViewById(R.id.scanisbn);
        Button returnBook = findViewById(R.id.returnBook);
        ISBN = findViewById(R.id.ISBN_book);
        spinner =  findViewById(R.id.returnList);
        users = new ArrayList<>();
        bookNames = new ArrayList<>();
        returnBook.setText("Get Book Back");
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), barcode_scanner.class);
                startActivityForResult(intent, 1);
            }
        });

        returnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (users.size() == 0){
                    if (bookISBN == null){
                        Toast toast = Toast.makeText(getApplicationContext(), "Please scan an ISBN code to get the book back!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        user = null;
                        book = null;
                        bookISBN = null;
                        Toast toast = Toast.makeText(getApplicationContext(), "Scanned ISBN code does not match any book!\nPlease scan again.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else if (user == null){
                    Toast toast = Toast.makeText(getApplicationContext(), "Please select a user to get the book from ", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference collectionReference = db.collection("Users/" + currentUser.getUsername() + "/MyBooks");
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("Borrower",null);
                    map.put("Book Status","Available");
                    collectionReference
                            .document(book)
                            .update(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Accept","Book values successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Accept","Book values failed to update!");
                                }
                            });
                    Toast toast = Toast.makeText(getApplicationContext(), "Book successfully retrieved from" + user, Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                    startActivity(getIntent());
                }

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                user = users.get(i);
                book = bookNames.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                user = null;
                book = null;
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == -1) {
                bookISBN = data.getStringExtra("ISBN");
                ISBN.setText("ISBN - " + bookISBN);
                String temp = bookISBN;
                checkFunc(temp);
            }
        }
    }

    public void checkFunc(final String isbn){
        users.clear();
        bookNames.clear();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        currentUser = (User)getIntent().getSerializableExtra("User");
        final CollectionReference foruser = db.collection("Users/" + currentUser.getUsername() + "/MyBooks");
        foruser.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot f : value){
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, users);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    if((isbn.equals((String) f.getData().get("Book ISBN")))){
                        if (f.getData().get("Borrower") != null){
                            users.add((String)f.getData().get("Borrower"));
                            bookNames.add(f.getId());
                            setSpinner();
                        }
                    }
                }
            }
        });
    }

    public void setSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
