package com.example.dlpbgj;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class AddBookFragment extends DialogFragment implements Serializable  {
    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookISBN;
    private EditText bookStatus;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Book newBook);
        void onOkPressed(Book book,String oldBookName);
        void onDeletePressed(Book book);
        void onOkPressed();
    }

    static AddBookFragment newInstance(Book book){
        Bundle args = new Bundle();
        args.putSerializable("Book",book);
        AddBookFragment fragment = new AddBookFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_book_fragment_layout, null);
       // final String barcode = (String) getActivity().getIntent().getSerializableExtra(barcode_scanner.varText);

        bookTitle = view.findViewById(R.id.book_title_editText);
        bookAuthor = view.findViewById(R.id.book_author_editText);
        bookISBN = view.findViewById(R.id.book_ISBN_editText);
        bookStatus = view.findViewById(R.id.book_status_editText);
        Button scan = view.findViewById(R.id.scan2);
        String title = "Add Book";
        if (getArguments() != null){
            Book book = (Book) getArguments().get("Book");
            title = "Edit Book";
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());
            bookISBN.setText(book.getISBN());
            bookStatus.setText(book.getStatus());
        }
        /**
         * When scan button is clicked
         * Starts new activity for scanning the barcode
         */
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),barcode_scanner.class);
                startActivityForResult(intent,1);

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (getArguments()!=null){
                            Book book = (Book) getArguments().get("Book");
                            listener.onDeletePressed(book);
                        }
                        else{
                            listener.onOkPressed();
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String book_title = bookTitle.getText().toString();
                        String book_author = bookAuthor.getText().toString();
                        String book_ISBN = bookISBN.getText().toString();
                        String book_status = bookStatus.getText().toString();
                        if (bookTitle.getText().toString().equals("")||bookAuthor.getText().toString().equals("")||bookISBN.getText().toString().equals("")||bookStatus.getText().toString().equals("")){
                                listener.onOkPressed();
                        }
                        else if (getArguments()!=null){
                            Book book = (Book)  getArguments().get("Book");
                            String temp = book.getTitle();
                            book.setAuthor(book_author);
                            book.setISBN(book_ISBN);
                            book.setStatus(book_status);
                            book.setTitle(book_title);
                            listener.onOkPressed(book,temp);
                        }
                        else {
                            listener.onOkPressed(new Book(book_title, book_author, book_ISBN, book_status)); //Send the inputted book as a parameter to the main function's implementation of this method
                        }
                    }}).create();
    }

    /**
     * gets the result from barcode_scanner class
     * Sets the desired result inside the fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode == -1){
                String code = data.getStringExtra("ISBN");
                bookISBN.setText(code);
            }
        }
    }
}
