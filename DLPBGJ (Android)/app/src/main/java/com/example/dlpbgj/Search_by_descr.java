package com.example.dlpbgj;

import android.app.AppComponentFactory;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;

import android.view.LayoutInflater;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class Search_by_descr extends AppCompatActivity implements RequestBookFragment.OnFragmentInteractionListener {
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    ArrayList<Book> filteredDataList;
    ArrayAdapter<Book> filteredBookAdapter;
    FirebaseFirestore db;
    EditText description;
    Button search;
    CheckBox checkAvail;
    CheckBox checkBorr;
    String availableConstraint = "available";
    String borrowedConstraint = "borrowed";
    CollectionReference userBookCollectionReference;
    private User currentUser;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_books);
        bookList = findViewById(R.id.book_list);
        description = findViewById(R.id.description);
        search = findViewById(R.id.search);
        checkAvail = findViewById(R.id.check_available);
        checkBorr = findViewById(R.id.check_borrowed);

        bookDataList = new ArrayList<Book>();
        filteredDataList = new ArrayList<Book>();
        bookAdapter = new customBookAdapter(this,bookDataList);
        filteredBookAdapter = new customBookAdapter(this,filteredDataList);



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
                                                System.out.println("Reached If like a boss!!");
                                                String book_title = f.getId();
                                                String book_author = (String) f.getData().get("Book Author");
                                                String book_ISBN = (String) f.getData().get("Book ISBN");
                                                String book_status = (String) f.getData().get("Book Status");
                                                ArrayList<String> req = (ArrayList)f.getData().get("Request List");
                                                Book thisBook = new Book(book_title, book_author, book_ISBN, book_status,book_description, username,req);
                                                bookDataList.add(thisBook);
                                                if(checkAvail.isChecked()&&checkBorr.isChecked()){
                                                    if(!(book_status.toLowerCase().equals(availableConstraint)||book_status.toLowerCase().equals(borrowedConstraint))){
                                                        bookDataList.remove(thisBook);}}
                                                if(checkBorr.isChecked()&&!checkAvail.isChecked())
                                                {
                                                    if(!(book_status.toLowerCase().equals(borrowedConstraint))){
                                                        bookDataList.remove(thisBook);
                                                    }
                                                }
                                                if(!checkBorr.isChecked()&&checkAvail.isChecked())
                                                {
                                                    if(!(book_status.toLowerCase().equals(availableConstraint))){
                                                        bookDataList.remove(thisBook);
                                                    }
                                                }

                                                bookAdapter.notifyDataSetChanged();
                                                bookList.setAdapter(bookAdapter);
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

        currentUser = (User) getIntent().getSerializableExtra("User");
        System.out.println(currentUser.getUsername());
        checkAvail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filteredDataList.clear();
                if (isChecked) {
                    for (int i = 0; i < bookDataList.size(); i++) {
                        Book book = bookDataList.get(i);
                        if (checkBorr.isChecked()){
                            if(book.getStatus().toLowerCase().equals(availableConstraint)||book.getStatus().toLowerCase().equals(borrowedConstraint))
                                filteredDataList.add(book);
                        }
                        else{
                            if(book.getStatus().toLowerCase().equals(availableConstraint))
                                filteredDataList.add(book);
                        }


                    }
                    filteredBookAdapter.notifyDataSetChanged();
                    bookList.setAdapter(filteredBookAdapter);

                }
                    else{
                        if(!checkBorr.isChecked())
                            bookList.setAdapter(bookAdapter);
                        else{
                    for (int i = 0; i < bookDataList.size(); i++) {
                        Book book = bookDataList.get(i);
                        filteredDataList.add(book);
                        if (!(book.getStatus().toLowerCase().equals(borrowedConstraint))) {
                            filteredDataList.remove(book);
                        }

                    }
                        filteredBookAdapter.notifyDataSetChanged();
                        bookList.setAdapter(filteredBookAdapter);

                    }

            }}
        });

        checkBorr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filteredDataList.clear();
                if (isChecked) {
                    for (int i = 0; i < bookDataList.size(); i++) {
                        Book book = bookDataList.get(i);
                        if (checkAvail.isChecked()){
                            if(book.getStatus().toLowerCase().equals(availableConstraint)||book.getStatus().toLowerCase().equals(borrowedConstraint))
                                filteredDataList.add(book);
                        }
                        else{
                            if(book.getStatus().toLowerCase().equals(borrowedConstraint))
                                filteredDataList.add(book);
                        }




                    }
                    filteredBookAdapter.notifyDataSetChanged();
                    bookList.setAdapter(filteredBookAdapter);

                }
                else{
                    if(!checkAvail.isChecked())
                        bookList.setAdapter(bookAdapter);
                    else{
                        for (int i = 0; i < bookDataList.size(); i++) {
                            Book book = bookDataList.get(i);
                            filteredDataList.add(book);
                            if (!(book.getStatus().toLowerCase().equals(availableConstraint))) {
                                filteredDataList.remove(book);
                            }

                        }
                        filteredBookAdapter.notifyDataSetChanged();
                        bookList.setAdapter(filteredBookAdapter);

                    }

                }}
        });


        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book temp = bookDataList.get(i);
                RequestBookFragment fragment = RequestBookFragment.newInstance(temp, currentUser);
                fragment.show(getSupportFragmentManager(),"REQUEST_BOOK");
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
        book.addRequest(currentUser.getUsername());
        data.put("Requests", book.getRequests());
        userBookCollectionReference = db.collection(book.getOwner());

        DocumentReference docRef = userBookCollectionReference.document(book.getTitle());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    userBookCollectionReference
                            .document(book.getTitle())
                            .update(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","Data has been successfully updated");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG","Failed to Update");
                                }
                            });
                }
            }
        });
        bookAdapter.notifyDataSetChanged();
        /*docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
            }
                }
            }
        });
        bookAdapter.notifyDataSetChanged();*/
    }
}

