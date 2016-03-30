package as.hif.offlinebrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

public class openSavedPagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_saved_pages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String fileToOpen = "";
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            fileToOpen = extras.getString("filePath");
        }

        WebView disp = (WebView) findViewById(R.id.webViewDisplay);
        disp.loadUrl("file://"+fileToOpen);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
//        Intent goBackIntent = new Intent(this,fileListActivity.class);
//        startActivity(goBackIntent);
        super.onBackPressed();
    }
}
