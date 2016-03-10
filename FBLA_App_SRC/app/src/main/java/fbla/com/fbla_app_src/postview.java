package fbla.com.fbla_app_src;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.BackendlessUser;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postview);

        post = (ImageView) findViewById(R.id.picturePost);
        profile = (FrameLayout) findViewById(R.id.profilepage_profileNav);
        add = (FrameLayout) findViewById(R.id.profilepage_addNav);
        search = (FrameLayout) findViewById(R.id.profilepage_searchNav);
        report = (ImageView) findViewById(R.id.reportIv);
        commentsButton = (ImageView) findViewById(R.id.commentsIv);
        downVoteButton = (ImageView) findViewById(R.id.downvoteIv);
        upVoteButton = (ImageView) findViewById(R.id.upvotesIv);
        caption = (TextView) findViewById(R.id.captionTv);
        userName = (TextView) findViewById(R.id.userNameTv);
        userNameBelow = (TextView) findViewById(R.id.userNameBelowTv);
        comments = (TextView) findViewById(R.id.commentsTv);
        downVotes = (TextView) findViewById(R.id.downVoteTv);
        upVotes = (TextView) findViewById(R.id.upVoteTv);
        //navigaion
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(postview.this, mostupvotessearch.class);
                startActivity(i);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 3);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(postview.this, profilePage.class);
                startActivity(i);
            }
        });
    }
}


