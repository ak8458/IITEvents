package iitevent.project.iit.activities;

/**
 * Created by Dhruv on 19-Nov-15.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;

import iitevent.project.iit.helpers.JSONParser;


public class RegisterUserActivity extends Activity {
    private static final String TAG = RegisterUserActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputAge;
    private EditText inputPassword;
    private ProgressDialog pDialog;

    String FullName,Email,Password,Age;
    JSONParser jsonParser = new JSONParser();
    static final int DATE_DIALOG_ID = 0;
    // JSON Node names
    private static final String TAG_ERROR = "error";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reg);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputAge=(EditText)findViewById(R.id.age);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

       /* // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }*/

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String age=inputAge.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    new RegisterNewUser().execute();
                } else {
                    Toast.makeText(getApplicationContext(),"Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Background Async Task to Create new product
     * */
    class RegisterNewUser extends AsyncTask<Void, Void, Void> {
        String setIntent;
        /**0
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterUserActivity.this);
            pDialog.setMessage("Registering User..");
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
            params.add(new BasicNameValuePair("uname", FullName));
            params.add(new BasicNameValuePair("uage",Age));
            params.add(new BasicNameValuePair("uemailID", Email));
            params.add(new BasicNameValuePair("upwd", Password));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest("http://"+getResources().getString(R.string.serverIP)+"/IITEvents/registerUser.php","POST", params);

            // check log cat fro response
            if(json!=null) {
                Log.d("Create Response", json.toString());

                // check for success tag
                try {
                    boolean error = json.getBoolean(TAG_ERROR);

                    if (!error) {
                        // successfully created product
                        String userID="User ID: "+json.getInt("uid");
                        JSONObject user=json.getJSONObject("user");
                        String userName="Full Name: "+user.getString("uname");
                        String userAge="Age: "+user.getString("age");
                        String userEmail="Email: "+user.getString("email");
                        String userPwd="Pwd: "+user.getString("pwd");
                        setIntent=userID+"\n"+userName +"\n"+userAge +"\n"+userPwd;
                        Intent toSuccessEvent = new Intent(getApplicationContext(), LoginActivity.class);
                        toSuccessEvent.putExtra("qrInput",setIntent);
                        startActivity(toSuccessEvent);

                        // closing this screen
                        finish();
                    } else {
                        // failed to create event
                    }
                }
                catch (JSONException e)
                {
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

        private void showDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }

        private void hideDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

}
