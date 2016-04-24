package fbla.com.fbla_app_src;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

/**
 * Created by Andrew on 2/3/2016.
 *
 */


//GENERAL UTILITY CLASS
public class util
{
    // Declared global variables

    static String src;
    static Bitmap imageBmap;
    static Drawable draw;
    static Bitmap bMAP;
    public static DownloadImageClass dlc = new DownloadImageClass();

    //uploads an image to the data vase, related to a picture object
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

    public static void uploadImageWCheck(Bitmap bMap, Picture p, Context thisContextp)
    {
        final String OID = p.getObjectId();

        final Context thisContext = thisContextp;

        Backendless.Files.Android.upload(bMap, Bitmap.CompressFormat.PNG, 100, OID + ".png", "/media/userpics", new AsyncCallback<BackendlessFile>()
        {
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
    //updates inputted user
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
    //sets a users properties to inputted data
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
    //prints an error for the phone number error
    public static int errorNum(Context context)
    {
        Toast.makeText(context, "Hmm I was not able to read your phone number. Please try again. You dont need to add dashes or a country code. Ex 7755551234", Toast.LENGTH_LONG).show();
        return -1;
    }
    //shaves date to readable format
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

