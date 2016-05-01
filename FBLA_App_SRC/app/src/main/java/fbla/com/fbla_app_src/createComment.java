package fbla.com.fbla_app_src;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class createComment extends AppCompatActivity
{
    // declare variables
    Intent i;
    Bundle b;
    String postID;
    EditText field;
    Button add;
    Comment comment = new Comment();
    BackendlessUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);
        i = getIntent();
        b = i.getExtras();
        postID = b.getString("postID");
        user = Backendless.UserService.CurrentUser();


        field = (EditText)findViewById(R.id.commentContentField);
        add = (Button) findViewById(R.id.addCommentButton);
        //listner for add comment
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!field.getText().toString().equals(""))
                {
                    //saves new commnent
                    comment.setContent(field.getText().toString());
                    comment.setUseruploaded(user.getUserId());
                    comment.setPostid(postID);
                    Backendless.Data.save(comment, new AsyncCallback<Comment>()
                    {
                        @Override
                        public void handleResponse(Comment comment)
                        {
                            Toast.makeText(createComment.this,"Succesfully created comment", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {

                        }
                    });
                }
            }
        });

    }

}
