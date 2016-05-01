package fbla.com.fbla_app_src;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    static SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
    Date theDateOfBirth;
    Date currentDate;
    TextView termsAndConditions;
    public Calendar c;
    public Calendar cal;
    private int year;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI(findViewById(R.id.background));
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
        c = Calendar.getInstance();
        cal = Calendar.getInstance();
        year =  cal.get(Calendar.YEAR);
        year = year - 13;
        cal.set(year, Calendar.MONTH, Calendar.DATE);
        currentDate = cal.getTime();
        dateFormat.format(currentDate);
        // a method to execute when the go back button is pressed
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackToSignIn = new Intent(signupEmail.this, MainActivity.class);
                startActivity(goBackToSignIn);

            }
        });
        // a method to execute when the terms and conditions button is pressed
        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signupEmail.this, termsandconditionstextview.class));
            }
        });
        // this method signs the user up and moves the user to the profile page
        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final String newUserName = userNameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                String name = fullName.getText().toString();
                String email = emailInput.getText().toString();
                String passwordCheck = passwordInputCheck.getText().toString();
                Boolean isUserNameValid = Validator.isUserNameValid(newUserName);
                Boolean validEmail = Validator.isEmailValid(signupEmail.this, email);
                Boolean passwordValid = Validator.isPasswordValid(signupEmail.this, password, passwordCheck);
                String theDate = date.getText().toString();
                convertDate(theDate);
                Log.i(cal.toString(), "cal");
                if (util.shave(theDate) == null)
                {
                Toast.makeText(signupEmail.this, "Incorrect format for your Date of Birth, please use the format mmddyyyy", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        theDateOfBirth = dateFormat.parse(theDate);
                        Log.i("try", "sucess");
                    } catch (ParseException e) {
                        Toast.makeText(signupEmail.this, "Incorrect format for your Date of Birth, please use the format mmddyyyy", Toast.LENGTH_LONG).show();
                    }
                        dateFormat.format(theDateOfBirth);
                    if (theDateOfBirth.before(currentDate))
                    {
                        Log.i("User Date of birth", theDateOfBirth.toString());
                        Log.i("device date", currentDate.toString());
                        if (validEmail && passwordValid && isUserNameValid) {
                            user.setProperty("userName", newUserName);
                            user.setPassword(password);
                            user.setEmail(email);
                            user.setProperty("name", name);
                            user.setProperty("DOB", theDateOfBirth);
                            Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>()
                            {
                                @Override
                                public void handleResponse(BackendlessUser backendlessUser) {
                                    Backendless.UserService.login(user.getProperty("userName").toString(), user.getPassword(), new AsyncCallback<BackendlessUser>() {
                                        @Override
                                        public void handleResponse(BackendlessUser backendlessUser) {
                                            startActivity(new Intent(signupEmail.this, profilePage.class));
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault backendlessFault) {
                                            Toast.makeText(signupEmail.this, backendlessFault.getCode().toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }

                                @Override
                                public void handleFault(BackendlessFault backendlessFault) {
                                    Toast.makeText(signupEmail.this, backendlessFault.getCode().toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    else
                    {
                        Toast.makeText(signupEmail.this, "I'm sorry but you are too young to use this application", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    // converts the date into a string acceptable for use for backendless
    public static String convertDate(String string)
    {
        String end = "";
        ArrayList<Character> newArray = new ArrayList<Character>();
        for(int i = 0;i< string.length();i++)
        {
            newArray.add(string.charAt(i));
        }
        for(int i = 0; i< newArray.size();i++)
        {
            if(newArray.get(i) == '-' || newArray.get(i) == '/')
            {
                newArray.remove(i);
            }
        }
        for(int i = 0;i< newArray.size();i++)
        {
            end += newArray.get(i);
        }
        return end;
    }
    //this method hides the keyboard
    public static void hideSoftKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    //this method checks to see if the user has clicked outside of the edit text when the key board is up, if they do the hideSoftKeyboard() method is called
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(signupEmail.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

}
