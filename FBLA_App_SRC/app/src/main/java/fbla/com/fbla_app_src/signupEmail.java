package fbla.com.fbla_app_src;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class signupEmail extends AppCompatActivity
{
    Button goBack;
    EditText emailInput;
    EditText passwordInput;
    Button signUp;
    CharSequence userName;
    BackendlessUser user;
    EditText passwordCheck;
    Intent moveTo;
    CheckBox showPW;
    EditText userNameInput;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Buttons, images
        goBack = (Button) findViewById(R.id.signupEmail_gobackButton);
        signUp = (Button) findViewById(R.id.signUp);
        showPW = (CheckBox) findViewById(R.id.shwoPW);
        emailInput = (EditText) findViewById(R.id.signupEmail_emailField);
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
            public void onClick(View v) {
                Toast.makeText(signupEmail.this, "Creating user...", Toast.LENGTH_SHORT).show();
                signUp(v);
            }
        });
        /*
        emailInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {

                if (emailInput.getText().toString().length() >= 20)
                {
                    emailInput.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    return true;
                }
                return false;
            }
        });
        */
    }
        /*
        showPW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onCheckBoxClicked(v);
            }
        });
        */

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
        emailInput = (EditText) findViewById(R.id.signupEmail_emailField);
        passwordCheck = (EditText) findViewById(R.id.passwordFieldCheckSignup);

        CharSequence email = emailInput.getText();
        final CharSequence password = passwordInput.getText();
        userName = userNameInput.getText();
        boolean isValidEmail = fbla.com.fbla_app_src.Validator.isEmailValid(getApplicationContext(), email);
        boolean isValidPassword = fbla.com.fbla_app_src.Validator.isPasswordValid(getApplicationContext(), password);

        if(isValidEmail && isValidPassword)
        {
            registerUser(userName.toString(), email.toString(), password.toString(), new AsyncCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser backendlessUser) {
                    Toast.makeText(signupEmail.this, "User " + userName.toString() + " created.", Toast.LENGTH_LONG).show();
                    Backendless.UserService.login(userName.toString(), password.toString(), new AsyncCallback<BackendlessUser>()
                    {
                        @Override
                        public void handleResponse(BackendlessUser backendlessUser) {
                            if((Boolean) backendlessUser.getProperty("firstTimeLogin") == true)
                            {
                                backendlessUser.setProperty("firstTimeLogin", false);
                                moveTo = new Intent(signupEmail.this, extrainfo.class);

                            }
                            else
                            {
                                moveTo = new Intent(signupEmail.this, extrainfo.class);
                            }
                            startActivity(moveTo);

                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {

                        }
                    }, true);

                    Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser backendlessUser) {

                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {

                        }
                    });




                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) {
                    String errorHandle;
                    switch(backendlessFault.getCode())
                    {
                        case "3033":
                            errorHandle = "User Name Taken!";
                            break;
                        case "3040":
                            errorHandle = "Email is in the wrong formay";
                            break;
                        default:
                            errorHandle = "Unknown error.";
                            break;


                    }
                    passwordInput.setText("");
                    passwordCheck.setText("");
                    Toast.makeText(signupEmail.this, errorHandle, Toast.LENGTH_LONG).show();
                }
            });


        }


    }
    public void registerUser( String userName, String email, String password, AsyncCallback<BackendlessUser> registrationCallback )
    {
        BackendlessUser user = new BackendlessUser();
        user.setEmail( email );
        user.setPassword(password);
        user.setProperty("userName", userName);
        user.setProperty("firstTimeLogin", true);

        //Backendless handles password hashing by itself, so we don't need to send hash instead of plain text
        Backendless.UserService.register( user, registrationCallback );
    }





}
