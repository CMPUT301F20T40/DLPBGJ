package com.example.dlpbgj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class customBookAdapter extends ArrayAdapter<Book> {

    private final ArrayList<Book> books;
    private final Context context;

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

        TextView bookTitle = view.findViewById(R.id.textView1);
        TextView bookAuthor = view.findViewById(R.id.textView2);
        TextView bookISBN = view.findViewById(R.id.textView3);
        TextView bookStatus = view.findViewById(R.id.textView4);
        TextView bookOwner = view.findViewById(R.id.textView5);

        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookISBN.setText(book.getISBN());
        bookStatus.setText(book.getStatus()); //Setting the values of each textView inside the view in ListView
        bookOwner.setText(book.getOwner());

        return view;

    }
}
