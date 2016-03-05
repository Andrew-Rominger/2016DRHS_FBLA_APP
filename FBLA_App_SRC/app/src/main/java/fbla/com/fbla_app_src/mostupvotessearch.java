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

public class mostupvotessearch extends AppCompatActivity {

    FrameLayout search;
    FrameLayout add;
    FrameLayout profile;
    RelativeLayout recent;
    RelativeLayout trending;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostupvotessearch);

        search = (FrameLayout) findViewById(R.id.profilepage_searchNav);
        add = (FrameLayout) findViewById(R.id.profilepage_addNav);
        profile = (FrameLayout) findViewById(R.id.profilepage_profileNav);
        recent = (RelativeLayout) findViewById(R.id.recent);
        trending = (RelativeLayout) findViewById(R.id.trending);

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
                Intent i = new Intent(mostupvotessearch.this, uploadPostActivity.class);
                startActivity(i);
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

}
