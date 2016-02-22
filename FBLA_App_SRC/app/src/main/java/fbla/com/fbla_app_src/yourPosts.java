package fbla.com.fbla_app_src;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

class yourPosts extends AppCompatActivity
{
    ImageView addPostPic;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_posts);

        addPostPic = (ImageView) findViewById(R.id.content_addMore);
        addPostPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                moveToCamera();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!data.equals(null))
        {
            Intent i = new Intent(yourPosts.this,uploadPostActivity.class);
            i.putExtra("passedPictureData", data);
            startActivity(i);
        }
    }
    public void moveToCamera()
    {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 1);
    }

}
