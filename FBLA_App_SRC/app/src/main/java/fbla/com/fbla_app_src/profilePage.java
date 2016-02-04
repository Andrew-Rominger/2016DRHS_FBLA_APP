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
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import org.w3c.dom.Text;

public class profilePage extends AppCompatActivity {

    BackendlessUser user;
    TextView unameField;
    TextView emailField;
    TextView phoneNumField;
    TextView nameField;
    Button loggoutButton;
    util Utility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Utility = new util();
        user = Backendless.UserService.CurrentUser();

        unameField = (TextView) findViewById(R.id.profilePageUserNameField);
        emailField = (TextView) findViewById(R.id.profilePageEmailField);
        phoneNumField = (TextView) findViewById(R.id.profilePagePhoneNumberField);
        nameField = (TextView) findViewById(R.id.profilePageFullName);
        loggoutButton = (Button) findViewById(R.id.profilePageLogoutButton);




        unameField.setText("User Name: " + user.getProperty("userName").toString());
        phoneNumField.setText("Phone number: " + Utility.convertPhone(user.getProperty("phoneNumber").toString()));
        emailField.setText("Email: " + user.getEmail());
        nameField.setText("Full Name: " + user.getProperty("name"));
        loggoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void aVoid)
                    {
                        //logged out
                        Toast.makeText(profilePage.this, "Logging out...",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(profilePage.this, MainActivity.class));
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {

                    }
                });
            }
        });




    }

}
