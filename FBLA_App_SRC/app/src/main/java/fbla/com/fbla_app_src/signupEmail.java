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
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class signupEmail extends AppCompatActivity
{
    ImageView goBack;
    EditText emailInput;
    EditText passwordInput;
    Button signUp;
    CharSequence userName;
    EditText passwordCheck;
    CheckBox showPW;
    EditText userNameInput;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Buttons, images
        goBack = (ImageView) findViewById(R.id.goBackPic);
        signUp = (Button) findViewById(R.id.signUp);
        showPW = (CheckBox) findViewById(R.id.shwoPW);
        //Edit Text




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
        /*
        showPW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onCheckBoxClicked(v);
            }
        });
        */




    }
    public void onCheckBoxClicked(View view)
    {
        boolean checked = ((CheckBox) view).isChecked();
        if(checked)
        {
            passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordInput.setSelection(passwordInput.getText().length());
        }
        else
        {
            passwordInput.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordInput.setSelection(passwordInput.getText().length());
        }
    }

    public void signUp(View v)
    {
        userNameInput = (EditText) findViewById(R.id.userNameFieldSignup);
        passwordInput = (EditText) findViewById(R.id.passwordFieldSignup);
        emailInput = (EditText) findViewById(R.id.emailFieldSignup);
        passwordCheck = (EditText) findViewById(R.id.passwordFieldCheckSignup);

        CharSequence email = emailInput.getText();
        CharSequence password = passwordInput.getText();
        userName = userNameInput.getText();
        boolean isValidEmail = fbla.com.fbla_app_src.Validator.isEmailValid(getApplicationContext(), email);
        boolean isValidPassword = fbla.com.fbla_app_src.Validator.isPasswordValid(getApplicationContext(), password);
        boolean isValidName = fbla.com.fbla_app_src.Validator.isNameValid(getApplicationContext(), userName);
        if(isValidEmail && isValidPassword && isValidName)
        {
            registerUser(userName.toString(), email.toString(), password.toString(), new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser backendlessUser) {
                    Toast.makeText(signupEmail.this, "User " + userName.toString() + " created. Retuning to login screen.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(signupEmail.this, MainActivity.class));
                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {
                    Toast.makeText(signupEmail.this, backendlessFault.getCode().toString(), Toast.LENGTH_LONG).show();
                }
            });


        }


    }
    public void registerUser( String userName, String email, String password, AsyncCallback<BackendlessUser> registrationCallback )
    {
        BackendlessUser user = new BackendlessUser();
        user.setEmail( email );
        user.setPassword( password );
        user.setProperty( "userName", userName );
        user.setProperty("firstTimeLogin", true);

        //Backendless handles password hashing by itself, so we don't need to send hash instead of plain text
        Backendless.UserService.register( user, registrationCallback );
    }





}
