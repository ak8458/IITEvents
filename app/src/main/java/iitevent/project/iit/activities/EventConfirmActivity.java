package iitevent.project.iit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author Akshay Patil
 * This activity is loaded when the User confirms going to the event.
 */
public class EventConfirmActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attend_success);

        Button moreEvents=(Button)findViewById(R.id.view_more_events);

        /**
         * When more events button is clicked the List of events activity is loaded and the current activity is finished.
         */
        moreEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewMoreEvent=new Intent(EventConfirmActivity.this,EventListViewActivity.class);
                startActivity(viewMoreEvent);
                finish();
            }
        });
    }
}
