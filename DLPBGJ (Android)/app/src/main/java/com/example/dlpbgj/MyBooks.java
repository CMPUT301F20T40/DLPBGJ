package com.example.dlpbgj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.List;

//When the user clicks MyBooks button from HomePage, this activity gets invoked
public class MyBooks extends AppCompatActivity implements AddBookFragment.OnFragmentInteractionListener{
    ListView bookList;
    ArrayAdapter<Book> bookAdapter; //A custom adapter
    ArrayList<Book> bookDataList;   //List of all the books user owns
    FirebaseFirestore db;
    private User currentUser;
    CollectionReference userBookCollectionReference;    //This is the sub-collection reference for the user who's logged in pointing to the collection of owned books
    String TAG = "Sample";
    CheckBox checkAvailable;
    CheckBox checkBorrowed;
    String availableConstraint = "a";
    String borrowedConstraint = "b";
    int checkCount = 0;
    boolean aUncheck = false;
    boolean bUncheck = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);
        bookList=findViewById(R.id.book_list);

        Intent intent = getIntent();
        currentUser = (User)getIntent().getSerializableExtra(HomePage.EXTRA_MESSAGE2);  //Catching the object of current user who's logged in

        bookDataList=new ArrayList<Book>();
        bookAdapter=new customBookAdapter(this,bookDataList);   //Implementing a custom adapter that connects the ListView with the ArrayList using bookcontent.xml layout
        bookList.setAdapter(bookAdapter);

        final FloatingActionButton addCityButton = findViewById(R.id.add_book_button);  //Invoking a fragment to add the books when the FAB is clicked
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddBookFragment().show(getSupportFragmentManager(), "ADD_BOOK");
            }
        });



        db = FirebaseFirestore.getInstance();
        userBookCollectionReference = db.collection("Users/" + currentUser.getUsername() + "/MyBooks" );//Creating/pointing to a sub-collection of the books that user owns
        userBookCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) { //Manages the state of the sub-collection

                bookDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get("Book Author")));
                    String book_title = doc.getId();
                    String book_author = (String) doc.getData().get("Book Author");
                    String book_ISBN = (String) doc.getData().get("Book ISBN");
                    String book_status = (String) doc.getData().get("Book Status");
                    bookDataList.add(new Book(book_title,book_author,book_ISBN,book_status)); // Adding the cities and provinces from FireStore
                }
                bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud

            }

        });

        checkAvailable = findViewById(R.id.checkAvailable);
        checkBorrowed = findViewById(R.id.checkBorrowed);
            //Code Added to update results depending on whether user wants to see only available or all books
        checkAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked ) {
                    userBookCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) { //Manages the state of the sub-collection
                            if(!checkBorrowed.isChecked())
                                bookDataList.clear();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                Log.d(TAG, String.valueOf(doc.getData().get("Book Author")));
                                String book_title = doc.getId();
                                String book_author = (String) doc.getData().get("Book Author");
                                String book_ISBN = (String) doc.getData().get("Book ISBN");
                                String book_status = (String) doc.getData().get("Book Status");
                                if (book_status.toLowerCase().equals(availableConstraint))
                                    bookDataList.add(new Book(book_title, book_author, book_ISBN, book_status));// Adding the cities and provinces from FireStore
                                //if (checkBorrowed.isChecked() && (book_status.toLowerCase().equals(borrowedConstraint)))
                                    //bookDataList.add(new Book(book_title, book_author, book_ISBN, book_status));
                            }
                            bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud

                        }
                    });
                } else {
                    userBookCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) { //Manages the state of the sub-collection
                            if(checkBorrowed.isChecked())
                                bookDataList.clear();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                Log.d(TAG, String.valueOf(doc.getData().get("Book Author")));
                                String book_title = doc.getId();
                                String book_author = (String) doc.getData().get("Book Author");
                                String book_ISBN = (String) doc.getData().get("Book ISBN");
                                String book_status = (String) doc.getData().get("Book Status");
                                //bookDataList.add(new Book(book_title, book_author, book_ISBN, book_status));// Adding the cities and provinces from FireStore
                                if (checkBorrowed.isChecked() && book_status.toLowerCase().equals(borrowedConstraint))
                                    bookDataList.add(new Book(book_title, book_author, book_ISBN, book_status));
                                else if (!checkBorrowed.isChecked())
                                    bookDataList.add(new Book(book_title, book_author, book_ISBN, book_status));
                            }
                            bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
                        }
                    });
                }
            }
        }
        );
        checkBorrowed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    userBookCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) { //Manages the state of the sub-collection
                            if(!checkAvailable.isChecked())
                                bookDataList.clear();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                Log.d(TAG, String.valueOf(doc.getData().get("Book Author")));
                                String book_title = doc.getId();
                                String book_author = (String) doc.getData().get("Book Author");
                                String book_ISBN = (String) doc.getData().get("Book ISBN");
                                String book_status = (String) doc.getData().get("Book Status");
                                if (book_status.toLowerCase().equals(borrowedConstraint))
                                    bookDataList.add(new Book(book_title, book_author, book_ISBN, book_status)); // Adding the cities and provinces from FireStore
                                //if (checkAvailable.isChecked() && (book_status.toLowerCase().equals(availableConstraint)))
                                  //  bookDataList.add(new Book(book_title, book_author, book_ISBN, book_status));
                            }
                            bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
                        }
                    });
                } else {
                    userBookCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) { //Manages the state of the sub-collection
                            if(checkAvailable.isChecked())
                                bookDataList.clear();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                Log.d(TAG, String.valueOf(doc.getData().get("Book Author")));
                                String book_title = doc.getId();
                                String book_author = (String) doc.getData().get("Book Author");
                                String book_ISBN = (String) doc.getData().get("Book ISBN");
                                String book_status = (String) doc.getData().get("Book Status");
                                //bookDataList.add(new Book(book_title, book_author, book_ISBN, book_status));// Adding the cities and provinces from FireStore
                                if (checkAvailable.isChecked() && book_status.toLowerCase().equals(availableConstraint))
                                    bookDataList.add(new Book(book_title, book_author, book_ISBN, book_status));
                                else if (!checkAvailable.isChecked())
                                    bookDataList.add(new Book(book_title, book_author, book_ISBN, book_status));
                            }
                            bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
                        }
                    });
                }
            }
        });
        /* just creating a function to remove the book from the data list if the reader decides to do so
        maybe later I will create an activity where the user can select multiple books at once to delete them all
        */
        bookList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book tempBook = bookDataList.get(position);
                userBookCollectionReference
                        .document(tempBook.getTitle())
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
                bookDataList.remove(position);
                bookAdapter.notifyDataSetChanged();
                return false;
            }
        });

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book temp = bookDataList.get(i);
                System.out.println(temp.getAuthor());
                AddBookFragment fragment = AddBookFragment.newInstance(temp);
                fragment.show(getSupportFragmentManager(),"ADD_BOOK");
            }
        });

    }



    @Override
    public void onOkPressed(Book newBook) { //Whenever the user adds a book, this method is called where the added book is sent as a parameter from the fragment

        HashMap<String, String> data = new HashMap<>();
        String bookTitle=newBook.getTitle();    //Title of the book will be the ID of the document representing the book inside the sub-collections of MyBooks
        final String bookAuthor=newBook.getAuthor();
        String bookISBN=newBook.getISBN();
        String bookStatus=newBook.getStatus();

        if (bookTitle.length()>0 && bookAuthor.length()>0 && bookISBN.length()>0 && bookStatus.length()>0) {//Data inside the document will consist of the following
            //Adding data inside the hash map
            data.put("Book Author", bookAuthor);
            data.put("Book ISBN", bookISBN);
            data.put("Book Status",bookStatus);
        }
        userBookCollectionReference
                .document(bookTitle)
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

    @Override
    public void onOkPressed(final Book newBook, final String oldBookName){
        final HashMap<String, Object> data = new HashMap<>();
        data.put("Book Author", newBook.getAuthor());
        data.put("Book ISBN", newBook.getISBN());
        data.put("Book Status",newBook.getStatus());
        DocumentReference docRef = userBookCollectionReference.document(newBook.getTitle());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        userBookCollectionReference
                                .document(newBook.getTitle())
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
                    }
                    else {
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
        bookAdapter.notifyDataSetChanged();
    }

    @Override
    public void onOkPressed(){
        //Do nothing
    }

}



