package fbla.com.fbla_app_src;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class addComment extends AppCompatActivity
{
    Button add;
    Button cancel;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        add = (Button) findViewById(R.id.addCommentButton);
        input = (EditText) findViewById(R.id.commentContent);
        cancel = (Button) findViewById(R.id.cancelAddComment);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent();
                i.putExtra("result", input.getText().toString());
                setResult(1,i);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent();
                setResult(1,i);
                finish();
            }
        });

    }



}
