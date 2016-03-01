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

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

public class editprofilesettings extends AppCompatActivity {

    EditText name;
    EditText handle;
    EditText bio;
    Button done;
    ImageView back;
    String newName;
    String newHanle;
    String newBio;
    BackendlessUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofilesettings);

        name = (EditText) findViewById(R.id.editText);
        handle = (EditText) findViewById(R.id.editText3);
        bio = (EditText) findViewById(R.id.editText4);
        done = (Button) findViewById(R.id.donebutton);
        back = (ImageView) findViewById(R.id.back);
        user = Backendless.UserService.CurrentUser();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newName = name.getText().toString();
                newHanle = handle.getText().toString();
                newBio = bio.getText().toString();
                user.setProperty("userName", newName);




            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(editprofilesettings.this, accountsettings.class);
                startActivity(i);
            }
        });
    }

}
