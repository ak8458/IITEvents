package iitevent.project.iit.activities;

import android.app.Activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import iitevent.project.iit.bean.Event;
import iitevent.project.iit.helpers.JSONParser;
import iitevent.project.iit.helpers.ListViewAdapter;

/**
 * Created by demo on 21-11-2015.
 */
public class EventListViewActivity extends Activity {
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;
    ListView list;
    ListViewAdapter listviewadapter;
    List<Event> eventList=new ArrayList<Event>();

    // JSON Node names
    private static final String TAG_ERROR = "error";
    private static final String TAG_EVENTS = "events";

    private static final String TAG_EID = "eid";
    private static final String TAG_ENAME = "ename";
    private static final String TAG_ELOC = "location";
    private static final String TAG_EDESC = "description";
    private static final String TAG_EDATE = "edate";

    // products JSONArray
    JSONArray events = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        new LoadAllProducts().execute();

        list = (ListView) findViewById(R.id.events_listview);
        listviewadapter = new ListViewAdapter(EventListViewActivity.this, R.layout.event_item,
                eventList);
        list.setAdapter(listviewadapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //declaring and instantiating a new intent to redirect to a new activity
                Intent intent = new Intent(EventListViewActivity.this, EventDetailActivity.class);

                //sending the quote detail like image and quote to the new activity
                Event eventDetail = listviewadapter.getItem(position);

                intent.putExtra("eName", eventDetail.getEventName());
                intent.putExtra("eDate", eventDetail.getEventDate());
                intent.putExtra("eTime", eventDetail.getEventTime());
                intent.putExtra("eDesc", eventDetail.getEventDescription());
                intent.putExtra("eLoc", eventDetail.getEventLocation());
                //starting the new activity
                startActivity(intent);


            }
        });
    }

    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EventListViewActivity.this);
            pDialog.setMessage("Loading Events. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest("http://"+getResources().getString(R.string.serverIP)+"/IITEvents/getAllEvents.php", "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                boolean success = json.getBoolean(TAG_ERROR);

                if (!success) {
                    // products found
                    // Getting Array of Products
                    events = json.getJSONArray(TAG_EVENTS);

                    // looping through All Products
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject c = events.getJSONObject(i);

                        // Storing each json item in variable
                        int eid = Integer.parseInt(c.getString(TAG_EID));
                        String ename = c.getString(TAG_ENAME);
                        String edate = c.getString(TAG_EDATE);
                        String location = c.getString(TAG_ELOC);
                        String description = c.getString(TAG_EDESC);
                        try {
                            DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            Date d = f.parse(edate);
                            DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
                            DateFormat time = new SimpleDateFormat("hh:mm:ss a");
                            System.out.println("Date: " + date.format(d));
                            System.out.println("Time: " + time.format(d));
                            Event event =new Event(ename,eid, description, location, date.format(d), time.format(d));
                            eventList.add(event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    /*// no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewProductActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();


        }

    }
}
