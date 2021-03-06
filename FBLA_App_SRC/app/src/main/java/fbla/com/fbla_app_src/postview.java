package fbla.com.fbla_app_src;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.List;

public class postview extends AppCompatActivity {
    // Declared global variables
    TextView upVotes;
    TextView downVotes;
    TextView comments;
    TextView userNameBelow;
    TextView userName;
    TextView caption;
    RelativeLayout backOfPost;
    ImageView upVoteButton;
    ImageView downVoteButton;
    ImageView commentsButton;
    ImageView postViewReport;
    Dialog log;
    ImageView report;
    FrameLayout search;
    FrameLayout add;
    FrameLayout profile;
    ImageView post;
    private GestureDetector gestureDetector;
    BackendlessUser user;
    Post postO;
    DownloadImageClass DLC = new DownloadImageClass();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        String PostID = extras.get("postID").toString();
        Backendless.Data.of(Post.class).findById(PostID, new AsyncCallback<Post>() {
            @Override
            public void handleResponse(Post foundPost) {
                //gets post from id and sets picture
                postO = foundPost;
                DLC.setImageView(post);
                DLC.execute("https://api.backendless.com/67BF989E-7E10-5DB8-FFD7-C9147CA4F200/v1/files/media/userpics/" + foundPost.getPictureOID() + ".png");
                upVotes.setText(Integer.toString(postO.getNumLikes()));
                downVotes.setText(Integer.toString(postO.getNumDislikes()));
                userName.setText(user.getProperty("userName").toString());
                userNameBelow.setText(postO.getUserUploadedS());
                caption.setText(postO.getCaption());
                //comments.setText(Integer.toString(postO.getNumComments()));
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });
        //get views


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postview);
        post = (ImageView) findViewById(R.id.picturePost);
        add = (FrameLayout) findViewById(R.id.postViewAdd);
        commentsButton = (ImageView) findViewById(R.id.postViewComment);
        postViewReport = (ImageView) findViewById(R.id.postViewReport);
        search = (FrameLayout) findViewById(R.id.postViewSearch);
        report = (ImageView) findViewById(R.id.postViewReport);
        backOfPost = (RelativeLayout) findViewById(R.id.backOfPost);
        downVoteButton = (ImageView) findViewById(R.id.postViewDownvoteButton);
        upVoteButton = (ImageView) findViewById(R.id.postViewUpvoteButton);
        caption = (TextView) findViewById(R.id.postViewCaption);
        userName = (TextView) findViewById(R.id.postViewUserName);
        userNameBelow = (TextView) findViewById(R.id.postViewUserPosted);
        profile = (FrameLayout) findViewById(R.id.postViewProfile);
        downVotes = (TextView) findViewById(R.id.postViewDownvote);
        upVotes = (TextView) findViewById(R.id.postViewUpvote);
        user = Backendless.UserService.CurrentUser();
        gestureDetector = new GestureDetector(new SwipeGestureDetector());

