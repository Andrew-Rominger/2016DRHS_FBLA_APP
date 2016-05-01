package fbla.com.fbla_app_src;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
        setupUI(findViewById(R.id.recoverBg));

        usernameInput = (EditText) findViewById(R.id.usernameInput);
        submit = (Button) findViewById(R.id.submitRecover);
        cancle = (ImageView) findViewById(R.id.goBackRec);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //makes a call to reset the user's password
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
    //this method hides the keyboard
    public static void hideSoftKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    //this method checks to see if the user has clicked outside of the edit text when the key board is up, if they do the hideSoftKeyboard() method is called
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(recoverPassword.this);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

}
