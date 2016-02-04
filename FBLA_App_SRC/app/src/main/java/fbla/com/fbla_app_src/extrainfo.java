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

public class extrainfo extends AppCompatActivity {

    private TextView skip;
    EditText phoneNumber;
    EditText name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrainfo);

        phoneNumber = (EditText) findViewById(R.id.phoneNumberField);
        name = (EditText) findViewById(R.id.nameField);

        skip = (TextView) findViewById(R.id.skipToFriends);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipToFriends();
            }
        });

    }
    public void skipToFriends()
    {
        Intent i = new Intent(extrainfo.this, findingfriends.class );
        startActivity(i);
    }

}
