package com.example.dlpbgj;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class View_Requests extends AppCompatActivity {
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    private User currentUser;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requested_books);
        bookList = findViewById(R.id.book_list);
        bookDataList = new ArrayList<>();
        bookAdapter = new customBookAdapter(this, bookDataList);
        bookList.setAdapter(bookAdapter);
        final FirebaseFirestore Db = FirebaseFirestore.getInstance();
        final CollectionReference cr = Db.collection("Users");
        currentUser = (User) getIntent().getSerializableExtra("User");
        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot d : value) {
                    final String username = d.getId();
                    CollectionReference foruser = Db.collection("Users/" + username + "/MyBooks");
                    foruser.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value2, @Nullable FirebaseFirestoreException error) {
                            for (QueryDocumentSnapshot newBook : value2) {
                                ArrayList<String> book_requests = (ArrayList<String>) newBook.getData().get("Requests");
                                if (book_requests != null) {
                                    if (book_requests.contains(currentUser.getUsername())) {
                                        String book_title = newBook.getId();
                                        String book_author = (String) newBook.getData().get("Book Author");
                                        String book_ISBN = (String) newBook.getData().get("Book ISBN");
                                        String book_status = (String) newBook.getData().get("Book Status");
                                        String book_description = (String) newBook.getData().get("Book Description");
                                        Book thisBook = new Book(book_title, book_author, book_ISBN, book_status, book_description, username, book_requests);
                                        bookDataList.add(thisBook);
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
}