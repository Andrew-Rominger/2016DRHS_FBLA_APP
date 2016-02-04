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

        unameField = (TextView) findViewById(R.id.profilePage_userNameContent);
        emailField = (TextView) findViewById(R.id.profilePage_emailContnent);
        phoneNumField = (TextView) findViewById(R.id.profilePage_phoneNumberContent);
        nameField = (TextView) findViewById(R.id.profilePage_nameContent);
        loggoutButton = (Button) findViewById(R.id.profilePageLogoutButton);




        unameField.setText(user.getProperty("userName").toString());
        phoneNumField.setText(Utility.convertPhone(user.getProperty("phoneNumber").toString()));
        emailField.setText(user.getEmail());
        nameField.setText(user.getProperty("name").toString());
        loggoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(profilePage.this, "Logging out...", Toast.LENGTH_LONG).show();
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void aVoid) {
                        //logged out

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
