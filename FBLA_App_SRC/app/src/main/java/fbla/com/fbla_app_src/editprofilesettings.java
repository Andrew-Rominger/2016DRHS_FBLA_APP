package fbla.com.fbla_app_src;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.makeramen.roundedimageview.RoundedImageView;

public class editprofilesettings extends AppCompatActivity {
    // Declared global variables
    EditText name;
    EditText handle;
    EditText bio;
    Intent i;
    Button done;
    ImageView back;
    String newName;
    String newHanle;
    String newBio;
    BackendlessUser user;
    DownloadImageClass dlc = new DownloadImageClass();
    RoundedImageView riv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofilesettings);

        name = (EditText) findViewById(R.id.editTextName);
        handle = (EditText) findViewById(R.id.editTextHandle);
        bio = (EditText) findViewById(R.id.editText4);
        done = (Button) findViewById(R.id.donebutton);
        back = (ImageView) findViewById(R.id.back);
        user = Backendless.UserService.CurrentUser();
        name.setHint(user.getProperty("name").toString());
        handle.setHint(user.getProperty("userName").toString());
        bio.setHint(user.getProperty("Bio").toString());
        riv = (RoundedImageView) findViewById(R.id.editProfPage_Picture);
        i = getIntent();
        if(!(user.getProperty("profilePictureID") == null))
        {
            riv.setVisibility(View.INVISIBLE);
            Log.i("userHas", "User Has profpic");
            dlc.setImageView(riv);
            dlc.execute("https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/" + user.getProperty("profilePictureID") + ".png");

            riv.setVisibility(View.VISIBLE);
        }

        if (i.getBooleanExtra("fromProf", false))
        {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(editprofilesettings.this, profilePage.class);
                    startActivity(i);
                }
            });
        }
        else
        {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(editprofilesettings.this, accountsettings.class);
                    startActivity(i);
                }
            });
        }
        done.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                newName = name.getText().toString();
                newHanle = handle.getText().toString();
                newBio = bio.getText().toString();
                user.setProperty("name", newName);
                user.setProperty("userName", newHanle);
                user.setProperty("Bio", newBio);
                Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser backendlessUser) {
                        Toast.makeText(editprofilesettings.this, "Profile Updated", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(editprofilesettings.this, "Failed to update profile", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

}
