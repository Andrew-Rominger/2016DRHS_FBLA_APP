package fbla.com.fbla_app_src;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.DateTimeKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.facebook.internal.Utility;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import weborb.reader.DateType;

public class signupEmail extends AppCompatActivity
{
    Button goBack;
    EditText emailInput;
    EditText passwordInput;
    Button signUp;
    BackendlessUser user;
    CharSequence userName;
    EditText passwordInputCheck;
    Intent moveTo;
    CheckBox showPW;
    EditText userNameInput;
    EditText fullName;
    EditText date;
    SimpleDateFormat dateFormat;
    Date theDateOfBirth;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Buttons, images
        goBack = (Button) findViewById(R.id.signupEmail_gobackButton);
        signUp = (Button) findViewById(R.id.signUp);
        showPW = (CheckBox) findViewById(R.id.shwoPW);
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

                if (util.shave(theDate) == null)
                {
                Toast.makeText(signupEmail.this, "Incorrect format for your Date of Birth, please use the format mm-dd-yyyy", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        theDateOfBirth = dateFormat.parse(theDate);
                    } catch (ParseException e) {
                        Toast.makeText(signupEmail.this, "Incorrect format for your Date of Birth, please use the format mm-dd-yyyy", Toast.LENGTH_LONG).show();
                    }
                    if (isThirteen(theDateOfBirth))
                    {
                        if (validEmail && passwordValid && isUserNameValid) {
                            user.setProperty("userName", newUserName);
                            user.setPassword(password);
                            user.setEmail(email);
                            user.setProperty("name", name);
                            user.setProperty("DOB", theDateOfBirth);
                            Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse(BackendlessUser backendlessUser) {
                                    Backendless.UserService.login(user.getProperty("userName").toString(), user.getPassword(), new AsyncCallback<BackendlessUser>() {
                                        @Override
                                        public void handleResponse(BackendlessUser backendlessUser) {
                                            startActivity(new Intent(signupEmail.this, extrainfo.class));
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault backendlessFault) {
                                            Toast.makeText(signupEmail.this, backendlessFault.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }

                                @Override
                                public void handleFault(BackendlessFault backendlessFault) {
                                    Toast.makeText(signupEmail.this, backendlessFault.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
            }
        });
        /*
        emailInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {

                if (emailInput.getText().toString().length() >= 20)
                {
                    emailInput.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    return true;
                }
                return false;
            }
        });
        */
    }
    public static boolean isThirteen(Date date)
    {
        Calendar c = Calendar.getInstance();
        int current;
        int thirteen;
        thirteen = c.YEAR - 13;
        thirteen = c.MONTH;
        thirteen = c.DAY_OF_MONTH;
        current = date.getYear();
        if(thirteen > current )
        {
            return false;
        }
        else
        {
            return true;
        }
    }


        /*
        showPW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onCheckBoxClicked(v);
            }
        });
        */

    /*
    public void onCheckBoxClicked(View view)
    {
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordInput.setSelection(passwordInput.getText().length());
        } else {
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setSelection(passwordInput.getText().length());
        }
    }
    */

}
