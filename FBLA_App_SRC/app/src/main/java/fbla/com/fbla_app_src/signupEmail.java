package fbla.com.fbla_app_src;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class signupEmail extends AppCompatActivity
{
    // Declared global variables
    Button goBack;
    EditText emailInput;
    EditText passwordInput;
    Button signUp;
    BackendlessUser user;
    EditText passwordInputCheck;
    CheckBox showPW;
    EditText userNameInput;
    EditText fullName;
    EditText date;
    static SimpleDateFormat dateFormat;
    Date theDateOfBirth;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Buttons, images
        termsAndConditions = (TextView) findViewById(R.id.TandCTV);
        goBack = (Button) findViewById(R.id.signupEmail_gobackButton);
        signUp = (Button) findViewById(R.id.signUp);
        emailInput = (EditText) findViewById(R.id.signupEmail_emailField);
        userNameInput = (EditText) findViewById(R.id.signupEmail_userName);
        passwordInput = (EditText) findViewById(R.id.signupEmail_password);
        passwordInputCheck = (EditText) findViewById(R.id.signupEmail_passwordCheck);
        fullName = (EditText) findViewById(R.id.FandLName);
        date = (EditText) findViewById(R.id.dob);
        user = new BackendlessUser();
        dateFormat = new SimpleDateFormat("MMddyyyy");



        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackToSignIn = new Intent(signupEmail.this, MainActivity.class);
                startActivity(goBackToSignIn);

            }
        });
        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signupEmail.this, termsandconditionstextview.class));
            }
        });
        //Declare a fucntion to be called when the signup button is pressed
        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                //get information from page and store in variables
                final String newUserName = userNameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                String name = fullName.getText().toString();
                String email = emailInput.getText().toString();
                String passwordCheck = passwordInputCheck.getText().toString();
                Boolean isUserNameValid = Validator.isUserNameValid(newUserName);
                Boolean validEmail = Validator.isEmailValid(signupEmail.this, email);
                Boolean passwordValid = Validator.isPasswordValid(signupEmail.this, password, passwordCheck);
                String theDate = date.getText().toString();

                //Checks for correct date format
                if (util.shave(theDate) == null)
                {
                    //Failed date format check
                Toast.makeText(signupEmail.this, "Incorrect format for your Date of Birth, please use the format mm-dd-yyyy", Toast.LENGTH_LONG).show();
                }
                else {
                    //date format is correct
                    try {
                        theDateOfBirth = dateFormat.parse(theDate);
                        //Log.i("try", "sucess");
                    } catch (ParseException e) {
                        //Could not parse date
                        Toast.makeText(signupEmail.this, "Incorrect format for your Date of Birth, please use the format mm-dd-yyyy", Toast.LENGTH_LONG).show();
                    }
                    //checks if the user is older than 13
                    if (isThirteen(theDateOfBirth))
                    {
                        //Checks for vallid email, password, and username
                        if (validEmail && passwordValid && isUserNameValid) {
                            //Sets the properties of the new user object to be uploaded to what was imputed by the user
                            user.setProperty("userName", newUserName);
                            user.setPassword(password);
                            user.setEmail(email);
                            user.setProperty("name", name);
                            user.setProperty("DOB", theDateOfBirth);

                            //Make a call to register the user and recive a callback with either a succes or failure method
                            Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>()
                            {
                                @Override
                                public void handleResponse(BackendlessUser backendlessUser)
                                {
                                    //User created succesfully, then log them in and move to the profile page activity
                                    Backendless.UserService.login(user.getProperty("userName").toString(), user.getPassword(), new AsyncCallback<BackendlessUser>()
                                    {
                                        @Override
                                        public void handleResponse(BackendlessUser backendlessUser) {
                                            startActivity(new Intent(signupEmail.this, profilePage.class));
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault backendlessFault) {
                                            Toast.makeText(signupEmail.this, backendlessFault.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                                //User not created, tells them why
                                @Override
                                public void handleFault(BackendlessFault backendlessFault) {
                                    Toast.makeText(signupEmail.this, backendlessFault.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    else
                    {
                        Toast.makeText(signupEmail.this, "You must be 13 to register for Suit", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    /**
     * Method to check if the date inputted is 13 or more years ago
     * @param date- The date to check
     * @return true or false if the date is 13 or more years ago
     */
    public boolean isThirteen(Date date)
    {
        //New date object, will hold current date
        Date currentDate;
        //Declare new date format to use when calculating
        dateFormat = new SimpleDateFormat("MMddyyyy");
        //Get an instance of the calender object
        Calendar c = Calendar.getInstance();
        //add -13 to the calender to get the date 13 years ago
        c.add(Calendar.YEAR, -13);
        //format the calender
        dateFormat.format(c.get(Calendar.YEAR));
        //get the current date
        currentDate = c.getTime();
        //check if the current date is after the date inputed(-13 years)
        if(currentDate.after(date))
        {

            Log.i("returned ", "true");
            return true;
        }
        else
        {
            Log.i("returned", "false");
            return false;
        }
    }
}
