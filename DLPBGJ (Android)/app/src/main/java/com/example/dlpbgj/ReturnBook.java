package com.example.dlpbgj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class ReturnBook extends AppCompatActivity {
    String bookISBN;
    User currentUser;
    TextView ISBN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);
        Button scan = findViewById(R.id.scanisbn);
        Button returnBook = findViewById(R.id.returnBook);
        ISBN = findViewById(R.id.ISBN_book);
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
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference userCollection = db.collection("Users");
                currentUser = (User)getIntent().getSerializableExtra("User");
                userCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        //bookDataList.clear();
                        for(QueryDocumentSnapshot d : value){
                            final String username = (String) d.getId();
                            final CollectionReference foruser = db.collection("Users/" + username + "/MyBooks");
                            foruser.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value2, @Nullable FirebaseFirestoreException error) {
                                    for(QueryDocumentSnapshot f : value2){
                                        System.out.println(bookISBN + "   " + /*f.getData().get("Book ISBN") +*/ " end\n");
                                        if((bookISBN.equals((String) f.getData().get("Book ISBN")))){
                                            if(currentUser.getUsername().equals((String) f.getData().get("Borrower"))){
                                                HashMap<String,Object> map = new HashMap<>();
                                                map.put("Borrower",null);
                                                map.put("Book Status","Available");
                                                foruser
                                                        .document(f.getId())
                                                        .update(map)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("Return","Book values successfully updated!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d("Return","Book values failed to update!");
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
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
            }
        }
    }
}