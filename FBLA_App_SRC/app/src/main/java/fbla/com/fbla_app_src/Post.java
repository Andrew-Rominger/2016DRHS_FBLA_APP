package fbla.com.fbla_app_src;

import com.backendless.BackendlessUser;

/**
 * Created by AromAdmin on 2/8/2016.
 */
public class Post
{
    private int NumFavorites;
    private int NumDislikes;
    private int NumLikes;
    private String Caption;
    private BackendlessUser userUploaded;
    private Picture PictureOnPost;

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
        return PictureOnPost;
    }

    public void setPictureOnPost(Picture pictureOnPost) {
        PictureOnPost = pictureOnPost;
    }
}
