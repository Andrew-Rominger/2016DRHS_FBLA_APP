package fbla.com.fbla_app_src;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class signIn extends AppCompatActivity {
    //Declare variables
    public EditText enterUsername;
    public EditText enterPassword;
    public Button signinButton;
    public ImageView goBackButton;
    Intent moveTo;

    //Called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        enterUsername = (EditText) findViewById(R.id.userNameLogin);
        enterPassword = (EditText) findViewById(R.id.passwordLogin);
        signinButton = (Button) findViewById(R.id.signInButton);
        goBackButton = (ImageView) findViewById(R.id.signIn_moveBackButton);

        String appVersion = "v1";
        Backendless.initApp(this, "67BF989E-7E10-5DB8-FFD7-C9147CA4F200", "12F047DB-382A-F6DA-FF16-C6A0A1F0CE00", appVersion);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(signIn.this, "Signing in...", Toast.LENGTH_SHORT);
                Backendless.UserService.login(enterUsername.getText().toString(), enterPassword.getText().toString(), new AsyncCallback<BackendlessUser>() {
                    public void handleResponse(BackendlessUser user)
                    {
                        // user has been logged in
                        user = Backendless.UserService.CurrentUser();
                        Boolean isFirstTime = (Boolean) user.getProperty("firstTimeLogin");

                        Toast.makeText(signIn.this, "User " + user.getProperty("userName") + " logged in.", Toast.LENGTH_LONG).show();
                        if (isFirstTime) {
                            user.setProperty("firstTimeLogin", false);
                            Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse(BackendlessUser backendlessUser) {

                                }

                                @Override
                                public void handleFault(BackendlessFault backendlessFault) {

                                }
                            });
                            moveTo = new Intent(signIn.this, extrainfo.class);
                        } else {
                            moveTo = new Intent(signIn.this, profilePage.class);
                        }
                        startActivity(moveTo);
                    }

                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(signIn.this, "Code: " + fault.getCode(), Toast.LENGTH_LONG).show();
                    }
                }, true);
            }
        });
        goBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(signIn.this, MainActivity.class));
            }
        });
    }




}
