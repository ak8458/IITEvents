package iitevent.project.iit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import iitevent.project.iit.helpers.SQLiteHandler;
import iitevent.project.iit.helpers.SessionManager;

/**
 * Created by demo on 03-12-2015.
 */
public class UserMenuActivity extends Activity {
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        Button viewEvents=(Button)findViewById(R.id.view_events);
        Button createEvent=(Button)findViewById(R.id.create_Event);
        Button logoutButton=(Button)findViewById(R.id.logout);

        viewEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewEventsIntent=new Intent(getApplicationContext(),EventListViewActivity.class);
                startActivity(viewEventsIntent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=new SQLiteHandler(getApplicationContext());
                db.deleteUsers();
                session = new SessionManager(getApplicationContext());
                session.setLogin(false);
                Intent logoutIntent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(logoutIntent);
                finish();
            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerEventIntent=new Intent(getApplicationContext(),RegisterEventActivity.class);
                startActivity(registerEventIntent);
            }
        });

    }
}
