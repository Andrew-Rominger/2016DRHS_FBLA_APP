package fbla.com.fbla_app_src;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class commentView extends AppCompatActivity
{
    //Create variables
    ListView commentListView;
    ArrayList<Comment> commentArrayList = new ArrayList<>();
    Post post;
    static ArrayList<Drawable> draw;
    public DownloadImagesClass DLC = new DownloadImagesClass();
    public ArrayList<String> URLS = new ArrayList<String>();
    ImageView add;
    ImageView close;
    ArrayAdapter adapter;
    commentView g = this;
    String postOID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //setup page
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setContentView(R.layout.activity_comment_view);
        Bundle b = i.getExtras();
        postOID = b.getString("postID");

        add = (ImageView) findViewById(R.id.addComment);
        close = (ImageView) findViewById(R.id.closeComments);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(commentView.this, addComment.class);
                startActivityForResult(i,1);

            }
        });



        //get post
        Backendless.Data.of(Post.class).findById(postOID, new AsyncCallback<Post>()
        {
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
        //get comments
        getComments();

    }
    //called after a comment is created
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(data.getStringExtra("result") != null)
        {
            // creates a new comment in the data base
            String commentString = data.getStringExtra("result");
            DateFormat df = new SimpleDateFormat("MM-dd-yyyy, hh:mm aa");
            String date = df.format(Calendar.getInstance().getTime());
            Comment c = new Comment();
            c.setContent(commentString);
            c.setPostid(commentView.this.postOID);
            c.setUseruploaded(Backendless.UserService.CurrentUser().getProperty("userName").toString());
            c.setDateUploaded(date);
            Backendless.Persistence.save(c, new AsyncCallback<Comment>()
            {
                @Override
                public void handleResponse(Comment comment)
                {
                    //adds comment to page
                    Toast.makeText(commentView.this, "Comment Uploaded", Toast.LENGTH_LONG).show();
                    commentArrayList.add(0,comment);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void handleFault(BackendlessFault backendlessFault)
                {
                    Toast.makeText(commentView.this, "Comment Failed to upload", Toast.LENGTH_LONG).show();
                    Log.e("Comment did not upload", backendlessFault.getMessage());
                }
            });
        }
        else
        {
            Log.i("Extra Error", "Extra Was null");
        }

    }

    //gets the comments for post
    private void getComments()
    {
        final commentView c = this;
        AsyncCallback<BackendlessCollection<Comment>> callback = new AsyncCallback<BackendlessCollection<Comment>>()
        {
            @Override
            public void handleResponse(BackendlessCollection<Comment> commentBackendlessCollection)
            {
                Log.i("found comments", "found "+ commentBackendlessCollection.getCurrentPage().size() + " comments");
                List<Comment> found = commentBackendlessCollection.getCurrentPage();
                for(Comment c : found)
                {
                    commentArrayList.add(c);
                }
                try {
                    c.populateListView();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        };
        // sets parametets for finding comments
        BackendlessDataQuery query = new BackendlessDataQuery();
        QueryOptions qo = new QueryOptions();
        qo.addSortByOption("created desc");
        Log.i("Post id:" ,postOID);
        query.setWhereClause("postid = '" + postOID + "'");
        query.setPageSize(10);
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.layout_commentlistview, parent, false);
            }
            final Comment c = commentArrayList.get(position);
            TextView content = (TextView) itemView.findViewById(R.id.item_commentContent);
            TextView userNameView = (TextView) itemView.findViewById(R.id.item_commentUserName);
            TextView dateView = (TextView) itemView.findViewById(R.id.item_commentDate);
            content.setText(c.getContent());
            userNameView.setText(c.getUseruploaded());
            dateView.setText(c.getDateUploaded().toString());
            return itemView;
            //return super.getView(position, convertView, parent);
        }
    }

}
