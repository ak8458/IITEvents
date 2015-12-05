package iitevent.project.iit.activities;

/**
 * Created by Dhruv on 19-Nov-15.
 * This class is used for login of user
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
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import iitevent.project.iit.activities.RegisterUserActivity;
import iitevent.project.iit.activities.UserMenuActivity;
import iitevent.project.iit.helpers.JSONParser;
import iitevent.project.iit.helpers.SQLiteHandler;
import iitevent.project.iit.helpers.SessionManager;

/**
 * @author Dhruv V
 * This class is used to authenticate the user and set the user session and add details to SQLite database.
 *
 */
public class LoginActivity extends Activity {
    private static final String TAG = RegisterUserActivity.class.getSimpleName();
    private Button btnLogin;
    boolean userVerified;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    JSONParser jsonParser = new JSONParser();
    String email,password;
    String fullName;
    String age;
    String emailID;
    String pwd;
    int userID;
    boolean falseLogin;
    // JSON Node names
    private static final String TAG_ERROR = "error";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, UserMenuActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                userVerified=false;
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    if(isEmailValid(email)) {
                        // login user
                        new VerifyUser().execute();
                    }
                    else{
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.InvalidFormatEmailError), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.EnterEmailAndPwdError), Toast.LENGTH_LONG).show();
                }
            }
        });


        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent viewMenu = new Intent(getApplicationContext(),RegisterUserActivity.class);
                startActivity(viewMenu);
               // finish();
            }
        });

    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }
    /**
     * Background Async Task to Create new product
     * */
    class VerifyUser extends AsyncTask<Void, Void, Void> {
        boolean internetConnection=true;
        /**0
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in ...");
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
            params.add(new BasicNameValuePair("email", email));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            JSONObject json = jsonParser.makeHttpRequest("http://"+getResources().getString(R.string.serverIP)+"/IITEvents/verifyCredentials.php","POST", params);

            if(json!=null) {
                Log.d("Create Response", json.toString());

                // check for success tag
                try {
                    falseLogin = json.getBoolean(TAG_ERROR);

                    if (!falseLogin)
                    {
                        // successfully created product

                        JSONObject user=json.getJSONObject("user");
                        userID = json.getInt("uid");
                        fullName=user.getString("fullName");
                        age=user.getString("age");
                        emailID=user.getString("emailID");
                        pwd=user.getString("pwd");
                        if(emailID.equals(email) && pwd.equals(password))
                        {
                            session.setLogin(true);
                            db.addUser(userID,fullName,age,emailID,pwd);
                            userVerified=true;
                            Intent intent = new Intent(LoginActivity.this, UserMenuActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                catch (JSONException e)
                {
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
                if (!userVerified) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.InvalidCredential), Toast.LENGTH_LONG).show();
                } else {
                    finish();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.noConnection), Toast.LENGTH_LONG).show();
            }
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