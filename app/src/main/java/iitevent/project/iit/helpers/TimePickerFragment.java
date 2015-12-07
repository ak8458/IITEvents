package iitevent.project.iit.helpers;

/**
 * Created by Akshay Patil on 21-11-2015.
 * 1.	this class is used to generate a time picker fragment on click of the
 * time Editview and also set the user selected content to the Editview
 */
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.app.DialogFragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.TimePicker;

import iitevent.project.iit.activities.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget
        EditText tv = (EditText) getActivity().findViewById(R.id.eventTime);
        //Set a message for user

        //Display the user changed time on TextView
        tv.setText(String.valueOf(hourOfDay)+ ":" + String.valueOf(minute));
    }
}