package fbla.com.fbla_app_src;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;

import fbla.com.fbla_app_src.util;

public class accountsettings extends AppCompatActivity {

    FrameLayout editProfile;
    FrameLayout changePassword;
    FrameLayout privatePolicy;
    FrameLayout terms;
    Button logout;
    Button deleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);

        editProfile = (FrameLayout) findViewById(R.id.editProfile);
        changePassword = (FrameLayout) findViewById(R.id.changePassword);
        privatePolicy = (FrameLayout) findViewById(R.id.privatePolicy);
        terms = (FrameLayout) findViewById(R.id.terms);
        logout = (Button) findViewById(R.id.logoutButton);
        deleteAccount = (Button) findViewById(R.id.deleteAccountButton);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(accountsettings.this, editprofilesettings.class);
                startActivity(i);
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(accountsettings.this, passwordsettings.class);
                startActivity(i);
            }
        });
        privatePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
                Intent i = new Intent(accountsettings.this, MainActivity.class);
                startActivity(i);
            }
        });
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
    public void logOut()
    {
        Backendless.UserService.logout(new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void aVoid) {
                //user logged out
                Toast.makeText(accountsettings.this, "Logout Successful", Toast.LENGTH_LONG).show();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                //user falied to logout
                Toast.makeText(accountsettings.this, backendlessFault.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
