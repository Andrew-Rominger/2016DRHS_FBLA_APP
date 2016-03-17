package fbla.com.fbla_app_src;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import com.facebook.CallbackManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
{

    //Declare variables
    private Button EmailSignUpButton;
    private TextView moveToSignInButton;
    CallbackManager callbackManager;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    //Called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Sets the app version for Backendless
        String appVersion = "v1";
        //Initializes Backendless
        Backendless.initApp(this, "67BF989E-7E10-5DB8-FFD7-C9147CA4F200", "12F047DB-382A-F6DA-FF16-C6A0A1F0CE00", appVersion);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/raleway.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        //Initialize variables and assign views
        EmailSignUpButton = (Button) findViewById(R.id.signupEmailButton);
        moveToSignInButton = (TextView) findViewById(R.id.main_SignInEmail);
        //Declare a function that is called when the EmailSignUpButton is clicked
        EmailSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Call a function that starts a new EmailSignUp activity and switches to it
                moveToEmailSignUp();
            }
        });
        //Declare a function that is called when the MoveToSignIn button is clicked
        moveToSignInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Call a function that starts a new signIn activity and switches to it
                moveToSignIn();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //moves user to sign up with their email
    public void moveToEmailSignUp()
    {
        Intent moveToSU = new Intent(MainActivity.this, signupEmail.class);
        startActivity(moveToSU);
    }
    public void moveToSignIn()
    {
        //declares a new intent and initialize it
        Intent movetosignIn = new Intent(MainActivity.this, signIn.class);
        //Starts and switches to the email sigin
        startActivity(movetosignIn);
    }
}


