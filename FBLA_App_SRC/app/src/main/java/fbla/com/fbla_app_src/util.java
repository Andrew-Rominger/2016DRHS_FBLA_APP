package fbla.com.fbla_app_src;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

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
    public static void cleanHouse()
    {

    }


}
