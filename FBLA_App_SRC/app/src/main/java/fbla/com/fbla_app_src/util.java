package fbla.com.fbla_app_src;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.io.BackendlessUserWriter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Andrew on 2/3/2016.
 */
public class util
{
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

    public void uploadImage(Intent data, Context context)
    {
        final Context thisContext = context;
        final BackendlessUser user = Backendless.UserService.CurrentUser();
        final Intent datathis = data;
        Picture image = new Picture();
        image.setparams("", user.getObjectId());
        Backendless.Data.save(image, new AsyncCallback<Picture>() {
            @Override
            public void handleResponse(Picture picture)
            {
                Backendless.Files.Android.upload(getBitmapFromData(datathis), Bitmap.CompressFormat.PNG, 100, picture.getObjectId(), "/media/userpics", new AsyncCallback<BackendlessFile>() {
                    @Override
                    public void handleResponse(final BackendlessFile backendlessFile) {
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(thisContext, backendlessFault.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(thisContext, backendlessFault.getDetail(), Toast.LENGTH_LONG).show();
                Toast.makeText(thisContext, backendlessFault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    public Bitmap getBitmapFromData(Intent data)
    {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bitmapdata = bos.toByteArray();
        photo.compress(Bitmap.CompressFormat.PNG, 100 /*ignord for PNG*/, bos);

        return photo;
    }



}
