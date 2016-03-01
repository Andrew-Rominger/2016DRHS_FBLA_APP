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
    public EditText UsernameIn;
    String Username;
    public EditText PassowrdIn;
    String Password;
    public Button signinButton;
    public ImageView goBackButton;
    util Utility;
    Intent moveTo;

    //Called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        UsernameIn = (EditText) findViewById(R.id.userNameLogin);
        PassowrdIn = (EditText) findViewById(R.id.passwordLogin);

        signinButton = (Button) findViewById(R.id.signInButton);
        goBackButton = (ImageView) findViewById(R.id.signIn_moveBackButton);

        Utility = new util();

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Username = UsernameIn.getText().toString();
                Password = PassowrdIn.getText().toString();
                /*
                BackendlessUser user = Utility.signInUser(Username, Password, signIn.this);

              //  user = util.signInUser(Username, Password, signIn.this);

                util.signInUser(Username, Password, signIn.this);

                if(util.loggedIn())
                {
                    if (util.extraInfo()) {
                        Intent i = new Intent(signIn.this, extrainfo.class);
                        startActivity(i);
                    }
                    if (util.moveProfilePage()) {
                        Intent i = new Intent(signIn.this, profilePage.class);
                        startActivity(i);
                    }
                }
                /*
                if(user == null)
                {
                    Toast.makeText(signIn.this, "Sign in failed.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(Utility.checkForFirstTime(user))
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
}
