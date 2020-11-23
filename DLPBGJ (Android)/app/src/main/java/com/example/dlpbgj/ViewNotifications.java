package com.example.dlpbgj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    ListView readNotificationList;
    private User currentUser;
    ArrayList<String> notifications;
    ArrayList<String> readNotifications;
    ArrayAdapter<String> notifAdapter;
    ArrayAdapter<String> readNotifAdapter;
    FirebaseFirestore db;
    CollectionReference userBookCollectionReference;
    Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notifications);
        notificationList = findViewById(R.id.notification_listview);
        readNotificationList = findViewById(R.id.readNotification_listview);
        currentUser = (User) getIntent().getSerializableExtra("User");
        notifications = new ArrayList<>();
        readNotifications = new ArrayList<>();
        notifAdapter = new CustomNotificationAdapter(this, notifications);   //Implementing a custom adapter that connects the ListView with the ArrayList using bookcontent.xml layout
        readNotifAdapter = new CustomNotificationAdapter(this,readNotifications);
        notificationList.setAdapter(notifAdapter);
        readNotificationList.setAdapter(readNotifAdapter);
        db = FirebaseFirestore.getInstance();
        userBookCollectionReference = db.collection("Users/" + currentUser.getUsername() + "/MyBooks");//Creating/pointing to a sub-collection of the books that user owns
        userBookCollectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Log.d("sucesss", doc.getId() + " => " + doc.getData());

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
                                            readNotifications.add(key+" requested " + doc.getId());
                                        else if (temp == 0) {
                                            notifications.add(key + " requested " + doc.getId());
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

                            } //For loop ends
                            System.out.println(notifications.size());
                            notifAdapter.notifyDataSetChanged();
                            readNotifAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("fail", "Error getting documents: ", task.getException());
                        }
                    }
                });

        notificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), BookRequests.class);
                intent.putExtra("User", currentUser);
                startActivity(intent);
            }
        });


    }
}