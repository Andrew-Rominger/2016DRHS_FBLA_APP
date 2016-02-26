package fbla.com.fbla_app_src;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.UserService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserTokenStorageFactory;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
{

    //Declare variables
    private Button EmailSignUpButton;
    private TextView moveToSignInButton;
    private Button facebookSignIn;
    private Button googleSignIn;
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
        facebookSignIn = (Button) findViewById(R.id.buttonFacebook);
        googleSignIn = (Button) findViewById(R.id.buttonGoogle);
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

        facebookSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Backendless.UserService.loginWithFacebook(MainActivity.this, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser backendlessUser) {
                        startActivity(new Intent(MainActivity.this, extrainfo.class));
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(MainActivity.this, backendlessFault.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Backendless.UserService.loginWithGooglePlus(MainActivity.this, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser backendlessUser) {
                        startActivity(new Intent(MainActivity.this, extrainfo.class));
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(MainActivity.this, backendlessFault.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

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


