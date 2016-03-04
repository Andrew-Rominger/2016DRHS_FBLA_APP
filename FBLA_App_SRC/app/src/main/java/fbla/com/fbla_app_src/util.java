package fbla.com.fbla_app_src;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.io.BackendlessUserWriter;
import com.facebook.internal.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.support.v4.content.ContextCompat.startActivities;

/**
 * Created by Andrew on 2/3/2016.
 */
public class util
{
    static Picture image;
    static String URL;
    static Bitmap imageBmap;
    static Picture savedPic;
    static boolean logged;


    public String convertPhone(String preCon)
    {
        ArrayList<Character>  charArray = new ArrayList<Character>();
        for(int i = 0;i< preCon.length();i++)
        {
            charArray.add(preCon.charAt(i));
        }
        charArray.add(0,'(');
        charArray.add(4, ')');
        charArray.add(8, '-');
        String listString = "";
        for(Character c : charArray)
        {
            listString += c;
        }
        return listString;
    }
    public void saveProf(Intent data, Context context)
    {
        saveImage(data, context, true);
    }

    public static Picture saveImage(Intent data, Context context, Boolean isProfile)
    {
        final boolean isProfP = isProfile;
        final Context thisContext = context;
        final Intent datathis = data;
        String toReturn;

        final BackendlessUser user = Backendless.UserService.CurrentUser();
        final Picture image = new Picture();

        image.setFileLocation("/media/userpics");
        image.setUserID(user.getUserId());
        if(isProfile)
        {
            image.setIsProf(true);
        }
        else
        {
            image.setIsProf(false);
        }

        Backendless.Data.save(image, new AsyncCallback<Picture>() {
            @Override
            public void handleResponse(Picture imagePassed) {
                if (isProfP) {
                    user.setProperty("profPicID", imagePassed.getObjectId());
                    Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser backendlessUser) {

                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {

                        }
                    });
                }
                savedPic = imagePassed;
                uploadImage(getBitmapFromData(datathis), imagePassed, thisContext);

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                //Toast.makeText(thisContext, backendlessFault.getDetail(), Toast.LENGTH_LONG).show();
                Toast.makeText(thisContext, backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return savedPic;



    }
    public static void uploadImage(Bitmap bMap, Picture p, Context thisContextp)
    {
        final String OID = p.getObjectId()+".png";

        final Context thisContext = thisContextp;
        Backendless.Files.Android.upload(bMap, Bitmap.CompressFormat.PNG, 100, OID, "/media/userpics", new AsyncCallback<BackendlessFile>() {
            @Override
            public void handleResponse(BackendlessFile backendlessFile) {
                Toast.makeText(thisContext, OID + " Uploaded", Toast.LENGTH_LONG).show();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(thisContext, backendlessFault.getCode(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public static Bitmap getBitmapFromData(Intent data)
    {
        byte[] bArr = data.getByteArrayExtra("data");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bArr,0,bArr.length,options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        return null;

    }
    public static Drawable getPictureFromPOID(final String PictureOID, final Context thisContext)
    {
        Backendless.Data.of(Picture.class).findById(PictureOID, new AsyncCallback<Picture>() {
            @Override
            public void handleResponse(Picture picture) {
                image = picture;
                if (image != null) {
                    URL = "https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/" + image.getObjectId() + ".png";
                    imageBmap = getBitmapFromURL(URL);
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(thisContext, backendlessFault.getCode(), Toast.LENGTH_LONG).show();
            }
        });
        return getDrawablleFromBMap(imageBmap, thisContext);

    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Drawable getDrawablleFromBMap(Bitmap bmap, Context context)
    {
        return new BitmapDrawable(context.getResources(), imageBmap);
    }

    public static void signInUser(final String userName, final String password, final Context c) {
        final Context context = c;
        final Intent i = new Intent(c, profilePage.class);
            Backendless.UserService.login(userName, password, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser backendlessUser) {
                    Toast.makeText(context, userName + " logged in", Toast.LENGTH_LONG).show();

                    c.startActivity(i);
                    //Intent moveTo;
                    Log.i("logged in","sucessful");


                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {
                    Toast.makeText(context, userName + " did not login", Toast.LENGTH_LONG).show();


                }
            });


    }
    public void moveToActivity(Context context,  Intent i)
    {
       context.startActivity(i);
    }
    public static boolean checkForFirstTime(BackendlessUser user)
    {

        if((Boolean) user.getProperty("firstTimeLogin"))
        {
            user.setProperty("firstTimeLogin", false);
            updateUser(user);
            return true;
        }
        else
        {
            return false;
        }


    }
    public static void updateUser(BackendlessUser user)
    {
         Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
             @Override
             public void handleResponse(BackendlessUser backendlessUser) {

             }

             @Override
             public void handleFault(BackendlessFault backendlessFault) {

             }
         });
    }
    public static void setProperties(String phoneNumber, String fullName, BackendlessUser user, Context ct)
    {
        if(user != null)
        {
            if (phoneNumber.length() == 10) {
                user.setProperty("phoneNumber", phoneNumber);
            }
            if (!fullName.equals("")) {
                user.setProperty("name", fullName);
            }
            updateUser(user);
        }
        else
        {
            Toast.makeText(ct, "User was null", Toast.LENGTH_LONG).show();
        }

    }
    public static String convertNumber(String numIn, Context c)
    {

        if(numIn.length() == 10)
        {
            return numIn;
        }
        else
        {
            return Integer.toString(errorNum(c));
        }
    }
    public static int errorNum(Context context)
    {
        Toast.makeText(context, "Hmm I was not able to read your phone number. Please try again. You dont need to add dashes or a country code. Ex 7755551234", Toast.LENGTH_LONG).show();
        return -1;
    }
    public static void createNewPost(BackendlessUser user, String captionForImage, Picture imageFromCamera)
    {
        Post post = new Post();
        post.setCaption(captionForImage);
        post.setPictureOnPost(imageFromCamera);
        post.setUserUploaded(user);
        
    }
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        if(ratio >= 1.0F)
        {
            return  realImage;
        }
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
    public static boolean isPassword(String string)
    {

        String currentPassword = Backendless.UserService.CurrentUser().getPassword().toString();
        if(currentPassword == string)
        {
            return true;
        }
        else
        {
            return false;
        }

    }
    public static String shave(String string)
    {
        if(string.length()>8)
        {
            String endString = "";
            for(int i = 0; i < 9; i++)
            {
                char character = string.charAt(i);
                endString += character;
            }
            return endString;
        }
        if(string.length() == 8)
        {
            return string;
        }
        else
        {
            return null;
        }
    }

}