        //navigaion

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 3);
            }
        });
        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(postview.this, mostupvotessearch.class);
                startActivity(i);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(postview.this, profilePage.class);
                startActivity(i);
            }
        });
        //Reports a post
        postViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                dialogBox();
            }
        });
        commentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(postview.this, commentView.class);
                Bundle b = new Bundle();
                b.putString("postID", postO.getObjectId());
                i.putExtras(b);
                startActivity(i);
            }
        });
        upVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AsyncCallback<BackendlessCollection<upvoted>> callback = new AsyncCallback<BackendlessCollection<upvoted>>()
                {
                    @Override
                    public void handleResponse(BackendlessCollection<upvoted> upvotedBackendlessCollection)
                    {
                        List<upvoted> list = upvotedBackendlessCollection.getCurrentPage();
                        if(list.isEmpty())
                        {
                            postO.setNumLikes(postO.getNumLikes() + 1);
                            Backendless.Persistence.save(postO, new AsyncCallback<Post>() {
                                @Override
                                public void handleResponse(Post post)
                                {
                                    upVotes.setText(Integer.toString(post.getNumLikes()));
                                }

                                @Override
                                public void handleFault(BackendlessFault backendlessFault) {

                                }
                            });
                            upvoted uv = new upvoted();
                            uv.setPostid(postO.getObjectId());
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
                query.setWhereClause("postid = '" + postO.getObjectId() + "' AND userid = '" + user.getObjectId() + "'");
                query.setQueryOptions(qo);
                Backendless.Data.of(upvoted.class).find(query, callback);


                //adds one upvote to the post and updates it
                Log.i("C", "CLICKED");

            }
        });
        downVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //adds one downvote to the post and updates it
                Log.i("C", "CLICKED");
                postO.setNumDislikes(postO.getNumDislikes() + 1);
                Backendless.Persistence.save(postO, new AsyncCallback<Post>() {
                    @Override
                    public void handleResponse(Post post) {
                        downVotes.setText(Integer.toString(post.getNumDislikes()));
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {

                    }
                });
            }
        });
        /*
        post.setOnTouchListener(new OnSwipeTouchListener(postview.this)
        {
            public void onSwipeRight() {
                Log.i("swipe", "right");
                postO.setNumLikes(postO.getNumLikes() + 1);
                Backendless.Persistence.save(postO, new AsyncCallback<Post>() {
                    @Override
                    public void handleResponse(Post post) {
                        upVotes.setText(Integer.toString(post.getNumLikes()));
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {

                    }
                });
            }
            public void onSwipeLeft() {
                Log.i("swipe", "left");
                postO.setNumDislikes(postO.getNumDislikes() + 1);
                Backendless.Persistence.save(postO, new AsyncCallback<Post>() {
                    @Override
                    public void handleResponse(Post post) {
                        downVotes.setText(Integer.toString(post.getNumDislikes()));
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {

                    }
                });
            }
        });
        */


    }
    public void dialogBox() {
        //creates intent ot move to after reporting
        final Intent i = new Intent(postview.this, profilePage.class);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to report this post? It will be reviewed by us and either put back up or removed permanently.");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        //saves the post with a flag that marks it as reported
                        postO.setisReported(true);
                        Backendless.Persistence.save(postO, new AsyncCallback<Post>() {
                            @Override
                            public void handleResponse(Post post)
                            {
                                Log.i("TEST", post.getisReported().toString());
                                Toast.makeText(postview.this, "Succesfully reported", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {
                                Toast.makeText(postview.this, "Did not report", Toast.LENGTH_LONG).show();
                                Log.e("ERROR:", backendlessFault.getMessage());
                            }
                        });
                        startActivity(i);
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        arg0.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public boolean onTouchEvent(MotionEvent event)
    {
        if(gestureDetector.onTouchEvent(event))
        {
            return true;
        }
        return super.onTouchEvent(event);
    }
    private void onRightSwipe()
    {
        //Activated when swiped right
        Log.i("Right", "Swiped");
        final int num = postO.getNumLikes();
        AsyncCallback<BackendlessCollection<upvoted>> callback = new AsyncCallback<BackendlessCollection<upvoted>>()
        {
            @Override
            public void handleResponse(BackendlessCollection<upvoted> upvotedBackendlessCollection)
            {
                List<upvoted> list = upvotedBackendlessCollection.getCurrentPage();
                if(list.isEmpty())
                {
                    backOfPost.setBackgroundResource(R.drawable.whitetoblue);
                    TransitionDrawable transition = (TransitionDrawable) backOfPost.getBackground();
                    transition.startTransition(1000);
                    postO.setNumLikes(num + 1);
                    Backendless.Persistence.save(postO, new AsyncCallback<Post>() {
                        @Override
                        public void handleResponse(Post post)
                        {
                            upVotes.setText(Integer.toString(post.getNumLikes()));
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {

                        }
                    });
                    upvoted uv = new upvoted();
                    uv.setPostid(postO.getObjectId());
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
        query.setWhereClause("postid = '" + postO.getObjectId() + "' AND userid = '" + user.getObjectId() + "'");
        query.setQueryOptions(qo);
        Backendless.Data.of(upvoted.class).find(query, callback);


        //adds one upvote to the post and updates it
        Log.i("C", "CLICKED");

    }
    private void onLeftSwipe()
    {
        Log.i("Left", "Swipe");
        int numl = postO.getNumDislikes();
        postO.setNumDislikes(numl + 1 );
        backOfPost.setBackgroundResource(R.drawable.whitetoorange);
        TransitionDrawable transition = (TransitionDrawable) backOfPost.getBackground();
        transition.startTransition(1000);
        downVotes.setText(Integer.toString(numl + 1));

        Backendless.Persistence.save(postO, new AsyncCallback<Post>() {
            @Override
            public void handleResponse(Post post) {

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {

            }
        });
        user.setProperty("numDislikes", (Integer) user.getProperty("numDislikes") + 1);
        util.updateUser(user);
    }
    //private class for gestures
    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        //makes swipes shorter or longer
        private static final int SWIPE_MIN_DISTANCE = 100;
        private static final int SWIPE_MAX_OFF_PATH = 100;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            try
            {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();
                if (diffAbs > SWIPE_MAX_OFF_PATH)
                {
                    return false;
                }
                //left swipe
                if (diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
                {
                    postview.this.onLeftSwipe();
                }
                //right swipe
                else if (-diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
                {
                    postview.this.onRightSwipe();
                    // backOfPost.setBackgroundResource(R.drawable.whitetoblue);

                }
            }
            catch (Exception e)
            {
                Log.e("postview", "Error on strech gestures");
                //Log.e("Eception", "" + e.getMessage());
                Log.e("Eception", e.getStackTrace().toString());
            }
            return false;
        }

    }
}


