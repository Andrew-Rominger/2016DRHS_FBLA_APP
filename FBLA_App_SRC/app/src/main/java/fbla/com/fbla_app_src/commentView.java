package fbla.com.fbla_app_src;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class commentView extends AppCompatActivity
{
    ListView commentListView;
    ArrayList<Comment> commentArrayList = new ArrayList<>();
    Post post;
    static ArrayList<Drawable> draw;
    public DownloadImagesClass DLC = new DownloadImagesClass();
    public ArrayList<String> URLS = new ArrayList<String>();
    ArrayAdapter adapter;
    commentView g = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setContentView(R.layout.activity_comment_view);
        Bundle b = i.getExtras();
        String postOID = b.getString("postID");
        Backendless.Data.of(Post.class).findById(postOID, new AsyncCallback<Post>() {
            @Override
            public void handleResponse(Post postF) {
                post = postF;
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });
        adapter = new MyListAdapter();

        commentListView = (ListView) findViewById(R.id.commentListView);



    }
    public ArrayList<String> getURLSCOMMENT(ArrayList<Post> postArray)
    {

        for (Post post : postArray)
        {
            URLS.add("https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/"+ post.getPictureOID() +".png");
            Log.i("ADDEDED", "https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/" + post.getPictureOID() + ".png");

        }
        return URLS;
    }

    public ArrayList<String> getURLS(ArrayList<Comment> commentArray)
    {
        for(Comment comment:commentArray)
        {
            URLS.add("https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/"+ post.getPictureOID() +".png");
        }
        return URLS;
    }
    public void getFirstPage()
    {
        AsyncCallback<BackendlessCollection<Comment>> callback = new AsyncCallback<BackendlessCollection<Comment>>()
        {
            @Override
            public void handleResponse(BackendlessCollection<Comment> CommentBackendlessCollection)
            {
                Log.i("GotPosts", "GotPosts");
                List<Comment> firstPage = CommentBackendlessCollection.getCurrentPage();
                Iterator<Comment> it = firstPage.iterator();
                while (it.hasNext())
                {
                    Comment comment = it.next();
                    Log.i("POST ID", post.getObjectId());
                    commentArrayList.add(comment);
                }
                DLC.setDlList(draw);
                DLC.setFromC(g);
                DLC.execute(getURLS(commentArrayList));
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        };

        int pageSize = 10;
        BackendlessDataQuery query = new BackendlessDataQuery();
        QueryOptions qo = new QueryOptions();
        qo.addSortByOption("created desc");
        query.setPageSize(pageSize);
        query.setQueryOptions(qo);
        Backendless.Data.of(Comment.class).find(query, callback);
    }
    public void populateListView() throws InterruptedException
    {
        commentListView.setAdapter(adapter);
    }
    public class MyListAdapter extends ArrayAdapter<Comment>
    {
        public MyListAdapter()
        {
            super(commentView.this, R.layout.layout_listview, commentArrayList);
        }
    }

}
