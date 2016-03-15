package fbla.com.fbla_app_src;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class searchByTags extends AppCompatActivity
{
    TextView seachBy;
    ListView seachList;
    FrameLayout search;
    FrameLayout add;
    String tag;
    FrameLayout profile;
    Intent i;
    final searchByTags g = this;
    ArrayList<Post> postsToBeDisplayed = new ArrayList<>();
    public DownloadImagesClass DLC = new DownloadImagesClass();
    public ArrayList<String> URLS = new ArrayList<String>();
    static ArrayList<Drawable> draw;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_tags);
        seachBy = (TextView) findViewById(R.id.tagTitle);
        seachList = (ListView) findViewById(R.id.searchListView);
        add = (FrameLayout) findViewById(R.id.searchAdd);
        profile = (FrameLayout) findViewById(R.id.searchProfile);
        search = (FrameLayout) findViewById(R.id.FLSearch);
        i = getIntent();
        Bundle b = i.getExtras();

        tag = (String) b.get("tag");
        String str = "Posts Tagged " + tag;
    //      seachBy.setText(str);
        adapter = new MyListAdapter();



        getFirstPage();
    }

    public static ArrayList<Drawable> getDraw() {
        return draw;
    }

    public void setDraw(ArrayList<Drawable> draw) {
        this.draw = draw;
    }

    public void populateListView() throws InterruptedException
    {
        seachList.setAdapter(adapter);
    }
    private void updateList()
    {
        adapter.notifyDataSetChanged();
    }
    public ArrayList<String> getURLS(ArrayList<Post> postArray)
    {

        for (Post post : postArray)
        {
            URLS.add("https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/"+ post.getPictureOID() +".png");
            Log.i("ADDEDED", "https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/" + post.getPictureOID() + ".png");

        }
        return URLS;
    }
    public void getFirstPage()
    {
        AsyncCallback<BackendlessCollection<Post>> callback = new AsyncCallback<BackendlessCollection<Post>>() {
            @Override
            public void handleResponse(BackendlessCollection<Post> postBackendlessCollection)
            {
                if(postBackendlessCollection!= null)
                {
                    Log.i("GotPosts", "GotPosts");
                    List<Post> firstPage = postBackendlessCollection.getCurrentPage();
                    Iterator<Post> it = firstPage.iterator();
                    while (it.hasNext()) {
                        Post post = it.next();
                        Log.i("POST ID", post.getObjectId());
                        postsToBeDisplayed.add(post);
                    }
                    DLC.setDlList(draw);
                    DLC.setFromS(g);
                    DLC.execute(getURLS(postsToBeDisplayed));
                }
                else
                {
                    Toast.makeText(searchByTags.this,"No posts found matching" + tag, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        };
        int pageSize = 10;
        BackendlessDataQuery query = new BackendlessDataQuery();
        QueryOptions qo = new QueryOptions();
        qo.addSortByOption("numLikes desc");
        String w = "tag LIKE '" + tag + "'";
        query.setWhereClause(w);
        query.setPageSize(pageSize);
        query.setQueryOptions(qo);
        Backendless.Data.of(Post.class).find(query, callback);
    }
    public class MyListAdapter extends ArrayAdapter<Post>
    {

        public MyListAdapter()
        {
            super(searchByTags.this, R.layout.layout_listview, postsToBeDisplayed);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.layout_listview, parent, false);
            }
            Post post = postsToBeDisplayed.get(position);
            ImageView iv = (ImageView) itemView.findViewById(R.id.item_listViewImage);
            TextView tv = (TextView) itemView.findViewById(R.id.item_listViewCaption);
            TextView numlikes = (TextView) itemView.findViewById(R.id.item_listViewUpVote);
            iv.setImageDrawable(draw.get(position));
            if(post.getCaption().length() > 16)
            {
                String newCap = post.getCaption().substring(0,12) + "...";
                tv.setText(newCap);
            }
            else
            {
                tv.setText(post.getCaption());
            }

            tv.setText(post.getCaption());
            numlikes.setText(""+post.getNumLikes());
            return itemView;
            //return super.getView(position, convertView, parent);
        }
    }

}
