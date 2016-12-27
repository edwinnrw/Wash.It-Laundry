package com.project.edn.washit_laundry.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by EDN on 10/23/2016.
 */

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public EditText editText;
    DatePicker dpResult;
    Calendar c = Calendar.getInstance();
    Context context;

    @SuppressLint("ValidFragment")
    public DatePickerFragment(EditText editText) {
        this.editText=editText;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this,  year
                ,month, day);
        dialog.getDatePicker().setMinDate(c.getTimeInMillis());
        c.add(Calendar.MONTH, 3);
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());



        return  dialog;
        //return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(c.getTime()));

    }

}
