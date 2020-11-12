package com.example.dlpbgj;

import android.app.AppComponentFactory;
import android.app.Dialog;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Search_by_descr extends AppCompatActivity {
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
                                                Book thisBook = new Book(book_title, book_author, book_ISBN, book_status, username);
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

    }}

