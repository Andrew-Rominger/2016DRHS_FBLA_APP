package fbla.com.fbla_app_src;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;


public class signIn extends AppCompatActivity {
    //Declare variables
    public EditText UsernameIn;
    String Username;
    public EditText PassowrdIn;
    String Password;
    public Button signinButton;
    TextView recoverypassword;
    public static ProgressBar spinner;
    public ImageView goBackButton;
    static Intent moveTo;
    BackendlessUser user;
    int timeTried;

    //Called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setupUI(findViewById(R.id.signInBackground));
        //initailaze varriables and get views
        timeTried = 0;
        UsernameIn = (EditText) findViewById(R.id.userNameLogin);
        PassowrdIn = (EditText) findViewById(R.id.passwordLogin);
        spinner = (ProgressBar) findViewById(R.id.signInSpinenr);
        recoverypassword = (TextView) findViewById(R.id.recoverPassword);
        spinner.setVisibility(View.GONE);


        signinButton = (Button) findViewById(R.id.signInButton);
        goBackButton = (ImageView) findViewById(R.id.signIn_moveBackButton);

        /**
         * Called when the user clicks the sign in button
         */
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets the inputted username and password
                Username = UsernameIn.getText().toString();
                Password = PassowrdIn.getText().toString();

                //shows a loading spinner when attempting to login
                showSpinner();
                //Attempts to sign in user
                signInUser(Username, Password, signIn.this);
            }
        });
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signIn.this, MainActivity.class));
            }
        });
        recoverypassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(signIn.this, recoverPassword.class));
            }
        });


    }
    public static void showSpinner()
    {
        //shows the loading spinner
        spinner.setVisibility(View.VISIBLE);
    }
    public static void hideSpinner(){
        //hides the spinner
        spinner.setVisibility(View.GONE
        );}

    /**
     *
     * @param userName-The username to login with
     * @param password-The password to login with
     * @param c-The context being called from
     */
    public static void signInUser(final String userName, final String password, Context c)
    {
        //declare varriables to user
        final Context context = c;
        final Intent i = new Intent(c, profilePage.class);

        //makes a call to login the user and run a method if it succeds or fails
        Backendless.UserService.login(userName, password, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser backendlessUser) {
                //user logged in succesfully, move to profile page
                Toast.makeText(context, userName + " logged in", Toast.LENGTH_LONG).show();
                context.startActivity(i);


            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                //user did not signin, show why and hide spinner
                Toast.makeText(context, userName + " did not login", Toast.LENGTH_LONG).show();
                hideSpinner();
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
                    hideSoftKeyboard(signIn.this);
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
