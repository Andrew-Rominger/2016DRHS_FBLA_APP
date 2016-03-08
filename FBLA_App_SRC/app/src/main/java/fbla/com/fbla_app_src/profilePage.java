package fbla.com.fbla_app_src;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressLint("NewApi")
public class profilePage extends AppCompatActivity{

    BackendlessUser user;
    TextView userName;
    ImageView uploadImage;
    ImageView settings;
    FrameLayout search;
    RelativeLayout bckg;
    Bitmap myBitmap;
    RoundedImageView riv;
    static ProgressBar loadingSpinner;
    FrameLayout add;
    Picture profPic;
    Picture coverPhoto;
    FrameLayout profile;
    TextView bio;
    TextView uploadCoverPhoto;
    DownloadImageClass downloadProf = new DownloadImageClass();
    DownloadImageClass downloadCover = new DownloadImageClass();
    private String pictureImagePath = "";


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
        coverPhoto = new Picture();
        riv = (RoundedImageView) findViewById(R.id.profilePage_addPic);
        userName = (TextView) findViewById(R.id.profilePage_UserNameField);
        userName.setText(user.getProperty("userName").toString());
        uploadImage = (ImageView) findViewById(R.id.profilePage_addPic);
        settings = (ImageView) findViewById(R.id.settings);
        search = (FrameLayout) findViewById(R.id.profilepage_searchNav);
        bio = (TextView) findViewById(R.id.profilepage_bio);
        loadingSpinner = (ProgressBar) findViewById(R.id.lodingProfSpinner);
        add = (FrameLayout) findViewById(R.id.profilepage_addPosNav);
        uploadCoverPhoto = (TextView) findViewById(R.id.addCoverPhoto);
        profile = (FrameLayout) findViewById(R.id.profilepage_profileNav);
        bckg = (RelativeLayout) findViewById(R.id.mainBCKG);



        if(!(user.getProperty("coverPhotoID") == null))
        {
            downloadCover.setRelativeLayout(bckg);
            downloadCover.execute("https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/" + user.getProperty("coverPhotoID") + ".png");
            uploadCoverPhoto.setText("");
            showSpinner();
        }
        if(!(user.getProperty("profilePictureID") == null))
        {
            showSpinner();
            uploadImage.setVisibility(View.INVISIBLE);
            //Log.i("userHas", "User Has profpic");
            downloadProf.setImageView(uploadImage);
            downloadProf.execute("https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/" + user.getProperty("profilePictureID") + ".png");
            uploadImage.setVisibility(View.VISIBLE);
        }
        else
        {
            hideSpinner();
        }

        if(!user.getProperty("Bio").equals("No Bio Set"))
        {
            bio.setText(user.getProperty("Bio").toString());
        }
        else
        {
            bio.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(profilePage.this, editprofilesettings.class);
                    i.putExtra("fromProf", true);
                    startActivity(i);
                }
            });
        }

        uploadImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                openBackCamera(1);
            }
        });


        uploadCoverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openBackCamera(2);
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
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 3);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        if (requestCode == 1) {
            File imgFile = new  File(pictureImagePath);
            //Log.i("PRINT IMGFILE", imgFile.getAbsolutePath());
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);
            Matrix matrix = new Matrix();
            matrix.setRotate(90);

            final Bitmap result = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
            uploadImage.setImageDrawable(new BitmapDrawable(getResources(), result));
            Backendless.Persistence.save(profPic, new AsyncCallback<Picture>() {
                @Override
                public void handleResponse(Picture picture)
                {
                    util.uploadImage(result, picture, profilePage.this);
                    user.setProperty("profilePictureID", picture.getObjectId());

                    Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>()
                    {
                        @Override
                        public void handleResponse(BackendlessUser backendlessUser) {
                            Log.i("setImageD", "SetD");
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {

                        }
                    });

                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {

                }
            });
        }
        else if(requestCode == 2)
        {
            File imgFile = new  File(pictureImagePath);
            Log.i("PRINT IMGFILE", imgFile.getAbsolutePath());
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            final Bitmap result = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);

            bckg.setBackground(new BitmapDrawable(getResources(), result));

            Backendless.Persistence.save(coverPhoto, new AsyncCallback<Picture>()
            {
                @Override
                public void handleResponse(Picture picture)
                {
                    Log.i("LOL", "MADE IT BRO");
                    util.uploadImage(result, picture, profilePage.this);
                    user.setProperty("coverPhotoID", picture.getObjectId());
                    Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>()
                    {
                        @Override
                        public void handleResponse(BackendlessUser backendlessUser)
                        {
                            Log.i("setImageD", "hjfdshjkfdhjkdfsdfsjkh");
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
        else if(requestCode == 3)
        {
            Intent i = new Intent(profilePage.this,uploadPostActivity.class);
            i.putExtra("passedPictureData", data);
            i.putExtra("IP", pictureImagePath);
            startActivity(i);
        }
    }
    public static void showSpinner(){loadingSpinner.setVisibility(View.VISIBLE);}
    public static void hideSpinner(){loadingSpinner.setVisibility(View.INVISIBLE);}


    private void openBackCamera(int numCode)
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".png";

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, numCode);
    }


}
