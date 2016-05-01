package fbla.com.fbla_app_src;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
    ImageView upVoteButton;
    ImageView downVoteButton;
    ImageView commentsButton;
    ImageView report;
    FrameLayout search;
    FrameLayout add;
    FrameLayout profile;
    ImageView post;
    BackendlessUser user;
    Post postO;
    DownloadImageClass DLC = new DownloadImageClass();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //get views
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        String PostID = extras.get("postID").toString();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postview);
        post = (ImageView) findViewById(R.id.picturePost);
        add = (FrameLayout) findViewById(R.id.postViewAdd);
        search = (FrameLayout) findViewById(R.id.postViewSearch);
        report = (ImageView) findViewById(R.id.postViewReport);
        downVoteButton = (ImageView) findViewById(R.id.postViewDownvoteButton);
        upVoteButton = (ImageView) findViewById(R.id.postViewUpvoteButton);
        caption = (TextView) findViewById(R.id.postViewCaption);
        userName = (TextView) findViewById(R.id.postViewUserName);
        userNameBelow = (TextView) findViewById(R.id.postViewUserPosted);
        commentsButton = (ImageView) findViewById(R.id.postViewComment);
        profile = (FrameLayout) findViewById(R.id.postViewProfile);
        downVotes = (TextView) findViewById(R.id.postViewDownvote);
        upVotes = (TextView) findViewById(R.id.postViewUpvote);
        user = Backendless.UserService.CurrentUser();

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
        commentsButton.setOnClickListener(new View.OnClickListener()
        {
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
                //adds one upvote to the post and updates it
                Log.i("C", "CLICKED");
                AsyncCallback<BackendlessCollection<upvoted>> callback = new AsyncCallback<BackendlessCollection<upvoted>>() {
                    @Override
                    public void handleResponse(BackendlessCollection<upvoted> upvotedBackendlessCollection) {
                        List<upvoted> list = upvotedBackendlessCollection.getCurrentPage();
                        if (list.isEmpty()) {
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
                            user.setProperty("numlikes", (Integer)user.getProperty("numlikes") + 1);
                            util.updateUser(user);
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
        post.setOnTouchListener(new OnSwipeTouchListener(postview.this)
        {
            public void onSwipeRight() {
                Log.i("swipe", "right");
                AsyncCallback<BackendlessCollection<upvoted>> callback = new AsyncCallback<BackendlessCollection<upvoted>>() {
                    @Override
                    public void handleResponse(BackendlessCollection<upvoted> upvotedBackendlessCollection) {
                        List<upvoted> list = upvotedBackendlessCollection.getCurrentPage();
                        if (list.isEmpty()) {
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






    }
}


