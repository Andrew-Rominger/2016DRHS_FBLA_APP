package fbla.com.fbla_app_src;

import android.app.Activity;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

public class recentsearch extends AppCompatActivity {
    // Declared global variables
    FrameLayout search;
    FrameLayout add;
    ProgressBar spinner;
    FrameLayout profile;
    RelativeLayout upVote;
    RelativeLayout trending;
    ListView lv;
    static ArrayList<Drawable> draw;
    ArrayList<Post> postsToBeDisplayed = new ArrayList<>();
    public DownloadImagesClass DLC = new DownloadImagesClass();
    public ArrayList<String> URLS = new ArrayList<String>();
    final recentsearch g = this;

    public void setDL(ArrayList<Drawable> dr)
    {
        draw = dr;
    }
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recentsearch);
        setupUI(findViewById(R.id.recentSearchBackground));
        //get views
        adapter = new MyListAdapter();
        search = (FrameLayout) findViewById(R.id.frameLayout4);
        add = (FrameLayout) findViewById(R.id.frameLayout5);
        profile = (FrameLayout) findViewById(R.id.frameLayout6);
        spinner = (ProgressBar) findViewById(R.id.loadingBarRecent);
        lv  = (ListView) findViewById(R.id.listView_Recent);
        upVote = (RelativeLayout) findViewById(R.id.upVote);
        trending = (RelativeLayout) findViewById(R.id.trending);

        //set listers
        upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(recentsearch.this, mostupvotessearch.class);
                startActivity(i);
            }
        });
        trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(recentsearch.this, trendingsearch.class);
                startActivity(i);
            }
        });
        //navigaion
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(recentsearch.this, recentsearch.class);
                startActivity(i);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackCamera(1,recentsearch.this);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(recentsearch.this, profilePage.class);
                startActivity(i);
            }
        });
        //get first page of data
        showSpinner();
        getFirstPage();
    }
    //put data in list
    public void populateListView() throws InterruptedException
    {

        lv.setAdapter(adapter);
    }
    public void showSpinner()
    {
        spinner.setVisibility(View.VISIBLE);
    }
    public void hideSpinner()
    {
        spinner.setVisibility(View.INVISIBLE);
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
        File imgFile = new File(pictureImagePath);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        if(myBitmap != null) {
            Intent i = new Intent(recentsearch.this, uploadPostActivity.class);
            i.putExtra("IP", pictureImagePath);
            startActivity(i);
        }
    }
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

    public void getFirstPage()
    {
        //declare new callback for posts
        AsyncCallback<BackendlessCollection<Post>> callback = new AsyncCallback<BackendlessCollection<Post>>() {
            @Override
            public void handleResponse(BackendlessCollection<Post> postBackendlessCollection)
            {
                Log.i("GotPosts", "GotPosts");
                //gets current page
                List<Post> firstPage = postBackendlessCollection.getCurrentPage();
                Iterator<Post> it = firstPage.iterator();
                while (it.hasNext())
                {
                    //adds each post from the page into a list
                    Post post = it.next();
                    Log.i("POST ID", post.getObjectId());
                    postsToBeDisplayed.add(post);
                }
                //downloads posts
                DLC.setDlList(draw);
                DLC.setFromR(g);
                DLC.execute(getURLS(postsToBeDisplayed));
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        };
        int pageSize = 10;
        //set query for sorting
        BackendlessDataQuery query = new BackendlessDataQuery();
        QueryOptions qo = new QueryOptions();
        qo.addSortByOption("created desc");

        query.setPageSize(pageSize);
        query.setQueryOptions(qo);
        //call method to get posts
        Backendless.Data.of(Post.class).find(query, callback);
    }
    public class MyListAdapter extends ArrayAdapter<Post>
    {

        public MyListAdapter()
        {
            super(recentsearch.this, R.layout.layout_listview, postsToBeDisplayed);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            //populates each row in the list with its respective data
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.layout_listview, parent, false);
            }
            Post post = postsToBeDisplayed.get(position);
            ImageView iv = (ImageView) itemView.findViewById(R.id.item_listViewImage);
            TextView tv = (TextView) itemView.findViewById(R.id.item_listViewCaption);
            TextView numlikes = (TextView) itemView.findViewById(R.id.item_listViewUpVote);
            iv.setImageDrawable(recentsearch.draw.get(position));
            if(post.getCaption().length() > 16)
            {
                String newCap = post.getCaption().substring(0,12) + "...";
                tv.setText(newCap);
            }
            else
            {
                tv.setText(post.getCaption());
            }
            numlikes.setText(""+post.getNumLikes());
            return itemView;
            //return super.getView(position, convertView, parent);
        }
    }
    //this method hides the keyboard
    public static void hideSoftKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    //this method checks to see if the user has clicked outside of the edit text when the key board is up, if they do the hideSoftKeyboard() method is called
    public void setupUI(View view) {

        if(!(view instanceof RelativeLayout)) {
            //Set up touch listener for non-text box views to hide keyboard.
            if (!(view instanceof EditText)) {

                view.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        hideSoftKeyboard(recentsearch.this);
                        return false;
                    }

                });
            }

            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {

                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                    View innerView = ((ViewGroup) view).getChildAt(i);

                    setupUI(innerView);
                }
            }
        }
    }

}
