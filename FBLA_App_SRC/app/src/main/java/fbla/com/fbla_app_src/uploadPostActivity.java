package fbla.com.fbla_app_src;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.Suppress;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class uploadPostActivity extends AppCompatActivity {
    Post post;
    BackendlessUser user;
    ImageView placeHolder;
    Intent data;
    Picture pObject;
    Picture image;
    Picture image2;
    EditText caption;
    Button shareButton;

    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);
        user = Backendless.UserService.CurrentUser();
        placeHolder = (ImageView) findViewById(R.id.placeholder);
        caption = (EditText) findViewById(R.id.uploadPost_Caption);
        shareButton = (Button) findViewById(R.id.uploadPost_shareButton);
        post = new Post();
        image = new Picture();
        extras = getIntent().getExtras();
        if(extras.get("passedPictureData")!=null)
        {
            data = (Intent) extras.get("passedPictureData");
            handleImage(data);
            post.setUserUploaded(user);
            saveImage(data, uploadPostActivity.this, false);
        }
        else
        {
            Log.i("ERROR:EXTRA NULL", "EXTRA WAS NULL");
        }



        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                 if(caption.getText().toString().isEmpty())
                 {
                     post.setCaption("");
                 }
                else
                 {
                     post.setCaption(caption.getText().toString());
                 }
                if(post.getPictureOnPost() != null)
                {
                    Backendless.Data.save(post, new AsyncCallback<Post>()
                    {
                        @Override
                        public void handleResponse(Post postlol)
                        {
                            Toast.makeText(uploadPostActivity.this, "Post " + postlol.getObjectId() + " uploaded succesfully!", Toast.LENGTH_LONG).show();
                            int numPosts = Integer.parseInt(user.getProperty("numPosts").toString());
                            user.setProperty("numPosts", numPosts++);
                            util.updateUser(user);
                            startActivity(new Intent(uploadPostActivity.this, profilePage.class));
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {

                        }
                    });
                }

            }
        });


    }

    @SuppressLint("NewApi")
    public void handleImage(Intent data)
    {
        Bitmap newbm = util.scaleDown((Bitmap) data.getExtras().get("data"), 1.0F, true);
        Drawable dr = new BitmapDrawable(getResources(), newbm);
        placeHolder.setImageDrawable(dr);
    }

    public void saveImage(Intent data, Context context, Boolean isProfile)
    {
        final boolean isProfP = isProfile;
        final Context thisContext = context;
        final Intent datathis = data;
        String toReturn;
        final BackendlessUser user = Backendless.UserService.CurrentUser();

        if(isProfile)
        {
            image.setIsProf(true);
        }
        else
        {
            image.setIsProf(false);
        }

        Backendless.Persistence.save(image, new AsyncCallback<Picture>()
        {
            @Override
            public void handleResponse(final Picture imagePassed)
            {
                util.uploadImage(util.getBitmapFromData(datathis), imagePassed, thisContext);
                Log.i("imagetest", image.getObjectId());
                image.setObjectId(imagePassed.getObjectId());
                Log.i("imagetest2", image.getObjectId());
                image.setFileLocation("/media/userpics");
                Log.i("imagetest3", image.getObjectId());
                image.setUserID(user.getUserId());
                Log.i("imagetest4", image.getObjectId());
                post.setPictureOnPost(image);


            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                //Toast.makeText(thisContext, backendlessFault.getDetail(), Toast.LENGTH_LONG).show();
                Toast.makeText(thisContext, backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }

}
