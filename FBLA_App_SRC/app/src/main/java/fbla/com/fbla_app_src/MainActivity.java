package fbla.com.fbla_app_src;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import com.facebook.FacebookSdk;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;

public class MainActivity extends AppCompatActivity {
    private Button signUp;
    private EditText emailField;
    private EditText passwordField;
    BackendlessUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String appVersion = "v1";
        Backendless.initApp(this, "67BF989E-7E10-5DB8-FFD7-C9147CA4F200", "12F047DB-382A-F6DA-FF16-C6A0A1F0CE00", appVersion);

        signUp = (Button) findViewById(R.id.signUpButton);
        emailField = (EditText) findViewById(R.id.emailtext);
        passwordField = (EditText) findViewById(R.id.passwordText);


        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signUp(v);
            }
        });
    }
    public void signUp(View v)
    {
        user = new BackendlessUser();
        user.setEmail(emailField.getText().toString());
        user.setPassword(passwordField.getText().toString());
        Backendless.UserService.register(user, new BackendlessCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser backendlessUser) {
                Log.i("Registration", backendlessUser.getEmail() + " successfully registered");
            }
        });

    }



}
