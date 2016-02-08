package fbla.com.fbla_app_src;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
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
import com.backendless.files.BackendlessFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

@SuppressLint("NewApi")
public class profilePage extends AppCompatActivity{

    BackendlessUser user;
    util Utility;

    ImageButton uploadPhoto;
    TextView numOfPosts;
    FrameLayout frameLayout;
    Button getLastImage;
    String passedOID;



    @Override
    public void onBackPressed()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Utility = new util();
        user = Backendless.UserService.CurrentUser();
        numOfPosts = (TextView) findViewById(R.id.profilepage_postNum);
        numOfPosts.setText(user.getProperty("numPosts").toString());
        uploadPhoto = (ImageButton) findViewById(R.id.profilePage_addPic);
        frameLayout = (FrameLayout) findViewById(R.id.profilepage_post);
        getLastImage = (Button) findViewById(R.id.getlastImage);

        frameLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        getLastImage.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Drawable d = util.getPictureFromPOID(Backendless.Data.of(Picture.class).findLast().getObjectId(),profilePage.this);
                set(d);
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
        uploadPhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moveToCamera();

            }
        });

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!data.equals(null))
        {
            Utility.saveImage(data, profilePage.this, false);
        }
    }


    public void moveToCamera()
    {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 1);
    }
    public void set(Drawable dr)
    {
        uploadPhoto.setBackground(dr);
    }



}
