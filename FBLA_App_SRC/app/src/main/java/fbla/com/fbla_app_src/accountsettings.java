package fbla.com.fbla_app_src;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;

import fbla.com.fbla_app_src.util;

public class accountsettings extends AppCompatActivity {
    // Declared global variables
    FrameLayout editProfile;
    FrameLayout changePassword;
    FrameLayout privatePolicy;
    FrameLayout terms;
    Dialog log;
    Intent i;
    Button logout;
    ImageView goBack;
    BackendlessUser user;
    Button deleteAccount;
    final IDataStore<BackendlessUser> dataStore = Backendless.Data.of( BackendlessUser.class );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = Backendless.UserService.CurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);


        editProfile = (FrameLayout) findViewById(R.id.editProfile);
        changePassword = (FrameLayout) findViewById(R.id.changePassword);
        privatePolicy = (FrameLayout) findViewById(R.id.privatePolicy);
        terms = (FrameLayout) findViewById(R.id.terms);
        goBack = (ImageView) findViewById(R.id.backArrowImageView);
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
                startActivity(new Intent(accountsettings.this, privatepolicy.class));
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               startActivity(new Intent(accountsettings.this, profilePage.class));
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(accountsettings.this, termsandconditions.class));
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
            public void onClick(View v)
            {
                dialogBox();


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
    public void dialogBox() {
        final Intent i = new Intent(accountsettings.this, MainActivity.class);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete your account? This is not reversable");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        dataStore.findById(Backendless.UserService.CurrentUser().getObjectId(), new AsyncCallback<BackendlessUser>()
                        {
                            @Override
                            public void handleResponse(BackendlessUser backendlessUser)
                            {
                                dataStore.remove(Backendless.UserService.CurrentUser(), new AsyncCallback<Long>()
                                {
                                    @Override
                                    public void handleResponse(Long aLong)
                                    {
                                        logOut();
                                        Toast.makeText(accountsettings.this, "Account Deleted", Toast.LENGTH_LONG).show();
                                        startActivity(i);


                                    }

                                    @Override
                                    public void handleFault(BackendlessFault backendlessFault)
                                    {

                                    }
                                });
                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault)
                            {

                            }

                        });
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        arg0.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void dismissDialog(Dialog log)
    {
        log.dismiss();
    }
}
