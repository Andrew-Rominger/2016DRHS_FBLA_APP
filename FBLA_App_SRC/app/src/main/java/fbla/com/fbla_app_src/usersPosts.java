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

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class usersPosts extends AppCompatActivity
{
    ListView listView;
    ImageView goBack;
    BackendlessUser user;

    static ArrayList<Drawable> draw;
    ArrayList<Post> postsToBeDisplayed = new ArrayList<>();
    public DownloadImagesClass DLC = new DownloadImagesClass();
    public ArrayList<String> URLS = new ArrayList<String>();
    final usersPosts u = this;
    public ArrayList<Integer> posArr = new ArrayList<>();
    ArrayAdapter adapter;
    public ArrayList<String> getURLS(ArrayList<Post> postArray)
    {

        for (Post post : postArray)
        {
            URLS.add("https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/"+ post.getPictureOID() +".png");
            Log.i("ADDEDED", "https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/"+post.getPictureOID() +".png");

        }
        return URLS;
    }

    public static ArrayList<Drawable> getDraw() {
        return draw;
    }

    public void setDraw(ArrayList<Drawable> draw) {
        this.draw = draw;
    }

    public ArrayList<Post> getPostsToBeDisplayed() {
        return postsToBeDisplayed;
    }

    public void setPostsToBeDisplayed(ArrayList<Post> postsToBeDisplayed) {
        this.postsToBeDisplayed = postsToBeDisplayed;
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }
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

        listView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        listView = (ListView) findViewById(R.id.yourPostsListView);
        goBack = (ImageView) findViewById(R.id.yourPostsCancle);
        user = Backendless.UserService.CurrentUser();
        adapter = new MyListAdapter();

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(usersPosts.this, profilePage.class ));
            }
        });
        getFirstPage();

    }

    public void getFirstPage()
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
                DLC.setFromU(u);
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
        query.setWhereClause("isReported = FALSE AND ownerId = '" + user.getObjectId() + "'");
        query.setPageSize(pageSize);
        query.setQueryOptions(qo);

        Backendless.Data.of(Post.class).find(query, callback);
    }
    public class MyListAdapter extends ArrayAdapter<Post>
    {

        public MyListAdapter()
        {
            super(usersPosts.this, R.layout.layout_listview, postsToBeDisplayed);
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
            final TextView tv = (TextView) itemView.findViewById(R.id.item_listViewCaption);
            final TextView numlikes = (TextView) itemView.findViewById(R.id.item_listViewUpVote);
            ImageView upvoteArrow = (ImageView) itemView.findViewById(R.id.item_listViewUpvoteArrow);

            try{
                iv.setImageDrawable(usersPosts.draw.get(position));
            }catch (IndexOutOfBoundsException e)
            {

            }



            if(post.getCaption().length() > 16)
            {
                String newCap = post.getCaption().substring(0,12) + "...";
                tv.setText(newCap);
            }
            else
            {
                tv.setText(post.getCaption());
            }
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(usersPosts.this, postview.class);
                    i.putExtra("postID", post.getObjectId());
                    startActivity(i);
                }
            });
            numlikes.setText(String.valueOf(post.getNumLikes()));
            upvoteArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            return itemView;
            //return super.getView(position, convertView, parent);
        }
    }
}
