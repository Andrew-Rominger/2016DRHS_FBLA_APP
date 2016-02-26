package fbla.com.fbla_app_src;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import android.os.Bundle;

import android.provider.MediaStore;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;


@SuppressLint("NewApi")
public class profilePage extends AppCompatActivity{

    BackendlessUser user;
    util Utility;

    TextView userName;
    ImageView uploadImage;
    ImageView settings;


    @Override
    public void onBackPressed()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Utility = new util();
        user = Backendless.UserService.CurrentUser();
        userName = (TextView) findViewById(R.id.profilePage_UserNameField);
        userName.setText(user.getProperty("userName").toString());
        uploadImage = (ImageView) findViewById(R.id.profilePage_addPic);
        settings = (ImageView) findViewById(R.id.settings);
        uploadImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(profilePage.this, yourPosts.class));
            }

        });

        /*
        loggoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(profilePage.this, "Logging out...", Toast.LENGTH_LONG).show();
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void aVoid) {
                        //logged out

                        startActivity(new Intent(profilePage.this, MainActivity.class));
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {

                    }
                });
            }
        });
        */

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profilePage.this, editprofilesettings.class);
                startActivity(i);
            }
        });

        }

}
