package fbla.com.fbla_app_src;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class yourupvotes extends AppCompatActivity {

    // Declared global variables
    FrameLayout search;
    FrameLayout add;
    FrameLayout profile;
    GridView gV;
    static ArrayList<Drawable> draw;
    ArrayList<Post> postsToBeDisplayed = new ArrayList<>();
    public DownloadImagesClass DLC = new DownloadImagesClass();
    public ArrayAdapter<Post> adapter;
    public ArrayList<String> URLS = new ArrayList<String>();
    final yourupvotes g = this;


    //gets urls for post list
    public ArrayList<String> getURLS(ArrayList<Post> postArray)
    {

        for (Post post : postArray)
        {
            URLS.add("https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/"+ post.getPictureOID() +".png");
            Log.i("ADDEDED", "https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/" + post.getPictureOID() + ".png");

        }
        return URLS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourupvotes);
        search = (FrameLayout) findViewById(R.id.frameLayout4);
        add = (FrameLayout) findViewById(R.id.frameLayout5);
        profile = (FrameLayout) findViewById(R.id.frameLayout6);
        adapter = new MyListAdapter();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(yourupvotes.this, mostupvotessearch.class));
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackCamera(1, yourupvotes.this);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(yourupvotes.this, profilePage.class));
            }
        });
        getFirstPage();
    }

    //declares image path
    private String pictureImagePath = "";
    public void openBackCamera(int numCode, Context context)
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".png";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, numCode);
    }
    public void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        Intent i = new Intent(yourupvotes.this, uploadPostActivity.class);
        i.putExtra("IP", pictureImagePath);
        startActivity(i);
    }
    //displays posts
    public void populateListView() throws InterruptedException
    {
        gV.setAdapter(adapter);
    }
    //updates the list of posts
    private void updateList()
    {
        adapter.notifyDataSetChanged();
    }

    public void getFirstPage()
    {
        AsyncCallback<BackendlessCollection<Post>> callback = new AsyncCallback<BackendlessCollection<Post>>() {
            @Override
            public void handleResponse(BackendlessCollection<Post> postBackendlessCollection)
            {
                Log.i("GotPosts", "GotPosts");
                List<Post> firstPage = postBackendlessCollection.getCurrentPage();
                Iterator<Post> it = firstPage.iterator();
                while (it.hasNext())
                {
                    Post post = it.next();
                    Log.i("POST ID", post.getObjectId());
                    postsToBeDisplayed.add(post);
                }
                DLC.setDlList(draw);
               // DLC.setFromY(g);
                DLC.execute(getURLS(postsToBeDisplayed));
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        };
        int pageSize = 10;
        BackendlessDataQuery query = new BackendlessDataQuery();
        QueryOptions qo = new QueryOptions();

        qo.addSortByOption("numLikes desc");
        query.setPageSize(pageSize);
        query.setQueryOptions(qo);

        Backendless.Data.of(Post.class).find(query, callback);
    }
    //displays a list of all posts
    public class MyListAdapter extends ArrayAdapter<Post>
    {

        public MyListAdapter() {
            super(yourupvotes.this, R.layout.upvotelist, postsToBeDisplayed);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.upvotelist, parent, false);
            }
            final Post post = postsToBeDisplayed.get(position);
            ImageView iv = (ImageView) itemView.findViewById(R.id.item_listViewImage);
        //    TextView tv = (TextView) itemView.findViewById(R.id.item_listViewCaption);
        //    final TextView numlikes = (TextView) itemView.findViewById(R.id.item_listViewUpVote);
        //    ImageView upvoteArrow = (ImageView) itemView.findViewById(R.id.item_listViewUpvoteArrow);
        //    ImageView downvoteArrow = (ImageView) itemView.findViewById(R.id.item_listViewDownVoteArrow);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(yourupvotes.this, postview.class);
                    i.putExtra("postID", post.getObjectId());
                    startActivity(i);
                }
            });
            /*
            iv.setImageDrawable(yourupvotes.draw.get(position));
            if(post.getCaption().length() > 16)
            {
                String newCap = post.getCaption().substring(0,12) + "...";
                tv.setText(newCap);
            }
            else
            {
                tv.setText(post.getCaption());
            }
            numlikes.setText(Integer.toString(post.getNumLikes()));
            */
            return itemView;
            //return super.getView(position, convertView, parent);
        }

    }

}
