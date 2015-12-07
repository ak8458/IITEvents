package iitevent.project.iit.helpers;

/**
 * Created by Dhruv on 19-Nov-15.
 * This class is used to store the details of logged in user to SQLite database.
 * It is also used to store evnts to the SQLite Database.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import iitevent.project.iit.bean.Event;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "login_session";

    // Login table name
    private static final String TABLE_USER = "users";

    private static final String TABLE_EVENT = "events";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "fullName";
    private static final String KEY_AGE = "age";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private static final String EVENT_ID = "eid";
    private static final String EVENT_NAME = "eventName";
    private static final String EVENT_DATE = "eventdate";
    private static final String EVENT_DESC = "eventdesc";
    private static final String EVENT_TIME = "eventtime";
    private static final String EVENT_LOCATION = "eventlocation";




    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_AGE + " TEXT," + KEY_EMAIL + " TEXT,"
                + KEY_PASSWORD + " TEXT" + ")";

        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENT + "("
                + EVENT_ID + " INTEGER PRIMARY KEY," + EVENT_NAME + " TEXT,"
                + EVENT_DATE + " TEXT," + EVENT_DESC + " TEXT,"+EVENT_LOCATION+" TEXT,"
                + EVENT_TIME + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_EVENT_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public void addUser(int id, String name, String age, String email, String pwd) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);//ID
        values.put(KEY_NAME, name); // Name
        values.put(KEY_AGE, age); // Age
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_PASSWORD, pwd); // pwd


        // Inserting Row
        long idn = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + idn);
    }

    public void addAllEvents(ArrayList<Event> eventList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for(Event e:eventList){
            ContentValues values = new ContentValues();
            values.put(EVENT_ID, e.getEventID());//Event ID
            values.put(EVENT_LOCATION, e.getEventLocation()); // Location
            values.put(EVENT_DESC, e.getEventDescription()); // Description
            values.put(EVENT_TIME, e.getEventTime()); // Time
            values.put(EVENT_DATE, e.getEventDate()); // date
            values.put(EVENT_NAME, e.getEventName());


            // Inserting Row
            long idn = db.insert(TABLE_EVENT, null, values);

        }
        db.close(); // Closing database connection

        Log.d(TAG, "All events inserted");
    }

    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("id", cursor.getString(0));
            user.put("fullName", cursor.getString(1));
            user.put("age", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("password", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> events=new ArrayList<Event>();

        String selectQuery = "SELECT  * FROM " + TABLE_EVENT;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);


            while (cursor.moveToNext()) {
                Event event = new Event();
                event.setEventID(cursor.getInt(cursor.getColumnIndex(EVENT_ID)));
                event.setEventName(cursor.getString(cursor.getColumnIndex(EVENT_NAME)));
                event.setEventDate(cursor.getString(cursor.getColumnIndex(EVENT_DATE)));
                event.setEventTime(cursor.getString(cursor.getColumnIndex(EVENT_TIME)));
                event.setEventLocation(cursor.getString(cursor.getColumnIndex(EVENT_LOCATION)));
                event.setEventDescription(cursor.getString(cursor.getColumnIndex(EVENT_DESC)));
                events.add(event);
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "getAllEvents "+e.getMessage());
        }
        db.close();
        // return user
        Log.d(TAG, "Fetching events from SQLite " + events.toString());

        return events;
    }
}

