package fbla.com.fbla_app_src;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Andrew on 2/6/2016.
 */
public class Picture
{
    //public String Caption;
    public String originalFileName;
    private String objectId;
    private String userID;

    public String getObjectId()
    {
        return objectId;
    }

    public void setObjectId( String objectId )
    {
        this.objectId = objectId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    /*
    public String getCaption()
    {
        return Caption;
    }

    public void setCaption(String caption)
    {
        Caption = caption;
    }
    */

    public String getOriginalFileName()
    {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName)
    {
        this.originalFileName = originalFileName;
    }
    public void setparams(String originalFilename, String userIDPass)
    {
        //setCaption(caption);
        setUserID(userIDPass);
        setOriginalFileName(originalFilename);
    }
}
