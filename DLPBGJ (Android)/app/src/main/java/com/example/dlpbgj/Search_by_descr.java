package com.example.dlpbgj;

import android.app.AppComponentFactory;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class Search_by_descr extends AppCompatActivity implements RequestBookFragment.OnFragmentInteractionListener{
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    FirebaseFirestore db;
    EditText description;
    Button search;
    CollectionReference userBookCollectionReference;
    String TAG = "Sample";
    private User currentUser;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_books);
        bookList = findViewById(R.id.book_list);
        description = findViewById(R.id.description);
        search = findViewById(R.id.search);

        bookDataList = new ArrayList<Book>();
        bookAdapter = new customBookAdapter(this,bookDataList);
        bookList.setAdapter(bookAdapter);

        db = FirebaseFirestore.getInstance();
        final FirebaseFirestore Db = FirebaseFirestore.getInstance();
        final CollectionReference cr = Db.collection("Users");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        bookDataList.clear();
                        final String descinput = description.getText().toString();
                        for(QueryDocumentSnapshot d : value){
                            final String username = (String) d.getId();
                            CollectionReference foruser = Db.collection("Users/" + username + "/MyBooks");
                            foruser.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value2, @Nullable FirebaseFirestoreException error) {
                                    for(QueryDocumentSnapshot f : value2){
                                        String book_description = (String) f.getData().get("Book Description");
                                        if(book_description != null){
                                            if(book_description.contains(descinput)){
                                                String book_title = f.getId();
                                                String book_author = (String) f.getData().get("Book Author");
                                                String book_ISBN = (String) f.getData().get("Book ISBN");
                                                String book_status = (String) f.getData().get("Book Status");
                                                bookDataList.add(new Book(book_title, book_author, book_ISBN, book_status, username));
                                                bookAdapter.notifyDataSetChanged();
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
        Intent intent = getIntent();
        currentUser = (User)getIntent().getSerializableExtra(HomePage.EXTRA_MESSAGE2);
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book temp = bookDataList.get(i);
                System.out.println("Reached fragment creator");
                RequestBookFragment r = RequestBookFragment.newInstance(temp, currentUser);
                r.show(getSupportFragmentManager(), "REQUEST_BOOK");
            }
        });
    }

    @Override
    public void onOkPressed(final Book book, User user) {
        final HashMap<String, Object> data = new HashMap<>();
        data.put("Book Author", book.getAuthor());
        data.put("Book ISBN", book.getISBN());
        data.put("Book Status",book.getStatus());
        data.put("Book Description",book.getDescription());
        data.put("Owner",book.getOwner());
        data.put("Requests", book.getRequests().addRequest(user.getUsername()));
        userBookCollectionReference = db.collection(book.getOwner());

        DocumentReference docRef = userBookCollectionReference.document(book.getTitle());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    //if (document.exists()){
                    userBookCollectionReference
                            .document(book.getTitle())
                            .update(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Data has been updated successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Data could not be updated!" + e.toString());
                                }
                            });
                    //}
                    /*else {
                        userBookCollectionReference
                                .document(oldBookName)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "user book data has been deleted");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG,"Failed to delete the user book data");
                                    }
                                });
                        bookDataList.remove(newBook);
                        userBookCollectionReference
                                .document(newBook.getTitle())
                                .set(data)
                                //Debugging methods
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // These are a method which gets executed when the task is succeeded
                                        Log.d(TAG, "Data has been added successfully!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // These are a method which gets executed if there’s any problem
                                        Log.d(TAG, "Data could not be added!" + e.toString());
                                    }
                                });
                    }*/
                }
            }
        });
        bookAdapter.notifyDataSetChanged();
    }
}
