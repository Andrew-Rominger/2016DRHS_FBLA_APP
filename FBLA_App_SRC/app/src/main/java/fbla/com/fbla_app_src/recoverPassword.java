package fbla.com.fbla_app_src;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class recoverPassword extends AppCompatActivity {
    EditText usernameInput;
    Button submit;
    ImageView cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        usernameInput = (EditText) findViewById(R.id.usernameInput);
        submit = (Button) findViewById(R.id.submitRecover);
        cancle = (ImageView) findViewById(R.id.goBackRec);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Backendless.UserService.restorePassword(usernameInput.getText().toString(), new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void aVoid)
                    {
                        Toast.makeText(recoverPassword.this, "Recovery Email Sent!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault)
                    {
                        Toast.makeText(recoverPassword.this, "Failed to send email", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(recoverPassword.this, signIn.class));
            }
        });

    }

}
