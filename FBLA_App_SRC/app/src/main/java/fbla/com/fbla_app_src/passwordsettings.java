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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.facebook.internal.Utility;

import fbla.com.fbla_app_src.util;


public class passwordsettings extends AppCompatActivity {

    ImageView back;
    Button done;
    EditText currentPassword;
    EditText anotherPassword;
    EditText anotherPasswordTwo;
    String theCurrentPassword;
    String theAnotherPassword;
    String theAnotherPasswordTwo;
    BackendlessUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordsettings);

        back = (ImageView) findViewById(R.id.imageView11);
        done = (Button) findViewById(R.id.donebutton);
        currentPassword = (EditText) findViewById(R.id.editText5);
        anotherPassword = (EditText) findViewById(R.id.editText6);
        anotherPasswordTwo = (EditText) findViewById(R.id.editText7);

        theCurrentPassword = currentPassword.getText().toString();
        theAnotherPassword = anotherPassword.getText().toString();
        theAnotherPasswordTwo = anotherPasswordTwo.getText().toString();

        user = Backendless.UserService.CurrentUser();



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calls a util method that checks if the password that is inputed by the user is his password
                if(util.isPassword(theCurrentPassword))
                {
                    if(theAnotherPassword == theAnotherPasswordTwo)
                    {
                        user.setPassword(theAnotherPassword);
                        Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser backendlessUser) {
                                Toast.makeText(passwordsettings.this, "Success", Toast.LENGTH_LONG);
                                goBack();
                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {
                                Toast.makeText(passwordsettings.this, "Failed to reset password", Toast.LENGTH_LONG);
                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(passwordsettings.this, "Passwords do not Match!", Toast.LENGTH_LONG);
                    }
                }
                else
                {
                    Toast.makeText(passwordsettings.this, "Incorrect Password", Toast.LENGTH_LONG);
                }
            }
        });
        //sets on click for going back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }
    //takes user back to profile page
    public void goBack()
    {
        Intent i = new Intent(passwordsettings.this, profilePage.class);
        startActivity(i);
    }

}
