package fbla.com.fbla_app_src;

import com.backendless.BackendlessUser;

import java.util.Date;

/**
 * Created by AromAdmin on 2/8/2016.
 */
public class Post
{
    private String tag;
    private int numFavorites;
    private int numDislikes;
    private int numLikes;
    private String Caption;
    private BackendlessUser userUploaded;
    private Picture pictureOnPost;
    private String objectId;
    private Date created;
    private int numComments;
    private String pictureOID;
    private String userUploadedS;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPictureOID() {
        return pictureOID;
    }

    public String getUserUploadedS()
    {
        return userUploadedS;
    }

    public void setUserUploadedS(String userUploadedS)
    {
        this.userUploadedS = userUploadedS;
    }

    public void setPictureOID(String pictureOID) {
        this.pictureOID = pictureOID;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getNumFavorites() {
        return numFavorites;
    }

    public void setNumFavorites(int numFavorites) {
        this.numFavorites = numFavorites;
    }

    public void setNumDislikes(int numDislikes) {
        this.numDislikes = numDislikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public int getNumDislikes() {
        return numDislikes;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public int getNumLikes() {
        return numLikes;
    }



    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public BackendlessUser getUserUploaded() {
        return userUploaded;
    }

    public void setUserUploaded(BackendlessUser userUploaded) {
        this.userUploaded = userUploaded;
    }

    public Picture getPictureOnPost() {
        return pictureOnPost;
    }

    public void setPictureOnPost(Picture pop) {
        pictureOnPost = pop;
    }
}
