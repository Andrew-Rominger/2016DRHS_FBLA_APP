package fbla.com.fbla_app_src;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
    EditText editImage;
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

    RoundedImageView riv;

    //Make an instance of the DownloadImageClass, which is used to download a single image in the background
    DownloadImageClass dlc = new DownloadImageClass();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofilesettings);
        setupUI(findViewById(R.id.editProfileBackground));
        //get views
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

        //check if the user has a profile picture in the database
        if(!(user.getProperty("profilePictureID") == null))
        {
            //user has profile picture,

            //Hide upload image button
            riv.setVisibility(View.GONE);
            //set the image view the the downloader will eventually set the profile picture to be
            dlc.setImageView(riv);
            //Executes the download with the provided user profile picure ID
            dlc.execute("https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/" + user.getProperty("profilePictureID") + ".png");
            //show profile picture
            riv.setVisibility(View.VISIBLE);
        }
        //sets the behavior of the back arrow
        if (i.getBooleanExtra("fromProf", false))
        {
            //user is coming from the profile page, should move them back
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
            //user is coming from the account settings page, should move them there
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(editprofilesettings.this, accountsettings.class);
                    startActivity(i);
                }
            });
        }
        //declares a method to be called when the user has clicked the done button
        done.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //gets the inputted date
                newName = name.getText().toString();
                newHanle = handle.getText().toString();
                newBio = bio.getText().toString();
                if(newName == "" || newBio == "") {
                    if (Validator.isUserNameValid(newName)) {
                        user.setProperty("name", newName);
                    }
                    user.setProperty("userName", newHanle);
                }
                user.setProperty("Bio", newBio);
                //attempts to update the user
                Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser backendlessUser) {
                        Toast.makeText(editprofilesettings.this, "Profile Updated", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(editprofilesettings.this, backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
    //this method hides the keyboard
    public static void hideSoftKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    //this method checks to see if the user has clicked outside of the edit text when the key board is up, if they do the hideSoftKeyboard() method is called
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(editprofilesettings.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

}
