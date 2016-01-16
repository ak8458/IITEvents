# IITEvents
Final project Android

Introduction:

This Application is used to manage the events at IIT. We can use this app to register users, create events by users and also view all the events organized by all other users. This Application also generates a QR code when a new event is created, this QR code will contain all the details about the event which can be shared across social networking sites or posters etc.

We handle the user and event content using a Client Server Architecture, hosting all the related data on the server which can be accessed by performing GET/POST HTTP requests/responses from the mobile device to the server.

1)	Application uses Client Server Architecture.
2)	Advantage of this architecture is that it is not a device operating system specific ie any device can GET/POST requests to the server and receive and resolve responses.
3)	Client makes HTTP GET/POST request to the php file of a server with namevalue parameters. 
4)	The Server hosts PHP files which performs the database activities, executes queries on the server database, creates key value paired JSON objects and echo’s those objects back to the client.
5)	We parse the JSON object and perform various client activities.


Activity Functionality:
1)	LoginActivity
   This activity is used for the login of the user. All the registered users can login using their registered email and password. The Values for email and password are provided by the user during the registration.  Once user provides his/her credentials request goes to server where all the user details are stored. The credentials are verified with the details present on server. If user is authenticated then user main menu activity is loaded.

2)	RegisterUserActivity
   This activity is used for registration of new user. User Name, Email ID, Age and password are provided by the user. After getting all the values from the user the details are sent to server where it gets stored in database table. Once the user is successfully registered we move to the LoginActivity.

3)	UserMenuActivity
  This activity displays a menu to the user once logged in. It provides View Event, create event and Logout options to the users.

4)	EventListViewActivity
              When User clicks on View events from the menu activity then this activity is loaded, This Activity is used to display list of all the events. Out of those events, user can select a particular event and can view details of particular event and also register.

5)	EventDetailActivity
This activity displays all the details of the activity such as event name, event time, event location and event date. It also provides a “Going” button which when clicked, registers the user to that event.

6)	EventConfirmActivity
Once user registers for particular event he is notified and this activity displays other events to User. 

7)	RegisterEventActivity
When user clicks on “Create Event” button in the menu we load this activity which is used to register a new event created by the user. Here User key’s in all the details about the event such as Event Name, Date, time , location etc.

8)	EventCreatedActivity
Once the User has successfully created an event, this activity is loaded which comprises a success message and a QR code containing all the event related data in it.


Custom classes
1.	JSONParser
This class is used to perfom GET and POST requests to the server and parse the String buffer object to JASON response.

2.	ListViewAdapter
Custom adapter to display custom list view and perform operations such as select items in the list view.

3.	SessionManager
This class is used to store the shared preferences values for login activity.

4.	SQLiteHandler 
This class is used to store the user and event related data to the SQLite local database.

5.	DatePickerFragment: 
this class is used to generate a date picker fragment on click of the date Editview and also set the user selected content to the Editview.

6.	TimePickerFragment
this class is used to generate a time picker fragment on click of the time Editview and also set the user selected content to the Editview.

7.	QRCodeEncoder
This class is provided by ZXing API to encode the input and generate QR code image.



Future Improvements 
1.	Option to share QR code on various social media can be directly provided in the activity.

2.	Email alert can be triggered to the user when he/she clicks on GOING button.

3.	There can be provision to upload images while creating the event and also fetch and display in the listview.

4.	User can view the events which he/she is registered.(i.e. Events created by User)

5.	A multiple share option can be provided by using which user can share the detail of his events to his contacts.

6.	Search can be done on the basis of QR code available on any social media.


Lessons Learnt
1.	Setup Git Hub repository in Android Studio and implement version control.

2.	Added external JAR’s and corresponding dependencies in gradle file.

3.	How to generate QR code comprising custom messages.

4.	Worked with shared preferences, user session management and storing content in local SQLite database.

5.	Installing WAMP server and hosting databases and php files to perform database activities.


Issues Faced and Resolution:

1.	Date Picker was not giving a proper month value. The month value was coming one less than expected. E.g: For December month was coming as 11. To resolve this issue +1 was added to month value.

2.	Http host connection refused. This problem usually occurs when Server is off.

3.	Application was not running when device internet connection was off. The issue was handled to avoid application crash by validating the invalid response.

4.	Wamp server should be put online so that it is accessible by the external world.


