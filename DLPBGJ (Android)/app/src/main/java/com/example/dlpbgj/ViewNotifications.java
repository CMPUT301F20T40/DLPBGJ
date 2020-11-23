package com.example.dlpbgj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
import java.util.Set;

public class ViewNotifications extends AppCompatActivity {
    ListView notificationList;
    ArrayList<Book> bookDataList;
    private User currentUser;
    ArrayList<String> notifications;
    ArrayAdapter<String> notifAdapter;
    FirebaseFirestore db;
    CollectionReference userBookCollectionReference;
    Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notifications);
        notificationList = findViewById(R.id.notification_listview);
        currentUser = (User) getIntent().getSerializableExtra("User");
        notifications = new ArrayList<>();
        notifAdapter = new CustomNotificationAdapter(this, notifications);   //Implementing a custom adapter that connects the ListView with the ArrayList using bookcontent.xml layout
        notificationList.setAdapter(notifAdapter);
        db = FirebaseFirestore.getInstance();
        userBookCollectionReference = db.collection("Users/" + currentUser.getUsername() + "/MyBooks");//Creating/pointing to a sub-collection of the books that user owns
        userBookCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) { //Manages the state of the sub-collection
                //notifications.clear();

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    //ArrayList<String> req = (ArrayList<String>) doc.getData().get("Requests");
                    System.out.println("Book name : " + doc.getId());
                    HashMap<String,Long> allNotifications = (HashMap<String,Long>) doc.getData().get("Notifications");
                    if(allNotifications!=null) {
                        for (String key : allNotifications.keySet()) {
                            System.out.println("Current User is : "+currentUser.getUsername());
                            System.out.println("Borrower : " + key + " Book name = " + doc.getId());
                            long zero = 0;
                            long one = 1;
                            long temp = allNotifications.get(key);
                            System.out.println("@Book Seen : " + temp);
                            if(temp==1)
                                break;
                            if (zero == temp) {
                                notifications.add(key + " requested " + doc.getId());

                                notificationList.setAdapter(notifAdapter);
                                notifAdapter.notifyDataSetChanged();
                                allNotifications.put(key,one);
                                HashMap<String,Object> data = new HashMap<>();
                                data.put("Notifications",allNotifications);
                                userBookCollectionReference
                                        .document(doc.getId())
                                        .update(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("ViewNotif","Notifications updated");
                                                System.out.println("Database Updated");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("ViewNotif","Failed to update Notifications");
                                            }
                                        });
                                System.out.println("Book Added*********************");
                            }
                        }
                    }
                }
                System.out.println(notifications.size());

                //bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }

        });


    }
}