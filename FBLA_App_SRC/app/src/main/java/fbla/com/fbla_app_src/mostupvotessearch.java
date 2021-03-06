package fbla.com.fbla_app_src;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
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

public class mostupvotessearch extends AppCompatActivity {
    // Declared global variables
    FrameLayout search;
    FrameLayout add;
    FrameLayout profile;
    RelativeLayout recent;
    ProgressBar spinner;
    BackendlessUser user;
    RelativeLayout trending;
    Boolean hasuserUpvoted;
    ArrayList<Post> postsToBeDisplayed = new ArrayList<>();
    ListView mainList;
    public ArrayAdapter<Post> adapter;
    static ArrayList<Drawable> draw = new ArrayList<Drawable>();
    public DownloadImagesClass DLC = new DownloadImagesClass();
    public ArrayList<String> URLS = new ArrayList<String>();
    final mostupvotessearch g = this;
    public ArrayList<Integer> posArr = new ArrayList<>();
    public void setDL(ArrayList<Drawable> dr)
    {
       draw = dr;
    }
    public ArrayList<String> getURLS(ArrayList<Post> postArray)
    {

        for (Post post : postArray)
        {
            URLS.add("https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/"+ post.getPictureOID() +".png");
            Log.i("ADDEDED", "https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/"+post.getPictureOID() +".png");

        }
        return URLS;
    }
    //creates the page
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostupvotessearch);
        // links variables to xml id representations
        search = (FrameLayout) findViewById(R.id.profilepage_searchNav);
        add = (FrameLayout) findViewById(R.id.searchAdd);
        profile = (FrameLayout) findViewById(R.id.frameLayout6);
        mainList = (ListView) findViewById(R.id.upvoteListView);
        recent = (RelativeLayout) findViewById(R.id.recentFromUpvote);
        user = Backendless.UserService.CurrentUser();
        trending = (RelativeLayout) findViewById(R.id.trendingFromUpvote);
        spinner = (ProgressBar) findViewById(R.id.loadingMostSpinner);
        adapter = new MyListAdapter();
        //takes user to recent tab
        recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mostupvotessearch.this, recentsearch.class);
                startActivity(i);
            }
        });
        //takes user to trending tab
        trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mostupvotessearch.this, trendingsearch.class);
                startActivity(i);
            }
        });
        //navigaion
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackCamera(1, mostupvotessearch.this);

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mostupvotessearch.this, profilePage.class);
                startActivity(i);
            }
        });
        getFirstPage(this);
        showSpinner();

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
        File imgFile = new File(pictureImagePath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        if(myBitmap != null) {
            Intent i = new Intent(mostupvotessearch.this, uploadPostActivity.class);
            i.putExtra("IP", pictureImagePath);
            startActivity(i);
        }
    }
    //displays posts
    public void populateListView() throws InterruptedException
    {
        for(int i : posArr)
        {
            try {
                postsToBeDisplayed.remove(i);
            }
            catch (IndexOutOfBoundsException e)
            {

            }
        }

        mainList.setAdapter(adapter);
    }
    //updates the list of posts
    private void updateList()
    {
        adapter.notifyDataSetChanged();
    }
    //retrieves the posts from backendless
    public void getFirstPage(final Context c)
    {

        AsyncCallback<BackendlessCollection<Post>> callback = new AsyncCallback<BackendlessCollection<Post>>()
        {
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
                DLC.setFrom(g);
                DLC.execute(getURLS(postsToBeDisplayed));

                /*
                ArrayList<String> sArr = getURLS(postsToBeDisplayed);
                for(String s : sArr)
                {
                    ExtendedTarget t = new ExtendedTarget()
                    {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                        {
                            Log.i("Download", "Complete");
                            setBd(new BitmapDrawable(bitmap));

                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable)
                        {
                            Log.i("Download", "Failed");
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable)
                        {

                        }
                    };
                    Picasso.with(c).load(s).into(t);
                    draw.add(t.getBd());

                }
                try {
                    populateListView();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */

            }


            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        };
        int pageSize = 10;
        BackendlessDataQuery query = new BackendlessDataQuery();
        QueryOptions qo = new QueryOptions();

        qo.addSortByOption("numLikes desc");
        query.setWhereClause("isReported = FALSE");
        query.setPageSize(pageSize);
        query.setQueryOptions(qo);

        Backendless.Data.of(Post.class).find(query, callback);
        hideSpinner();



    }
    public void showSpinner(){spinner.setVisibility(View.VISIBLE);}
    public void hideSpinner(){spinner.setVisibility(View.INVISIBLE);}
    //displays a list of all posts
    public class MyListAdapter extends ArrayAdapter<Post>
    {

        public MyListAdapter() {
            super(mostupvotessearch.this, R.layout.layout_listview, postsToBeDisplayed);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.layout_listview, parent, false);
            }
            final Post post = postsToBeDisplayed.get(position);
            ImageView iv = (ImageView) itemView.findViewById(R.id.item_listViewImage);
            TextView tv = (TextView) itemView.findViewById(R.id.item_listViewCaption);
            final TextView numlikes = (TextView) itemView.findViewById(R.id.item_listViewUpVote);
            ImageView upvoteArrow = (ImageView) itemView.findViewById(R.id.item_listViewUpvoteArrow);
            ImageView downvoteArrow = (ImageView) itemView.findViewById(R.id.item_listViewDownVoteArrow);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(mostupvotessearch.this, postview.class);
                    i.putExtra("postID", post.getObjectId());
                    startActivity(i);
                }
            });
            upvoteArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Log.i("C", "CLICKED");

                    AsyncCallback<BackendlessCollection<upvoted>> callback = new AsyncCallback<BackendlessCollection<upvoted>>()
                    {
                        @Override
                        public void handleResponse(BackendlessCollection<upvoted> upvotedBackendlessCollection)
                        {
                            List<upvoted> list = upvotedBackendlessCollection.getCurrentPage();
                            if(list.isEmpty())
                            {
                                post.setNumLikes(post.getNumLikes() + 1);
                                Backendless.Persistence.save(post, new AsyncCallback<Post>() {
                                    @Override
                                    public void handleResponse(Post post) {
                                        numlikes.setText(Integer.toString(post.getNumLikes()));
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault backendlessFault) {

                                    }
                                });
                                upvoted uv = new upvoted();
                                uv.setPostid(post.getObjectId());
                                uv.setUserid(user.getObjectId());
                                Backendless.Persistence.save(uv, new AsyncCallback<upvoted>() {
                                    @Override
                                    public void handleResponse(upvoted upvoted) {

                                    }

                                    @Override
                                    public void handleFault(BackendlessFault backendlessFault) {

                                    }
                                });
                                user.setProperty("numlikes", (Integer) user.getProperty("numlikes") + 1);
                                util.updateUser(user);
                            }

                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {

                        }
                    };
                    BackendlessDataQuery query = new BackendlessDataQuery();
                    QueryOptions qo = new QueryOptions();
                    query.setWhereClause("postid = '" + post.getObjectId() + "' AND userid = '" + user.getObjectId() + "'");
                    query.setQueryOptions(qo);
                    Backendless.Data.of(upvoted.class).find(query, callback);
                }
            });
            downvoteArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("C", "CLICKED");
                    if (post.getNumLikes() > 0) {
                        post.setNumDislikes(post.getNumDislikes() + 1);
                        Backendless.Data.save(post, new AsyncCallback<Post>() {
                            @Override
                            public void handleResponse(Post post) {

                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {

                            }
                        });
                    }
                }
            });
            iv.setImageDrawable(mostupvotessearch.draw.get(position));
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
            return itemView;
            //return super.getView(position, convertView, parent);
        }

    }

}
