package fbla.com.fbla_app_src;

import android.content.Intent;
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
    private Button EmailSU;

    BackendlessUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String appVersion = "v1";
        Backendless.initApp(this, "67BF989E-7E10-5DB8-FFD7-C9147CA4F200", "12F047DB-382A-F6DA-FF16-C6A0A1F0CE00", appVersion);
        EmailSU = (Button) findViewById(R.id.signupEmailButton);
        EmailSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               moveToEmailSignUp();
            }
        });

    }
    public void moveToEmailSignUp()
    {
        Intent moveToSU = new Intent(MainActivity.this, login_activity.class);
        startActivity(moveToSU);
    }




}


