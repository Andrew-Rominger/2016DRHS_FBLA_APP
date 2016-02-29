package fbla.com.fbla_app_src;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.facebook.internal.Utility;

public class extrainfo extends AppCompatActivity {

    private TextView skip;
    BackendlessUser user;
    private Button saveData;
    String convtPhoneNum;
    EditText fullName;
    PopupMenu popup;
    EditText phoneNumber;
    util Utility;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrainfo);

        user = Backendless.UserService.CurrentUser();

        skip = (TextView) findViewById(R.id.skipToFriends);
        saveData = (Button) findViewById(R.id.saveExtra);
        fullName = (EditText) findViewById(R.id.nameField);
        phoneNumber = (EditText) findViewById(R.id.phoneNumberField);
        Utility = new util();




        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToProf(user);
            }
        });
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String pn = phoneNumber.getText().toString();
                String fn = fullName.getText().toString();
                util.setProperties(pn,fn,user, extrainfo.this);
                moveToProf(user);

            }
        });

    }

    public void moveToProf(BackendlessUser user)
    {
        startActivity(new Intent(extrainfo.this, profilePage.class));
    }


}
