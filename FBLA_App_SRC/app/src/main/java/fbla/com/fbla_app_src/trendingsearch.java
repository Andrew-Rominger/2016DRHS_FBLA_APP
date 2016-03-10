package fbla.com.fbla_app_src;

import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class trendingsearch extends AppCompatActivity {
    // Declared global variables
    FrameLayout search;
    FrameLayout add;
    FrameLayout profile;
    RelativeLayout upVote;
    RelativeLayout recent;
    String pictureImagePath = "";
    ListView listView;

    static ArrayList<Drawable> draw;
    ArrayList<Post> postsToBeDisplayed = new ArrayList<>();
    public DownloadImagesClass DLC = new DownloadImagesClass();
    public ArrayList<String> URLS = new ArrayList<String>();
    final trendingsearch g = this;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trendingsearch);
        adapter = new MyListAdapter();

        search = (FrameLayout) findViewById(R.id.frameLayout4);
        add = (FrameLayout) findViewById(R.id.frameLayout5);
        profile = (FrameLayout) findViewById(R.id.frameLayout6);
        upVote = (RelativeLayout) findViewById(R.id.upVote);
        recent = (RelativeLayout) findViewById(R.id.recent);
        listView = (ListView) findViewById(R.id.listView_Trending);


        upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(trendingsearch.this, mostupvotessearch.class);
                startActivity(i);
            }
        });
        recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(trendingsearch.this, recentsearch.class);
                startActivity(i);
            }
        });
        //navigation on click listeners
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(trendingsearch.this, trendingsearch.class);
                startActivity(i);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackCamera(1,trendingsearch.this);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(trendingsearch.this, profilePage.class);
                startActivity(i);
            }
        });
        getFirstPage();
    }
    public void populateListView() throws InterruptedException
    {
        listView.setAdapter(adapter);
    }
    private void updateList()
    {
        adapter.notifyDataSetChanged();
    }
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
        Intent i = new Intent(trendingsearch.this, uploadPostActivity.class);
        i.putExtra("IP", pictureImagePath);
        startActivity(i);
    }
    public void setDL(ArrayList<Drawable> dr)
    {
        draw = dr;
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
                DLC.setFromT(g);
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
        query.setWhereClause(createWhereClause());
        query.setPageSize(pageSize);
        query.setQueryOptions(qo);
        Backendless.Data.of(Post.class).find(query, callback);
    }
    public class MyListAdapter extends ArrayAdapter<Post>
    {

        public MyListAdapter()
        {
            super(trendingsearch.this, R.layout.layout_listview, postsToBeDisplayed);
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
            iv.setImageDrawable(trendingsearch.draw.get(position));
            tv.setText(post.getCaption());
            numlikes.setText(""+post.getNumLikes());
            return itemView;
            //return super.getView(position, convertView, parent);
        }
    }
    public String createWhereClause()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -2);
        dateFormat.format(c.get(Calendar.DAY_OF_MONTH));
        Date toLoadFrom = c.getTime();
        String toLoadFromS = toLoadFrom.toString();
        toLoadFromS = toLoadFromS.toLowerCase();

        String Month = toLoadFromS.substring(4, 7);
        Log.i("LOG", toLoadFromS);
        Log.i("logging", Month);
        switch (Month)
        {
            case "jan":
                Month = "01";
                break;
            case "feb":
                Month = "02";
                break;
            case "mar":
                Month = "03";
                break;
            case "apr":
                Month = "04";
                break;
            case "may":
                Month = "05";
                break;
            case "jun":
                Month = "06";
                break;
            case "jul":
                Month = "07";
                break;
            case "aug":
                Month = "08";
                break;
            case "sep":
                Month = "09";
                break;
            case "oct":
                Month = "10";
                break;
            case "nov":
                Month = "11";
                break;
            case "dec":
                Month = "13";
                break;
            default:
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
        }
        String Day = toLoadFromS.substring(8,10);
        String Hour = toLoadFromS.substring(11,13);
        String Minute = toLoadFromS.substring(14, 16);
        String Seconds = toLoadFromS.substring(17,19);
        String Year = toLoadFromS.substring(24,28);
        String str1 = Month + "/" + Day + "/" + Year;
        String str = "created > " + str1;
        Log.i("c", str);
        return str;

    }

}
