package fbla.com.fbla_app_src;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.util.Log;
import android.widget.Toast;

import com.backendless.*;
import com.backendless.async.callback.BackendlessCallback;

public class login_activity extends AppCompatActivity
{
    ImageView goBack;
    BackendlessUser user;
    EditText email;
    EditText password;
    Button signUp;
    EditText passwordCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        goBack = (ImageView) findViewById(R.id.goBackPic);
        email = (EditText) findViewById(R.id.userNameField);
        password = (EditText) findViewById(R.id.passwordField);
        passwordCheck = (EditText) findViewById(R.id.passwordFieldCheck);
        signUp = (Button) findViewById(R.id.signUp);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent goBackToSignIn = new Intent(login_activity.this, MainActivity.class);
                startActivity(goBackToSignIn);

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                signUp(v);
            }
        });



    }

    public void signUp(View v)
    {
        if(!password.getText().equals(passwordCheck.getText()))
        {
            user = new BackendlessUser();
            user.setEmail(email.getText().toString());
            user.setPassword(password.getText().toString());

            Backendless.UserService.register(user, new BackendlessCallback<BackendlessUser>()
            {
                @Override
                public void handleResponse(BackendlessUser backendlessUser) {
                    Log.i("Registration", backendlessUser.getEmail() + " successfully registered");
                }
            });

        }
        else
        {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }


    }



}
