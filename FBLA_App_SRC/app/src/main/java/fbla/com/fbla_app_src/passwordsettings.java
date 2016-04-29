package fbla.com.fbla_app_src;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.facebook.internal.Utility;

import fbla.com.fbla_app_src.util;


public class passwordsettings extends AppCompatActivity {
    // Declared global variables
    ImageView back;
    Button done;
    EditText currentPassword;
    EditText anotherPassword;
    EditText anotherPasswordTwo;
    String theCurrentPassword;
    String theAnotherPassword;
    String theAnotherPasswordTwo;
    String passwordThatIsCurrent;
    BackendlessUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordsettings);
        setupUI(findViewById(R.id.passwordSettingsBackground));
        back = (ImageView) findViewById(R.id.imageView11);
        done = (Button) findViewById(R.id.donebutton);
        currentPassword = (EditText) findViewById(R.id.editText5);
        anotherPassword = (EditText) findViewById(R.id.editText6);
        anotherPasswordTwo = (EditText) findViewById(R.id.editText7);
        user = Backendless.UserService.CurrentUser();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                theAnotherPassword = anotherPassword.getText().toString();
                theAnotherPasswordTwo = anotherPasswordTwo.getText().toString();
                theCurrentPassword = currentPassword.getText().toString();
                Log.i("MADE It", "MADE IT");
                if (user != null) {
                    passwardEqual(theCurrentPassword);
                    /*
                    if (true) {
                        if (theAnotherPassword == theAnotherPasswordTwo) {
                            user.setPassword(theAnotherPassword);
                            Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse(BackendlessUser backendlessUser) {
                                    Toast.makeText(passwordsettings.this, "Success", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void handleFault(BackendlessFault backendlessFault) {
                                    Toast.makeText(passwordsettings.this, "Failed to reset password", Toast.LENGTH_LONG).show();
                                }
                            });

                        } else {
                            Toast.makeText(passwordsettings.this, "Passwords do not Match!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(passwordsettings.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                    }
                    */
                } else {
                    Toast.makeText(passwordsettings.this, "NULL", Toast.LENGTH_LONG).show();
                }
            }
        });
        //sets on click for going back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }
    //takes user back to profile page
    public void goBack()
    {
        Intent i = new Intent(passwordsettings.this, profilePage.class);
        startActivity(i);
    }
    public void passwardEqual(final String currentPassword)
    {
        Backendless.UserService.login(user.getProperty("userName").toString(), currentPassword, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser backendlessUser) {
                user.setPassword(theAnotherPassword);
                Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser backendlessUser) {
                        Toast.makeText(passwordsettings.this, "Success", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(passwordsettings.this, accountsettings.class));
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(passwordsettings.this, "Failed to reset password", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(passwordsettings.this, "Incorrect passowrd", Toast.LENGTH_LONG).show();
            }
        });
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
                    hideSoftKeyboard(passwordsettings.this);
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
