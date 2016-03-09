package fbla.com.fbla_app_src;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Andrew on 2/3/2016.
 */
public class util
{
    static Picture image;
    static String src;
    static Bitmap imageBmap;
    static Drawable draw;
    static Bitmap bMAP;
    static Picture savedPic;
    static boolean logged;
    public static DownloadImageClass dlc = new DownloadImageClass();



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


    public static void uploadImage(Bitmap bMap, Picture p, Context thisContextp)
    {
        final String OID = p.getObjectId();

        final Context thisContext = thisContextp;

        Backendless.Files.Android.upload(bMap, Bitmap.CompressFormat.PNG, 100, OID + ".png", "/media/userpics", new AsyncCallback<BackendlessFile>() {
            @Override
            public void handleResponse(BackendlessFile backendlessFile)
            {
                Toast.makeText(thisContext, OID + ".png Uploaded", Toast.LENGTH_LONG).show();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(thisContext, backendlessFault.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    public static Bitmap getBitmapFromData(Intent data)
    {
        Bitmap bmp = (Bitmap) data.getExtras().get("data");
        return bmp;

    }
    public static Drawable getPictureFromPOID(final String PictureOID, final Context thisContext)
    {
        Backendless.Data.of(Picture.class).findById(PictureOID, new AsyncCallback<Picture>()
        {
            @Override
            public void handleResponse(Picture picture)
            {
                src = "https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/" + picture.getObjectId() + ".png";
                dlc.execute(src);
                draw = dlc.getDrawAble();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault)
            {
                Toast.makeText(thisContext, backendlessFault.getCode(), Toast.LENGTH_LONG).show();
            }
        });
        return draw;

    }
    public static Bitmap getBitmapFromURL(final String src)
    {
        try
        {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bMAP = BitmapFactory.decodeStream(input);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return bMAP;
    }
    public static Drawable getDrawablleFromBMap(Bitmap bmap, Context context)
    {
        return new BitmapDrawable(context.getResources(), imageBmap);
    }

    public static void signInUser(final String userName, final String password, final Context c)
    {
        final Context context = c;
        final Intent i = new Intent(c, profilePage.class);
            Backendless.UserService.login(userName, password, new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser backendlessUser) {
                    Toast.makeText(context, userName + " logged in", Toast.LENGTH_LONG).show();

                    c.startActivity(i);
                    //Intent moveTo;
                    Log.i("logged in", "sucessful");


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
        /*
        float ratio = Math.min
                (
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        if(ratio >= 1.0F)
        {
            return  realImage;
        }
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());
        Log.i("HEIGHT", String.valueOf(height));
        Log.i("WIDTH", String.valueOf(width));

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
        */
        return Bitmap.createScaledBitmap(realImage,(int)(realImage.getWidth()*maxImageSize), (int)(realImage.getHeight()*maxImageSize), true);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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

