package as.hif.offlinebrowser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedBackActivity extends AppCompatActivity {

    String feedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText name = (EditText) findViewById(R.id.input_name);
        final EditText msg = (EditText) findViewById(R.id.input_feedback);

        String nm = name.getText().toString();
        String ms = msg.getText().toString();

        feedBack = nm.concat(ms);


        Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!msg.getText().toString().isEmpty())
                try
                    {
                    String[] email = {
                                      "admin@ashif.me",
                                      "24rd94@gmail.com"
                                     };
                    forwardRequest(email,"FeedBack for Offline Browser",feedBack);
                    Snackbar.make(view, "Thanks for your Feedback!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
               else {
                    Snackbar.make(view, "Field Shouldn't be Empty", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void forwardRequest(String[] destAddress,String emailTitle,String whatToSend) throws Exception {
        feedBack = whatToSend;
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        String[] to = destAddress;
        emailIntent.putExtra(Intent.EXTRA_EMAIL,to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,emailTitle);
        emailIntent.putExtra(Intent.EXTRA_TEXT,whatToSend);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent,"Email"));
    }

    @Override
    public void onBackPressed() {
//        Intent goBackIntent = new Intent(this,MainActivity.class);
//        startActivity(goBackIntent);
        super.onBackPressed();
    }
}
