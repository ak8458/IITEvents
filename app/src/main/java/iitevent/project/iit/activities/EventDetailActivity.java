package iitevent.project.iit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by demo on 22-11-2015.
 */
public class EventDetailActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        //get all the quotes from the xml resource as an array of string.
        Intent intent=getIntent();
        String eventName=intent.getStringExtra("eName");
        String eDate=intent.getStringExtra("eDate");
        String eTime=intent.getStringExtra("eTime");
        String eDesc=intent.getStringExtra("eDesc");
        String eLoc=intent.getStringExtra("eLoc");

        TextView eNameView =(TextView)findViewById(R.id.eventDetailName);
        TextView edateView =(TextView)findViewById(R.id.eventDetailDate);
        TextView eTimeView =(TextView)findViewById(R.id.eventDetailTime);
        TextView eDescView =(TextView)findViewById(R.id.eventDetailDescription);
        TextView eLocView=(TextView)findViewById(R.id.eventDetailLocation);

        eNameView.setText(eventName);
        edateView.setText(eDate);
        eTimeView.setText(eTime);
        eDescView.setText(eDesc);
        eLocView.setText(eLoc);

    }
    }

