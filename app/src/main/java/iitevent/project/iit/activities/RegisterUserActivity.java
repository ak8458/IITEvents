package iitevent.project.iit.activities;

/**
 * Created by Dhruv on 19-Nov-15.
 * This class is used to register a new user
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


    String fullName,email,password,age;
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

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fullName = inputFullName.getText().toString().trim();
                age=inputAge.getText().toString().trim();
                email=inputEmail.getText().toString().trim();
                password=inputPassword.getText().toString().trim();


                if (!fullName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !age.isEmpty()) {
                    if(isEmailValid(email)) {
                        if(age.length()<4&&Integer.parseInt(age)<150) {
                            new RegisterNewUser().execute();

                           }
                        else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.InvalidAge), Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.ValidMailError), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.EnterAllDetailsMsg), Toast.LENGTH_LONG).show();
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

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    /**
     * Background Async Task to Create new product
     * */
    class RegisterNewUser extends AsyncTask<Void, Void, Void> {
        /**0
         * Before starting background thread Show Progress Dialog
         * */
        private Boolean userExists;
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
            params.add(new BasicNameValuePair("uname", fullName));
            params.add(new BasicNameValuePair("uage",age));
            params.add(new BasicNameValuePair("uemailID", email));
            params.add(new BasicNameValuePair("upwd", password));

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
                        String userName="Full Name: "+user.getString("fullName");
                        String userAge="Age: "+user.getString("age");
                        String userEmail="Email: "+user.getString("emailID");
                        String userPwd="Pwd: "+user.getString("pwd");

                        userExists=false;

                    } else {
                        // failed to create event
                        userExists=true;
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
            if(userExists)
            {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.RegisteredEmail), Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.RegistrationSuccess), Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
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