package fbla.com.fbla_app_src;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    public static ProgressBar spinner;
    public ImageView goBackButton;
    static Intent moveTo;
    BackendlessUser user;
    int timeTried;

    //Called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //initailaze varriables and get views
        timeTried = 0;
        UsernameIn = (EditText) findViewById(R.id.userNameLogin);
        PassowrdIn = (EditText) findViewById(R.id.passwordLogin);
        spinner = (ProgressBar) findViewById(R.id.signInSpinenr);
        spinner.setVisibility(View.GONE);


        signinButton = (Button) findViewById(R.id.signInButton);
        goBackButton = (ImageView) findViewById(R.id.signIn_moveBackButton);

        /**
         * Called when the user clicks the sign in button
         */
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Gets the inputted username and password
                Username = UsernameIn.getText().toString();
                Password = PassowrdIn.getText().toString();

                //shows a loading spinner when attempting to login
                showSpinner();
                //Attempts to sign in user
                signInUser(Username, Password, signIn.this);
            }
        });
        goBackButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(signIn.this, MainActivity.class));
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
            public void handleFault(BackendlessFault backendlessFault)
            {
                //user did not signin, show why and hide spinner
                Toast.makeText(context, userName + " did not login", Toast.LENGTH_LONG).show();
                hideSpinner();
            }
        });


    }
}
