package fbla.com.fbla_app_src;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class privatepolicy extends AppCompatActivity {
    // Declared global variables
    ImageView button;
    //creates the page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privatepolicy);
        // links variables to xml id representations
        button = (ImageView) findViewById(R.id.back);
        // this code declares that if the user presses the back button, he be taken to account settings page
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(privatepolicy.this, accountsettings.class));
            }
        });
    }

}
