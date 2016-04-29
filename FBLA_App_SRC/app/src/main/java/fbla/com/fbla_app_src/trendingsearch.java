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
import android.widget.LinearLayout;
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
    BackendlessUser user;

    ListView listView;
    ProgressBar spinner;
    EditText tagSearch;
    LinearLayout m;
    ImageView go;
    ImageView imageView;

    static ArrayList<Drawable> draw;
    ArrayList<Post> postsToBeDisplayed = new ArrayList<>();
    public DownloadImagesClass DLC = new DownloadImagesClass();
    public ArrayList<String> URLS = new ArrayList<String>();
    final trendingsearch g = this;
    ArrayAdapter adapter;

    public static ArrayList<Drawable> getDraw() {
        return draw;
    }

    public static void setDraw(ArrayList<Drawable> draw) {
        trendingsearch.draw = draw;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trendingsearch);
        setupUI(findViewById(R.id.trendingSearchBackground));
        adapter = new MyListAdapter();
        user = Backendless.UserService.CurrentUser();
        search = (FrameLayout) findViewById(R.id.frameLayout4);
        m = (LinearLayout)findViewById(R.id.dummmyFocus);
        add = (FrameLayout) findViewById(R.id.frameLayout5);
        profile = (FrameLayout) findViewById(R.id.frameLayout6);
        upVote = (RelativeLayout) findViewById(R.id.upVote);
        recent = (RelativeLayout) findViewById(R.id.recent);
        spinner = (ProgressBar) findViewById(R.id.loadingBarTrending);
        listView = (ListView) findViewById(R.id.listView_Trending);
        tagSearch = (EditText) findViewById(R.id.searchView);
        go = (ImageView) findViewById(R.id.goButton);
        tagSearch.clearFocus();
        imageView = (ImageView) findViewById(R.id.lfs);
        m.requestFocus();

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
        tagSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                imageView.setVisibility(View.GONE);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackCamera(1, trendingsearch.this);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(trendingsearch.this, profilePage.class);
                startActivity(i);
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle b = new Bundle();
                b.putString("tag", tagSearch.getText().toString());

                Intent i = new Intent(trendingsearch.this, searchByTags.class);
                i.putExtras(b);
                DLC.cancel(true);

                startActivity(i);
            }
        });
        showSpinner();
        getFirstPage();
    }
    public void populateListView() throws InterruptedException
    {
        listView.setAdapter(adapter);
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
            Intent i = new Intent(trendingsearch.this, uploadPostActivity.class);
            i.putExtra("IP", pictureImagePath);
            startActivity(i);
        }
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
        //query.setWhereClause(createWhereClause());
        query.setPageSize(pageSize);
        query.setQueryOptions(qo);
        Backendless.Data.of(Post.class).find(query, callback);
    }
    public void showSpinner()
    {
        spinner.setVisibility(View.VISIBLE);
    }
    public void hideSpinner()
    {
        spinner.setVisibility(View.INVISIBLE);
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
            final Post post = postsToBeDisplayed.get(position);
            ImageView iv = (ImageView) itemView.findViewById(R.id.item_listViewImage);
            final TextView tv = (TextView) itemView.findViewById(R.id.item_listViewCaption);
            final TextView numlikes = (TextView) itemView.findViewById(R.id.item_listViewUpVote);
            ImageView upvoteArrow = (ImageView) itemView.findViewById(R.id.item_listViewUpvoteArrow);
            iv.setImageDrawable(trendingsearch.draw.get(position));
            if(post.getCaption().length() > 16)
            {
                String newCap = post.getCaption().substring(0,12) + "...";
                tv.setText(newCap);
            }
            else
            {
                tv.setText(post.getCaption());
            }
            numlikes.setText(String.valueOf(post.getNumLikes()));
            upvoteArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("C", "CLICKED");
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
                    user.setProperty("numlikes", (Integer) user.getProperty("numlikes") + 1);
                    util.updateUser(user);
                }
            });
            return itemView;
            //return super.getView(position, convertView, parent);
        }
    }
    public String createWhereClause()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -2);
        dateFormat.format(c.get(Calendar.DAY_OF_MONTH));
        Date toLoadFrom = c.getTime();
        String toLoadFromS = toLoadFrom.toString();
        Log.i(toLoadFromS, toLoadFromS);
        return toLoadFromS.toLowerCase();


    }
    //this method hides the keyboard
    public static void hideSoftKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    //this method checks to see if the user has clicked outside of the edit text when the key board is up, if they do the hideSoftKeyboard() method is called
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(trendingsearch.this);
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
