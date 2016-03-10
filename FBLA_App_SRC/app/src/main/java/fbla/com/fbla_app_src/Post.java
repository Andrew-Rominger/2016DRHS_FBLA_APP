package fbla.com.fbla_app_src;

import com.backendless.BackendlessUser;

import java.util.Date;

/**
 * Created by AromAdmin on 2/8/2016.
 */
public class Post
{
    // Declared global variables
    private int NumFavorites;
    private int NumDislikes;
    private int NumLikes;
    private String Caption;
    private BackendlessUser userUploaded;
    private Picture pictureOnPost;
    private String objectId;
    private Date created;

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
        return NumFavorites;
    }

    public void setNumFavorites(int numFavorites) {
        NumFavorites = numFavorites;
    }

    public int getNumDislikes() {
        return NumDislikes;
    }

    public void setNumDislikes(int numDislikes) {
        NumDislikes = numDislikes;
    }

    public int getNumLikes() {
        return NumLikes;
    }

    public void setNumLikes(int numLikes) {
        NumLikes = numLikes;
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
