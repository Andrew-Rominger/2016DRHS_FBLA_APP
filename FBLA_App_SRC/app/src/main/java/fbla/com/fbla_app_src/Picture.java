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
    public String objectId;
    public String userID;
    public String fileLocation;
    public boolean isProf;

    public boolean isProf()
    {
        return isProf;
    }

    public void setIsProf(boolean isProf) {
        this.isProf = isProf;
    }
    //public String ownerID;
    /*
    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }
    */

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

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
}
