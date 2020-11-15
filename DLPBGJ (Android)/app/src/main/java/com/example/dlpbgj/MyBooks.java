package com.example.dlpbgj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//When the user clicks MyBooks button from HomePage, this activity gets invoked
public class MyBooks extends AppCompatActivity implements AddBookFragment.OnFragmentInteractionListener{
    ListView bookList;
    ArrayAdapter<Book> bookAdapter; //A custom adapter
    ArrayList<Book> bookDataList;   //List of all the books user owns
    ArrayAdapter<Book> filteredBookAdapter; //A custom adapter
    ArrayList<Book> filteredDataList;
    FirebaseFirestore db;
    private User currentUser;
    CollectionReference userBookCollectionReference;    //This is the sub-collection reference for the user who's logged in pointing to the collection of owned books
    CollectionReference arrayReference;
    String TAG = "Sample";
    CheckBox checkAvail;
    CheckBox checkBorr;
    String availableConstraint = "available";
    String borrowedConstraint = "borrowed";
    int checkCount = 0;
    boolean aUncheck = false;
    boolean bUncheck = false;
    public static Context contextOfApplication;

    /**
     * onCreate Called when MyBooks activity is launched.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);
        bookList=findViewById(R.id.book_list);
        contextOfApplication = getApplicationContext();
        Intent intent = getIntent();
        currentUser = (User)getIntent().getSerializableExtra(HomePage.EXTRA_MESSAGE2);  //Catching the object of current user who's logged in

        bookDataList=new ArrayList<Book>();
        bookAdapter=new customBookAdapter(this,bookDataList);   //Implementing a custom adapter that connects the ListView with the ArrayList using bookcontent.xml layout
        bookList.setAdapter(bookAdapter);

        filteredDataList = new ArrayList<Book>();
        filteredBookAdapter = new customBookAdapter(this,filteredDataList);


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
                    String book_description = (String) doc.getData().get("Book Description");
                    String book_owner = (String) doc.getData().get("Owner");

                    bookDataList.add(new Book(book_title,book_author,book_ISBN,book_status,book_description,book_owner)); // Adding the cities and provinces from FireStore
                }
                bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud

            }

        });

        checkAvail = findViewById(R.id.checkAvailable);
        checkBorr = findViewById(R.id.checkBorrowed);
            //Code Added to update results depending on whether user wants to see only available or all books
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

        bookList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = bookDataList.get(i);
                Intent intent = new Intent(view.getContext(),ViewBookDetails.class);
                intent.putExtra("Book",book);
                startActivity(intent);
                return false;
            }
        });

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book temp = bookDataList.get(i);
                AddBookFragment fragment = AddBookFragment.newInstance(temp,currentUser);
                fragment.show(getSupportFragmentManager(),"ADD_BOOK");
            }
        });

    }

    /**
     * When user wants to add a new book to their list of books. No return value.
     * @param newBook
     */

    @Override
    public void onOkPressed(final Book newBook) { //Whenever the user adds a book, this method is called where the added book is sent as a parameter from the fragment

        final HashMap<String, String> data = new HashMap<>();
        final String bookTitle=newBook.getTitle();    //Title of the book will be the ID of the document representing the book inside the sub-collections of MyBooks
        final String bookAuthor=newBook.getAuthor();
        String bookISBN=newBook.getISBN();
        String bookStatus=newBook.getStatus();
        String bookDescription = newBook.getDescription();
        String bookOwner = currentUser.getUsername();
        if (bookTitle.length()>0 && bookAuthor.length()>0 && bookISBN.length()>0 && bookStatus.length()>0) {//Data inside the document will consist of the following
            //Adding data inside the hash map
            data.put("Book Author", bookAuthor);
            data.put("Book ISBN", bookISBN);
            data.put("Book Status",bookStatus);
            data.put("Book Description",bookDescription);
            data.put("Owner",bookOwner);
        }
        arrayReference = db.collection("GlobalArray");
        DocumentReference docRef = arrayReference.document("Array"); //If username does not exist then prompt for a sign-up
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        Map<String,Object> temp = document.getData();
                        ArrayList<String> name = (ArrayList<String>) temp.get("Array");
                        data.put("Uid",Integer.toString(name.size()+1));
                        name.add(Integer.toString(name.size()+1));
                        HashMap<String,Object> array = new HashMap<>();
                        array.put("Array",name);
                        arrayReference
                                .document("Array")
                                .update(array)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG,"Array Size successfully updated");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG,"Failed to update Array Size");
                                    }
                                });
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
                }
                else{
                    Log.d("Param","get failed with ",task.getException());
                }
            }
        });


    }

    /**
     *This function is used for the fragment that gives the user the option to edit a book or delete it.
     * @param newBook
     * @param oldBookName
     */
    @Override
    public void onOkPressed(final Book newBook, final String oldBookName){
        final HashMap<String, Object> data = new HashMap<>();
        data.put("Book Author", newBook.getAuthor());
        data.put("Book ISBN", newBook.getISBN());
        data.put("Book Status",newBook.getStatus());
        data.put("Book Description",newBook.getDescription());
        data.put("Owner",newBook.getOwner());
        data.put("Uid",newBook.getUid());

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

    /**
     * When a user wants to delete the selected book. This function will delete it from the database as well.
     * @param book
     */

    @Override
    public void onDeletePressed(Book book){
        userBookCollectionReference
                .document(book.getTitle())
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
        bookDataList.remove(book);
        bookAdapter.notifyDataSetChanged();
    }

    @Override
    public void onOkPressed(){
        //Do nothing
    }

    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }


}



