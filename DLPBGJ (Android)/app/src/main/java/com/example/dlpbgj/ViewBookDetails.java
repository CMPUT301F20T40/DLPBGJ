package com.example.dlpbgj;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewBookDetails extends AppCompatActivity {
    Button backButton;
    TextView title;
    TextView author;
    TextView isbn;
    TextView status;


    /**
     * Created when the user long clicks on a book to view its details
     * This loads up a new activity that shows the details of the selected book.
     *
     * @param savedInstanceState
     */
    TextView description;
    TextView owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book_details);
        Book book = (Book) getIntent().getSerializableExtra("Book");
        title = findViewById(R.id.Title);
        author = findViewById(R.id.Author);
        isbn = findViewById(R.id.ISBN);
        status = findViewById(R.id.Status);
        backButton = findViewById(R.id.Back);
        description = findViewById(R.id.Description);
        owner = findViewById(R.id.Owner);
        title.setText("Book Title :\n" + book.getTitle());
        author.setText("Book Author :\n" + book.getAuthor());
        isbn.setText("Book ISBN :\n" + book.getISBN());
        status.setText("Book Status :\n" + book.getStatus());
        description.setText("Book Description :\n" + book.getDescription());
        owner.setText("Current Owner : " + book.getOwner());
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}