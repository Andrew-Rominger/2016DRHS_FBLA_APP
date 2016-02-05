package fbla.com.fbla_app_src;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import org.w3c.dom.Text;

import java.io.File;
import java.net.URI;

public class profilePage extends AppCompatActivity {

    BackendlessUser user;
    TextView unameField;
    TextView emailField;
    TextView phoneNumField;
    TextView nameField;
    Button loggoutButton;
    Button uploadPhoto;
    util Utility;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Utility = new util();
        user = Backendless.UserService.CurrentUser();

        unameField = (TextView) findViewById(R.id.profilePage_userNameContent);
        emailField = (TextView) findViewById(R.id.profilePage_emailContnent);
        phoneNumField = (TextView) findViewById(R.id.profilePage_phoneNumberContent);
        nameField = (TextView) findViewById(R.id.profilePage_nameContent);
        loggoutButton = (Button) findViewById(R.id.profilePageLogoutButton);
        uploadPhoto = (Button) findViewById(R.id.profilePage_uploadButton);





        unameField.setText(user.getProperty("userName").toString());
        phoneNumField.setText(Utility.convertPhone(user.getProperty("phoneNumber").toString()));
        emailField.setText(user.getEmail());
        nameField.setText(user.getProperty("name").toString());
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
        String realPath;

            if (resultCode == Activity.RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(this, "Cancled", Toast.LENGTH_LONG).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
            }
            if(Build.VERSION.SDK_INT < 11) {
                realPath = Utility.getRealPathFromURI_BelowAPI11(this, data.getData());
            }
            else if(Build.VERSION.SDK_INT < 19)
            {
                realPath = Utility.getRealPathFromURI_API11to18(this, data.getData());
            }
            else
            {
                realPath = Utility.getRealPathFromURI_API19(this, data.getData());
            }

            try{
                Backendless.Files.upload(new File(realPath, user.getProperty("userName").toString() + "_" + user.getProperty("picNum")),"root\\media\\userpics");
            }
            catch(Exception e)
            {
                System.out.println(e);
            }






    }
    public void moveToCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }



}
