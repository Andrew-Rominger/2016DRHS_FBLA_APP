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

public class trendingsearch extends AppCompatActivity {

    FrameLayout search;
    FrameLayout add;
    FrameLayout profile;
    RelativeLayout upVote;
    RelativeLayout recent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trendingsearch);

        search = (FrameLayout) findViewById(R.id.profilepage_searchNav);
        add = (FrameLayout) findViewById(R.id.profilepage_addNav);
        profile = (FrameLayout) findViewById(R.id.profilepage_profileNav);
        upVote = (RelativeLayout) findViewById(R.id.upVote);
        recent = (RelativeLayout) findViewById(R.id.recent);

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
        //navigaion
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
                Intent i = new Intent(trendingsearch.this, uploadPostActivity.class);
                startActivity(i);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(trendingsearch.this, profilePage.class);
                startActivity(i);
            }
        });
    }

}
