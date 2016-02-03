package fbla.com.fbla_app_src;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.persistence.local.UserTokenStorageFactory;


public class MainActivity extends AppCompatActivity {
    private Button EmailSU;
    private TextView moveToSignIn;

    Window window;

    BackendlessUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String appVersion = "v1";
        Backendless.initApp(this, "67BF989E-7E10-5DB8-FFD7-C9147CA4F200", "12F047DB-382A-F6DA-FF16-C6A0A1F0CE00", appVersion);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {window.setStatusBarColor(Color.RED);}

        EmailSU = (Button) findViewById(R.id.signupEmailButton);
        EmailSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               moveToEmailSignUp();
            }
        });
        moveToSignIn = (TextView) findViewById(R.id.moveToSignIn);
        moveToSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent movetosignIn = new Intent(MainActivity.this, signIn.class);
                startActivity(movetosignIn);
            }
        });

    }
    public void moveToEmailSignUp()
    {
        Intent moveToSU = new Intent(MainActivity.this, signupEmail.class);
        startActivity(moveToSU);
    }




}


