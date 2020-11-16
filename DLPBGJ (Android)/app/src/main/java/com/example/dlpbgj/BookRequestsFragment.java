package com.example.dlpbgj;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;
import java.util.ArrayList;

public class BookRequestsFragment extends DialogFragment implements Serializable {
    private BookRequestsFragment.OnFragmentInteractionListener listener;
    private Book book;
    ArrayList<String> req;
    private String selection;

    public interface OnFragmentInteractionListener {
        void onAcceptPressed(Book book);
        void onDeclinePressed(Book book);
    }

    static BookRequestsFragment newInstance(Book book){
        Bundle args = new Bundle();
        args.putSerializable("Book", book);
        BookRequestsFragment fragment = new BookRequestsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookRequestsFragment.OnFragmentInteractionListener){
            listener = (BookRequestsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.book_requests_fragment, null);
        final Spinner spinner = view.findViewById(R.id.dropDown);
        final ImageView profile = view.findViewById(R.id.ProfilePic);
        String title = "Accept / Decline Requests";
        if (getArguments() != null){
            book = (Book) getArguments().get("Book");
            req = book.getRequests();
            System.out.println("Look Here");
            System.out.println(req.get(0));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_spinner_item, req);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selection = req.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do Nothing
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(title)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        book.setBorrower(selection);
                        Toast toast = Toast.makeText(getContext(), selection+"'s request accepted!", Toast.LENGTH_SHORT);
                        toast.show();
                        listener.onAcceptPressed(book);
                    }
                })
                .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        book.removeRequest(selection);
                        Toast toast = Toast.makeText(getContext(), selection+"'s request declined!", Toast.LENGTH_SHORT);
                        toast.show();
                        listener.onDeclinePressed(book);
                    }
                })
                .setNeutralButton("Cancel",null)
                .create();
    }
    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#B59C34"));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#B59C34"));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#B59C34"));
    }


}