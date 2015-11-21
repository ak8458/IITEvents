package iitevent.project.iit.iitevents;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.View.OnClickListener;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by demo on 19-11-2015.
 */

public class RegisterEvent extends Activity{
    EditText eventName,eventDate,eventTime,eventDescription,eventLocation;
    String eveName,eveDate,eveTime,eveDescription,eveLocation;
    private DatePickerDialog fromDatePickerDialog;
    private TimePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    static final int DATE_DIALOG_ID = 0;
    // url to create new product
    private static String url_create_product = "http://192.168.1.109/IITEvents/registerEvent.php";

    // JSON Node names
    private static final String TAG_ERROR = "error";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_reg_form);
        eventName=(EditText)findViewById(R.id.eventName);
        eventDate=(EditText)findViewById(R.id.eventDate);
        eventDate.setInputType(InputType.TYPE_NULL);

        eventTime=(EditText)findViewById(R.id.eventTime);
        eventDate.setInputType(InputType.TYPE_NULL);

        eventDescription=(EditText)findViewById(R.id.eventDescription);
        eventLocation=(EditText)findViewById(R.id.eventLocation);
        Button createEvent=(Button)findViewById(R.id.create_Event);

        eventTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });

        eventDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(),"DatePicker");
            }
        });
        eventTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getFragmentManager(), "TimePicker");
                }
            }
        });

        eventDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(),"DatePicker");
                }
            }
        });
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eveName = eventName.getText().toString();
                eveTime = eventTime.getText().toString();
                eveDate = eventDate.getText().toString();
                eveDescription = eventDescription.getText().toString();
                eveLocation = eventLocation.getText().toString();
                eveDate = eveDate + " " + eveTime;
                Log.d("my date",eveDate);
                new RegisterNewEvent().execute();
            }
        });


    }



    /**
     * Background Async Task to Create new product
     * */
    class RegisterNewEvent extends AsyncTask<Void, Void, Void> {
        String setIntent;
        /**0
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterEvent.this);
            pDialog.setMessage("Regestering Event..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected Void doInBackground(Void... args) {


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ename", eveName));
            params.add(new BasicNameValuePair("edate", eveDate));
            params.add(new BasicNameValuePair("location", eveLocation));
            params.add(new BasicNameValuePair("description", eveDescription));



            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(getResources().getString(R.string.serverIP),
                    "POST", params);

            // check log cat fro response
            if(json!=null) {
                Log.d("Create Response", json.toString());

                // check for success tag
                try {
                    boolean error = json.getBoolean(TAG_ERROR);

                    if (!error) {
                        // successfully created product
                        String eventID="Event ID: "+json.getInt("eid");
                        JSONObject event=json.getJSONObject("event");
                        String eventdate="Date: "+event.getString("edate");
                        String eventName="Event Name: "+event.getString("ename");
                        String eventLocation="Location: "+event.getString("location");
                        String eventDec="About: "+event.getString("description");
                        setIntent=eventID+"\n"+eventName +"\n"+eventdate +"\n"+eventLocation +"\n"+eventDec;
                        Intent toSuccessEvent = new Intent(getApplicationContext(), RegistrationSuccessActivity.class);
                        toSuccessEvent.putExtra("qrInput",setIntent);
                        startActivity(toSuccessEvent);

                        // closing this screen
                        finish();
                    } else {
                        // failed to create event
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Void file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();


        }

    }
}
