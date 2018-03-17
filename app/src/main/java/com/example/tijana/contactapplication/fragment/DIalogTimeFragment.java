package com.example.tijana.contactapplication.fragment;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.example.tijana.contactapplication.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DIalogTimeFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int hourOfDay;
    private int minute;




    public DIalogTimeFragment() {
        // Required empty public constructor
    }




    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;

    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    @Override
    public Dialog onCreateDialog(Bundle state) {
        Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hourOfDay, minute, true);
    }
}