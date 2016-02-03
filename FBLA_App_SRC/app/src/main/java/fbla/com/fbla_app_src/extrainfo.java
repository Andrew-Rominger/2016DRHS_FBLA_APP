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

import com.backendless.BackendlessUser;

public class extrainfo extends AppCompatActivity {

    private TextView skip;
    private BackendlessUser user;
    private Button saveData;
    EditText fullName;
    EditText phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrainfo);
        user.setProperty("firstTimeLogin", false);


        skip = (TextView) findViewById(R.id.skipToFriends);
        saveData = (Button) findViewById(R.id.saveExtra);
        fullName = (EditText) findViewById(R.id.nameField);
        phoneNumber = (EditText) findViewById(R.id.phoneNumberField);
        /*skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipToFriends();
            }
        });
        */
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipToProf();
            }
        });
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                user.setProperty("name", fullName.getText().toString());
                user.setProperty("phoneNumber", phoneNumber.getText().toString());
            }
        });

    }
    public void skipToFriends()
    {
        Intent i = new Intent(extrainfo.this, findingfriends.class);
        startActivity(i);
    }
    public void skipToProf()
    {
        Intent i = new Intent(extrainfo.this, profilePage.class);
        startActivity(i);
    }

}
