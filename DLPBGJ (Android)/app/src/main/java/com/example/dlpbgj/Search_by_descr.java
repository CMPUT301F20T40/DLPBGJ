package com.example.dlpbgj;

import android.app.AppComponentFactory;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    FirebaseFirestore db;
    EditText description;
    Button search;

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
                                                System.out.println("Reached If like a boss!!");
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

        /*bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book temp = bookDataList.get(i);
                AddBookFragment fragment = AddBookFragment.newInstance(temp, new User("param", "hooda"));
                fragment.show(getSupportFragmentManager(),"ADD_BOOK");
            }
        });*/
    }

}
