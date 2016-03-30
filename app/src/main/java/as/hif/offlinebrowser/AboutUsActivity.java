package as.hif.offlinebrowser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    @Override
    public void onBackPressed() {
//        Intent goBackIntent = new Intent(this,MainActivity.class);
//        startActivity(goBackIntent);
        super.onBackPressed();
    }
}
