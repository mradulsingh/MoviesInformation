package com.android.example.github.ui.movie;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.example.github.R;

import java.util.Calendar;

public class MovieFilterFragment extends DialogFragment {

    EditText releaseDate, voteAverage, originalLanguage;
    View rootView;
    Bundle args = new Bundle();
    MovieListFragment movieListFragment;
    Button openCalenderButton;

    EditText txtDate;
    private int mYear, mMonth, mDay;

    public static MovieFilterFragment newInstance(MovieListFragment movieListFragment) {
        MovieFilterFragment dialog = new MovieFilterFragment();
        dialog.movieListFragment = movieListFragment;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        init();
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(rootView)
                .setTitle("MOVIES REPORT")
                .setCancelable(false)
                .setPositiveButton("apply", null)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.setOnShowListener(dialog -> {
            onFocusChangelistener();
            onDialogShow(alertDialog);
        });
        return alertDialog;
    }

    private void onFocusChangelistener() {
        releaseDate.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {

            }
        });

        /** clicking openGallery Button*/
        openCalenderButton.setOnClickListener(v -> {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("SetTextI18n")
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> releaseDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth), mYear, mMonth, mDay);
            datePickerDialog.show();
        });
    }

    /** initialize class and attributes */
    private void init() {
        rootView = LayoutInflater.from(getContext())
                .inflate(R.layout.activity_movie_filter_fragment, null, false);
        releaseDate = rootView.findViewById(R.id.editTextReleaseDate);
        voteAverage = rootView.findViewById(R.id.editTextVoteAverage);
        originalLanguage = rootView.findViewById(R.id.editTextLanguage);

        openCalenderButton = rootView.findViewById(R.id.openCalender);

    }

    /** when you click the apply button*/
    private void onDialogShow(AlertDialog alertDialog) {
        Button positiveButton = alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(v -> {
            onDoneClicked();
        });
    }

    /** checking validity of EditTexts after clicking Apply button*/
    private void onDoneClicked() {
        if (releaseDate != null) {
            args.putString("releaseDate", releaseDate.getText().toString());
        } else
            args.putString("releaseDate", "2014-09-10");

        if (voteAverage != null) {
            args.putString("voteAverage", voteAverage.getText().toString());
        } else
            args.putString("releaseDate", "0");

        args.putString("language", "en");
        dismiss();
    }

}
