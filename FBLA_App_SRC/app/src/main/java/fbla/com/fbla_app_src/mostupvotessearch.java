package fbla.com.fbla_app_src;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

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

public class mostupvotessearch extends AppCompatActivity {
    // Declared global variables
    FrameLayout search;
    FrameLayout add;
    FrameLayout profile;
    RelativeLayout recent;
    RelativeLayout trending;
    ArrayList<Post> postsToBeDisplayed = new ArrayList<>();
    ListView mainList;
    ArrayAdapter<Post> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostupvotessearch);

        search = (FrameLayout) findViewById(R.id.profilepage_searchNav);
        add = (FrameLayout) findViewById(R.id.searchAdd);
        profile = (FrameLayout) findViewById(R.id.profilepage_profileNav);
        mainList = (ListView) findViewById(R.id.upvoteListView);
        recent = (RelativeLayout) findViewById(R.id.recentFromUpvote);
        trending = (RelativeLayout) findViewById(R.id.trendingFromUpvote);
        adapter = new ArrayAdapter<Post>(this, R.layout.layout_listview, postsToBeDisplayed);

        recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mostupvotessearch.this, recentsearch.class);
                startActivity(i);
            }
        });
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
    }

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
        Intent i = new Intent(mostupvotessearch.this, uploadPostActivity.class);
        i.putExtra("passedPictureData", data);
        i.putExtra("IP", pictureImagePath);
        startActivity(i);
    }

    private void populateListView() throws InterruptedException
    {

        mainList.setAdapter(adapter);

    }
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
                List<Post> firstPage = postBackendlessCollection.getCurrentPage();
                Iterator<Post> it = firstPage.iterator();
                while (it.hasNext())
                {
                    Post post = it.next();
                    postsToBeDisplayed.add(post);
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        };
        int pageSize = 10;
        BackendlessDataQuery query = new BackendlessDataQuery();
        QueryOptions qo = new QueryOptions();
        qo.addSortByOption("upvotes DSC");
        query.setPageSize(pageSize);
        Backendless.Data.of(Post.class).find(query,callback);
    }





}
