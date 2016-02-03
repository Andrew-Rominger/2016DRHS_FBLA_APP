package fbla.com.fbla_app_src;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.backendless.*;
import com.backendless.async.callback.BackendlessCallback;

public class signupEmail extends AppCompatActivity
{
    ImageView goBack;
    BackendlessUser user;
    EditText email;
    EditText password;
    Button signUp;
    EditText passwordCheck;
    CheckBox showPW;
    EditText userName;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        goBack = (ImageView) findViewById(R.id.goBackPic);
        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passwordField);
        passwordCheck = (EditText) findViewById(R.id.passwordFieldCheck);
        signUp = (Button) findViewById(R.id.signUp);
        showPW = (CheckBox) findViewById(R.id.shwoPW);
        userName = (EditText)findViewById(R.id.userNameField);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent goBackToSignIn = new Intent(signupEmail.this, MainActivity.class);
                startActivity(goBackToSignIn);

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                signUp(v);
            }
        });
        showPW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onCheckBoxClicked(v);
            }
        });





    }
    public void onCheckBoxClicked(View view)
    {
        boolean checked = ((CheckBox) view).isChecked();

        if(checked)
        {
            password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            password.setSelection(password.getText().length());
        }
        else
        {
            password.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);

            password.setSelection(password.getText().length());


        }


    }



    public void signUp(View v)
    {
        if(!password.getText().equals(passwordCheck.getText()))
        {
            user = new BackendlessUser();
            user.setEmail(email.getText().toString().toLowerCase());
            user.setPassword(password.getText().toString().toLowerCase());
            user.setProperty("userName", userName.getText().toString());

            Backendless.UserService.register(user, new BackendlessCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser backendlessUser) {
                    Toast.makeText(getApplicationContext(), "Account created, returning to login screen", Toast.LENGTH_SHORT).show();
                }
            });
            startActivity(new Intent(signupEmail.this, MainActivity.class));

        }
        else
        {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            email.setText("");
            password.setText("");

        }


    }



}
