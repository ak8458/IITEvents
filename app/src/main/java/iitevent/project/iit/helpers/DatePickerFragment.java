package iitevent.project.iit.helpers;


/**
 * Created by demo on 21-11-2015.
 */
        import android.app.DatePickerDialog;
        import android.os.Bundle;
        import android.app.Fragment;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.app.DialogFragment;
        import android.app.Dialog;
        import java.util.Calendar;

        import iitevent.project.iit.activities.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(getActivity(),this,year,month,day);
    }



    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        EditText tv = (EditText) getActivity().findViewById(R.id.eventDate);
        //Set a message for user

        //Display the user changed time on TextView
        tv.setText(String.valueOf(year)+ "-" + String.valueOf(monthOfYear)+"-"+String.valueOf(dayOfMonth));
    }
}