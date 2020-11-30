package com.example.dlpbgj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

public class BookRequests extends AppCompatActivity implements BookRequestsFragment.OnFragmentInteractionListener {
    ListView reqList;
    ArrayAdapter<Book> bookAdapter; //A custom adapter
    ArrayList<Book> bookDataList;   //List of all the books user owns
    FirebaseFirestore db;
    CollectionReference userBookCollectionReference;    //This is the sub-collection reference for the user who's logged in pointing to the collection of owned books
    String TAG = "BookRequests";
    private User currentUser;
    String Location = "";
    Book book;
    Statuses status = new Statuses();
    DatabaseAccess access = new DatabaseAccess();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_requests);
        reqList = findViewById(R.id.req_list);
        currentUser = (User) getIntent().getSerializableExtra("User");
        bookDataList = new ArrayList<>();
        bookAdapter = new customBookAdapter(this, bookDataList);   //Implementing a custom adapter that connects the ListView with the ArrayList using bookcontent.xml layout
        reqList.setAdapter(bookAdapter);
        Button handover = findViewById(R.id.handover);
        Button returnBook = findViewById(R.id.returnBook);

        db = FirebaseFirestore.getInstance();
        userBookCollectionReference = db.collection("Users/" + currentUser.getUsername() + "/MyBooks");//Creating/pointing to a sub-collection of the books that user owns
        userBookCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) { //Manages the state of the sub-collection

                bookDataList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    HashMap<String,String> req = (HashMap<String, String>) doc.getData().get("Requests");
                    if (req != null && !req.containsValue(status.getBorrowed())) {
                        Log.d(TAG, String.valueOf(doc.getData().get(access.getRequests())));
                        String book_title = doc.getId();
                        String book_author = (String) doc.getData().get(access.getAuthor());
                        String book_ISBN = (String) doc.getData().get(access.getISBN());
                        String book_status = (String) doc.getData().get(access.getStatus());
                        String book_description = (String) doc.getData().get(access.getDescription());
                        String book_owner = (String) doc.getData().get(access.getOwner());
                        bookDataList.add(new Book(book_title, book_author, book_ISBN, book_status, book_description, book_owner, req));
                    }
                }
                bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });

        reqList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book temp = bookDataList.get(i);
                BookRequestsFragment fragment = BookRequestsFragment.newInstance(temp);
                fragment.show(getSupportFragmentManager(), "REQ_BOOK");
            }
        });

        handover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HandBook.class);
                intent.putExtra("User", currentUser);
                startActivity(intent);
            }
        });

        returnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AcceptBook.class);
                intent.putExtra("User", currentUser);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onAcceptPressed(Book newBook) {
        book = newBook;
        Toast toast = Toast.makeText(getApplicationContext(), "Please Select a Pickup Location!", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(getApplicationContext(), UserLocation.class);
        intent.putExtra("Flag","None");
        startActivityForResult(intent,1);
    }

    @Override
    public void onDeclinePressed(Book book) {
        db = FirebaseFirestore.getInstance();
        userBookCollectionReference = db.collection("Users/" + currentUser.getUsername() + "/MyBooks");
        HashMap<String, Object> map = new HashMap<>();
        map.put(access.getRequests(), book.getRequests());
        userBookCollectionReference
                .document(book.getTitle())
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Book data successfully updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Book Data failed to update");
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == -1) {
                Location = data.getStringExtra("Location");
                db = FirebaseFirestore.getInstance();
                userBookCollectionReference = db.collection("Users/" + currentUser.getUsername() + "/MyBooks");
                HashMap<String, Object> map = new HashMap<>();
                //map.put("Borrower", book.getBorrower());
                book.setStatus(status.getAccepted());
                map.put(access.getRequests(), book.getRequests());
                map.put(access.getStatus(), book.getStatus());
                map.put(access.getLocation(),Location);
                book.setLocation(Location);
                userBookCollectionReference
                        .document(book.getTitle())
                        .update(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Book data successfully updated");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Book Data failed to update");
                            }
                        });
            }
        }
    }

}