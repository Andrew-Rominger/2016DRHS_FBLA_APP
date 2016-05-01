package fbla.com.fbla_app_src;

/**
 * Created by Andrew on 3/14/2016.
 */
public class Comment
{
    //Represnets one comment
    private String objectId;
    private String content;
    private String postid;
    private String useruploaded;
    private String dateUploaded;

    public String getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(String dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getUseruploaded() {
        return useruploaded;
    }

    public void setUseruploaded(String useruploaded) {
        this.useruploaded = useruploaded;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
