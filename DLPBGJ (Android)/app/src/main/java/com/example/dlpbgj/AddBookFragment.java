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
import java.util.ArrayList;

public class AddBookFragment extends DialogFragment implements Serializable  {
    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookISBN;
    private EditText bookStatus;
    private EditText bookDescription;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Book newBook);
        void onOkPressed(Book book,String oldBookName);
        void onDeletePressed(Book book);
        void onOkPressed();
    }

    static AddBookFragment newInstance(Book book,User user){
        Bundle args = new Bundle();
        args.putSerializable("Book",book);
        args.putSerializable("User",user);
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
        bookDescription = view.findViewById(R.id.book_description_editText);
        final ArrayList<String> validStatus = new ArrayList<String>();
        validStatus.add("Available");
        validStatus.add("Borrowed");
        validStatus.add("Accepted");
        validStatus.add("Requested");

        Button scan = view.findViewById(R.id.scan2);
        String title = "Add Book";
        if (getArguments() != null) {
            Book book = (Book) getArguments().get("Book");
            title = "Edit Book";
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());
            bookISBN.setText(book.getISBN());
            bookStatus.setText(book.getStatus());
            bookDescription.setText(book.getDescription());

        }
        /**
         * When scan button is clicked
         * Starts new activity for scanning the barcode
         */
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), barcode_scanner.class);
                startActivityForResult(intent, 1);

            }
        });

        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle(title)
                .setNegativeButton("Cancel", null)
                .setNeutralButton("delete", null)
                .setPositiveButton(android.R.string.ok, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button bOk = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button bCancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                Button bDel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);

                bDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getArguments() != null) {
                            Book book = (Book) getArguments().get("Book");
                            listener.onDeletePressed(book);
                        } else {
                            listener.onOkPressed();
                        }
                        dialog.dismiss();

                    }
                });

                bOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String book_title = bookTitle.getText().toString();
                        String book_author = bookAuthor.getText().toString();
                        String book_ISBN = bookISBN.getText().toString();
                        String book_status = bookStatus.getText().toString();
                        String book_description = bookDescription.getText().toString();
                        View focus = null;
                        boolean wrong_input = false;
                        System.out.println(book_title);
                        if (bookTitle.getText().toString().equals("")) { //Mandatory to enter book's title
                            bookTitle.setError("Please enter the book's title!");
                            System.out.println("Im here");
                            wrong_input = true;
                            focus = bookTitle;
                        }

                        if (book_status.equals("")) { //Mandatory to enter book's status
                            //System.out.println("!!!IF!!!");
                            bookStatus.setError("Please enter the book's status");
                            wrong_input = true;
                            focus = bookStatus;
                        }

                        if (book_description.equals("")) {    //Mandatory to enter book's description
                            bookDescription.setError("Please enter the book's description");
                            wrong_input = true;
                            focus = bookDescription;

                        }
                        if (!validStatus.contains(book_status)) { //Input validation for the status
                            bookStatus.setError("Please enter a valid status: Available, Borrowed, Requested, Accepted");
                            wrong_input = true;
                            focus = bookStatus;

                        }
                        if (book_author.equals("")) {
                            book_author = "Unknown";

                        }
                        if (book_ISBN.equals("")) {
                            book_ISBN = "Unknown";
                        }

                        if (wrong_input) {
                            focus.requestFocus();

                        } else if (getArguments() != null) {
                            System.out.println("ELSE IF");

                            Book book = (Book) getArguments().get("Book");
                            User user = (User) getArguments().get("User");

                            String temp = book.getTitle();
                            book.setAuthor(book_author);
                            book.setISBN(book_ISBN);
                            book.setStatus(book_status);
                            book.setTitle(book_title);
                            book.setDescription(book_description);
                            book.setOwner(user.getUsername());
                            listener.onOkPressed(book, temp);
                            dialog.dismiss();
                        } else {
                            System.out.println("ELSE");
                            listener.onOkPressed(new Book(book_title, book_author, book_ISBN, book_status, book_description)); //Send the inputted book as a parameter to the main function's implementation of this method
                            dialog.dismiss();
                        }

                    }
                });

            }
        });
        dialog.show();


    return dialog;
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





