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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;


@SuppressLint("NewApi")
public class profilePage extends AppCompatActivity{

    BackendlessUser user;
    TextView userName;
    ImageView uploadImage;
    ImageView settings;
    FrameLayout search;
    RelativeLayout bckg;
    FrameLayout add;
    Picture profPic;
    FrameLayout profile;


    @Override
    public void onBackPressed()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);


        user = Backendless.UserService.CurrentUser();
        profPic = new Picture();
        userName = (TextView) findViewById(R.id.profilePage_UserNameField);
        userName.setText(user.getProperty("userName").toString());
        uploadImage = (ImageView) findViewById(R.id.profilePage_addPic);
        settings = (ImageView) findViewById(R.id.settings);
        search = (FrameLayout) findViewById(R.id.profilepage_searchNav);
        add = (FrameLayout) findViewById(R.id.profilepage_addNav);
        profile = (FrameLayout) findViewById(R.id.profilepage_profileNav);
        bckg = (RelativeLayout) findViewById(R.id.mainBCKG);
        if(!(user.getProperty("coverPhotoID") == null))
        {
            bckg.setBackground(util.getPictureFromPOID(user.getProperty("coverPhotoID").toString(),profilePage.this));
        }
        if(!(user.getProperty("profilePictureID") == null))
        {
            uploadImage.setImageDrawable(util.getPictureFromPOID(user.getProperty("profilePictureID").toString(),profilePage.this));
        }

        uploadImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 1);

            }

        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profilePage.this, accountsettings.class);
                startActivity(i);
            }
        });

        //navigaion
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profilePage.this, mostupvotessearch.class);
                startActivity(i);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profilePage.this, uploadPostActivity.class);
                startActivity(i);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profilePage.this, profilePage.class);
                startActivity(i);
            }
        });

        }
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        Backendless.Persistence.save(profPic, new AsyncCallback<Picture>()
        {
            @Override
            public void handleResponse(Picture picture)
            {
                util.uploadImage(util.getBitmapFromData(data), picture, profilePage.this);
                user.setProperty("profilePictureID", picture.getObjectId());
                Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser backendlessUser)
                    {
                        uploadImage.setImageDrawable(util.getDrawablleFromBMap(util.getBitmapFromData(data), profilePage.this));
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault)
                    {

                    }
                });

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });
    }


}
