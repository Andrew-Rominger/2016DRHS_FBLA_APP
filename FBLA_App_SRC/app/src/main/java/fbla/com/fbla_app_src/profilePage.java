package fbla.com.fbla_app_src;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class profilePage extends AppCompatActivity {

    BackendlessUser user;
    TextView unameField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Bundle extras = getIntent().getExtras();
        unameField = (TextView) findViewById(R.id.usernameTextbox);
        Backendless.UserService.login(extras.getString("uname").toLowerCase(), extras.getString("pword").toLowerCase(), new AsyncCallback<BackendlessUser>()
        {
            public void handleResponse( BackendlessUser user )
            {
                // user has been logged in

            }

            public void handleFault( BackendlessFault fault )
            {
                // login failed, to get the error code call fault.getCode()
                Toast.makeText(getApplicationContext(), fault.getCode(), Toast.LENGTH_LONG).show();
            }

        });
        user = Backendless.UserService.CurrentUser();
        unameField.setText(user.getProperty("userName").toString());



    }

}
