package fbla.com.fbla_app_src;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
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

public class profilePage extends AppCompatActivity {

    BackendlessUser user;
    util Utility;


    Button loggoutButton;
    ImageView uploadPhoto;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        util.cleanHouse();
        System.exit(0);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Utility = new util();
        user = Backendless.UserService.CurrentUser();

        uploadPhoto = (ImageView) findViewById(R.id.profilePage_addPic);


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
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToCamera();
            }
        });

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        String name = user.getObjectId().toString() + "_" + user.getProperty("picNum");
        String location = "/media/userpics";

        Toast.makeText(this, "File uploaded to " + location + " as " + name, Toast.LENGTH_LONG).show();





    }

    public void moveToCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }
    public void uploadImage(Intent data, String fileName, String location)
    {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.WEBP, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        Backendless.Files.saveFile(location, fileName + ".webp", bitmapdata, new AsyncCallback<String>() {
            @Override
            public void handleResponse(String s) {
                Toast.makeText(profilePage.this, s, Toast.LENGTH_LONG).show();
                user.setProperty("picNum", ((Integer) user.getProperty("picNum") + 1));
                Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser backendlessUser) {

                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {

                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(profilePage.this, backendlessFault.getCode(), Toast.LENGTH_LONG).show();
            }
        });

    }



}
