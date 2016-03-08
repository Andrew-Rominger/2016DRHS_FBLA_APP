package fbla.com.fbla_app_src;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.UserService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserTokenStorageFactory;

import java.io.Serializable;
import java.util.Objects;
import fbla.com.fbla_app_src.util;


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

        timeTried = 0;
        UsernameIn = (EditText) findViewById(R.id.userNameLogin);
        PassowrdIn = (EditText) findViewById(R.id.passwordLogin);
        spinner = (ProgressBar) findViewById(R.id.signInSpinenr);
        spinner.setVisibility(View.GONE);


        signinButton = (Button) findViewById(R.id.signInButton);
        goBackButton = (ImageView) findViewById(R.id.signIn_moveBackButton);

       // Utility = new util();

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Username = UsernameIn.getText().toString();
                Password = PassowrdIn.getText().toString();


                showSpinner();
                if(Build.VERSION.SDK_INT >= 21)
                {
                    spinner.setTranslationZ(0);
                }
                else
                {
                    ViewCompat.setTranslationZ(spinner, 0);
                }
                signInUser(Username, Password, signIn.this);




                /*
                if(user == null)
                {
                    Toast.makeText(signIn.this, "Sign in failed.", Toast.LENGTH_LONG).show();
                    return;
                }


                if(util.checkForFirstTime(user))
                {
                    moveTo = new Intent(signIn.this, extrainfo.class);
                }
                else
                {
                    moveTo = new Intent(signIn.this, profilePage.class);
                }
                startActivity(moveTo);
                */
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
        spinner.setVisibility(View.VISIBLE);
    }
    public static void hideSpinner(){spinner.setVisibility(View.GONE);}
    public static void signInUser(final String userName, final String password, final Context c)
    {
        final Context context = c;
        final Intent i = new Intent(c, profilePage.class);
        Backendless.UserService.login(userName, password, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser backendlessUser) {
                Toast.makeText(context, userName + " logged in", Toast.LENGTH_LONG).show();
                c.startActivity(i);
                //Intent moveTo;
                Log.i("logged in", "sucessful");
            }
            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(context, userName + " did not login", Toast.LENGTH_LONG).show();
                hideSpinner();
            }
        });


    }
}
