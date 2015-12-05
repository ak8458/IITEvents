package iitevent.project.iit.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iitevent.project.iit.helpers.JSONParser;
import iitevent.project.iit.helpers.SQLiteHandler;

/**
 * Created by Akshay Patil on 22-11-2015.
 * This activity displays the details about the event and accepts user input if attending.
 */
public class EventDetailActivity extends Activity {
    private ProgressDialog pDialog;
    String eventName,eDate,eTime,eDesc,eLoc,eID,uID,errorToast;
    JSONParser jsonParser = new JSONParser();
    private SQLiteHandler db;

    private static final String TAG_ERROR = "error";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);

        //get all the quotes from the xml resource as an array of string.
        Intent intent=getIntent();
        eventName=intent.getStringExtra("eName");
        eDate=intent.getStringExtra("eDate");
        eTime=intent.getStringExtra("eTime");
        eDesc=intent.getStringExtra("eDesc");
        eLoc=intent.getStringExtra("eLoc");
        eID=intent.getStringExtra("eID");

        db= new SQLiteHandler(getApplicationContext());
        HashMap<String, String> userDetail=db.getUserDetails();
        uID=userDetail.get("id");


        TextView eNameView =(TextView)findViewById(R.id.eventDetailName);
        TextView edateView =(TextView)findViewById(R.id.eventDetailDate);
        TextView eTimeView =(TextView)findViewById(R.id.eventDetailTime);
        TextView eDescView =(TextView)findViewById(R.id.eventDetailDescription);
        TextView eLocView=(TextView)findViewById(R.id.eventDetailLocation);
        Button attendEvent=(Button)findViewById(R.id.attend_event);

        eNameView.setText(eventName);
        edateView.setText(eDate);
        eTimeView.setText(eTime);
        eDescView.setText(eDesc);
        eLocView.setText(eLoc);

        attendEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegisterUserToEvent().execute();
            }
        });

    }

    /**
     * Background Async Task to register user to event
     * */
    class RegisterUserToEvent extends AsyncTask<Void, Void, Void> {
        boolean internetConnection=true;
        String setIntent;
        /**0
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EventDetailActivity.this);
            pDialog.setMessage("Confirming..");
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
            params.add(new BasicNameValuePair("eID", eID));
            params.add(new BasicNameValuePair("uID", uID));



            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest("http://"+getResources().getString(R.string.serverIP)+"/IITEvents/registerUserToEvent.php",
                    "POST", params);

            // check log cat fro response
            if(json!=null) {
                Log.d("Create Response", json.toString());

                // check for success tag
                try {
                    boolean error = json.getBoolean(TAG_ERROR);

                    if (!error) {

                        Intent regToEvent = new Intent(getApplicationContext(), EventConfirmActivity.class);
                        startActivity(regToEvent);

                        // closing this screen
                        finish();
                    } else {
                        errorToast=json.getString("error_msg");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                internetConnection=false;
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Void file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            if(internetConnection) {
                if (errorToast != null) {
                    Toast.makeText(getApplicationContext(), errorToast, Toast.LENGTH_LONG).show();
                } else {
                    finish();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.noConnection), Toast.LENGTH_LONG).show();
            }
        }

    }
}

