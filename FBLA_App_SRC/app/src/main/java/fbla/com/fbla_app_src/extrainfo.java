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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class extrainfo extends AppCompatActivity {

    private TextView skip;
    BackendlessUser user;
    private Button saveData;
    String convtPhoneNum;
    EditText fullName;
    PopupMenu popup;
    EditText phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrainfo);
        user = Backendless.UserService.CurrentUser();
        Toast.makeText(extrainfo.this, user.getProperty("userName").toString() + " logged in", Toast.LENGTH_LONG).show();
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
            public void onClick(View v) {
                convtPhoneNum = convertNumber(phoneNumber.getText().toString());
                if (convtPhoneNum == null || convtPhoneNum.equals("-1")) {
                    errorNum();
                    phoneNumber.setText("");
                } else {
                    setProperties();

                }

            }
        });

    }
    public void setProperties()
    {
        user.setProperty("name", fullName.getText().toString());
        user.setProperty("phoneNumber", convtPhoneNum);
        Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser backendlessUser) {

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });
        skipToProf();
    }
    public void skipToProf()
    {
        Intent i = new Intent(extrainfo.this, profilePage.class);
        startActivity(i);
    }
    public String convertNumber(String numIn)
    {

        if(numIn.length() == 10)
        {
         return numIn;
        }
        else
        {
         return Integer.toString(errorNum());
        }
    }
    public int errorNum()
    {
        Toast.makeText(extrainfo.this, "Hmm I was not able to read your phone number. Please try again. You dont need to add dashes or a country code. Ex 7755551234", Toast.LENGTH_LONG).show();
        return -1;
    }

}
