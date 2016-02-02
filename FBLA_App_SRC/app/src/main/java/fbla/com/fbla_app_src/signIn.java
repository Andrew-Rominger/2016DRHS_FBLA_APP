package fbla.com.fbla_app_src;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;

public class signIn extends AppCompatActivity{
    public EditText enterUsername;
    public EditText enterPassword;
    public Button signinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        enterUsername = (EditText) findViewById(R.id.userNameLogin);
        enterPassword = (EditText) findViewById(R.id.passwordLogin);
        signinButton = (Button) findViewById(R.id.signInButton);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Backendless.UserService.login(enterUsername.getText().toString().toLowerCase(), enterPassword.getText().toString().toLowerCase(), new AsyncCallback<BackendlessUser>()
                {
                    public void handleResponse( BackendlessUser user )
                    {
                        // user has been logged in
                        Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_LONG).show();
                    }

                    public void handleFault( BackendlessFault fault )
                    {
                        // login failed, to get the error code call fault.getCode()
                        Toast.makeText(getApplicationContext(), fault.getCode(), Toast.LENGTH_LONG).show();
                    }

                });

            }
        });



    }


}
