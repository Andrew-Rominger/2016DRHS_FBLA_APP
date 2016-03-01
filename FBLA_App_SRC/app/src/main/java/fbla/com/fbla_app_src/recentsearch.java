package fbla.com.fbla_app_src;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class recentsearch extends AppCompatActivity {

    FrameLayout search;
    FrameLayout add;
    FrameLayout profile;
    RelativeLayout upVote;
    RelativeLayout trending;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recentsearch);

        search = (FrameLayout) findViewById(R.id.profilepage_searchNav);
        add = (FrameLayout) findViewById(R.id.profilepage_addNav);
        profile = (FrameLayout) findViewById(R.id.profilepage_profileNav);
        upVote = (RelativeLayout) findViewById(R.id.upVote);
        trending = (RelativeLayout) findViewById(R.id.trending);

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
                Intent i = new Intent(recentsearch.this, uploadPostActivity.class);
                startActivity(i);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(recentsearch.this, profilePage.class);
                startActivity(i);
            }
        });
    }

}
