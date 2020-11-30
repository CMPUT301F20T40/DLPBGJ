package com.example.dlpbgj;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;

public class customBookAdapter extends ArrayAdapter<Book> {

    private final ArrayList<Book> books;
    private final Context context;
    FirebaseStorage storage;
    StorageReference storageReference;

    public customBookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.books = books;
        this.context = context;
    }


    /**
     * Function to use our custom array adapter to show the books of a user.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.bookcontent, parent, false); //Attaches layout from bookcontent to each item inside the ListView
        }

        Book book = books.get(position);
        ImageView image = view.findViewById(R.id.textView1);
        TextView bookTitle = view.findViewById(R.id.textView2);
        TextView bookStatus = view.findViewById(R.id.textView3);
        TextView bookOwner = view.findViewById(R.id.textView4);

        bookTitle.setText("Title: " + book.getTitle());
        bookStatus.setText("Status: " + book.getStatus()); //Setting the values of each textView inside the view in ListView
        bookOwner.setText("Owner: " + book.getOwner());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        System.out.println(book.getUid());
        StorageReference imagesRef = storageReference.child("images/" + book.getUid());
        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Glide
                        .with(context)
                        .load(downloadUrl.toString())
                        .centerCrop()
                        .into(image);
            }

        });
        return view;

    }
}
