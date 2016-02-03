package fbla.com.fbla_app_src;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.UserService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserTokenStorageFactory;

public class signIn extends AppCompatActivity{
    public EditText enterUsername;
    public EditText enterPassword;
    public Button signinButton;
    String userToken;
    BackendlessUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        enterUsername = (EditText) findViewById(R.id.userNameLogin);
        enterPassword = (EditText) findViewById(R.id.passwordLogin);
        signinButton = (Button) findViewById(R.id.signInButton);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CharSequence userName = enterUsername.getText();
                CharSequence password = enterPassword.getText();

                if(isLoginValuesValid(userName, password))
                {
                    LoadingCallback<BackendlessUser> loginCallback = createLoginCallback();
                }

            }
        });



    }
    public void loginUser(String userName, String password, AsyncCallback<BackendlessUser> callback)
    {
        Backendless.UserService.login(userName, password, callback);
    }
    public boolean isLoginValuesValid(CharSequence userName, CharSequence password)
    {
        if(userName != null && password != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


}
