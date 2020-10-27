package com.example.dlpbgj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class customBookAdapter extends ArrayAdapter<Book> {

    private ArrayList<Book> books;
    private Context context;

    public customBookAdapter(Context context, ArrayList<Book> books){
        super(context,0, books);
        this.books = books;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.bookcontent, parent,false); //Attaches layout from bookcontent to each item inside the ListView
        }

        Book book = books.get(position);

        TextView bookTitle = view.findViewById(R.id.textView1);
        TextView bookAuthor = view.findViewById(R.id.textView2);
        TextView bookISBN = view.findViewById(R.id.textView3);
        TextView bookStatus = view.findViewById(R.id.textView4);

        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookISBN.setText(book.getISBN());
        bookStatus.setText(book.getStatus()); //Setting the values of each textView inside the view in ListView

        return view;

    }
}
