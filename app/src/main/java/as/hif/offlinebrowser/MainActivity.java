package as.hif.offlinebrowser;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import as.hif.offlinebrowser.AppPreferences;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerCallbacks {

    IntentFilter intentFilter;
    FileOutputStream fos;
    OutputStreamWriter osw;
    String webData;
    public static String keyWord;

    private BroadcastReceiver intentReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context,Intent intent ) {
            WebView wv = (WebView) findViewById(R.id.mwebView);
            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            webData = intent.getExtras().getString("sms");
            wv.loadData(webData, mimeType, encoding);
        }
    };
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        // populate the navigation drawer
        mNavigationDrawerFragment.setUserData("Offline Browser", "Access Internet Without Internet");

        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);

     /*   Transition exitTrans = new Explode();
        getWindow().setExitTransition(exitTrans);

        Transition reenterTrans = new Slide();
        getWindow().setReenterTransition(reenterTrans);
   try
        {
            File sdcard = Environment.getExternalStorageDirectory();
            File directory = new File(sdcard.getAbsolutePath() + "/offlinebrowser");
            directory.mkdirs();
            File file = new File(directory, ".");
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);
            osw.write("hi this is offline Browser");
            osw.flush();
            osw.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        } */
    }

    @Override
    protected void onResume() {
        registerReceiver(intentReciever, intentFilter);
        super.onResume();
    }
    @Override
    protected  void onPause() {
        unregisterReceiver(intentReciever);
        super.onPause();
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment;
        switch (position) {
            case 0: fragment = getFragmentManager().findFragmentByTag(WebFragment.TAG);
                if (fragment == null) {
                    fragment = new WebFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.container, fragment, WebFragment.TAG).commit();
                break;
          /*
            i have commented activity transitions code since it crashes the app in pre lollipop devices!
            this is the intent code with transitions....works only in API > 19
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
            Intent intent = new Intent(MainActivity.this, GoogleActivity.class);
            startActivity(intent);
            startActivity(intent, options.toBundle());
         */
            case 1:
                Intent intent = new Intent(MainActivity.this, GoogleActivity.class);
                startActivity(intent);
                break;
            case 2:
                Intent intent1 = new Intent(MainActivity.this, WikiActivity.class);
                startActivity(intent1);
                break;
            case 3:
                Intent intent2 = new Intent(MainActivity.this, fileListActivity.class);
                startActivity(intent2);
                break;
            case 4:
                Intent intent3 = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent3);
                break;
            case 5:
                Intent intent4 = new Intent(MainActivity.this, FeedBackActivity.class);
                startActivity(intent4);
                break;
            case 6:
                Intent intent5 = new Intent(MainActivity.this,as.hif.offlinebrowser.AppPreferences.class);
                startActivity(intent5);
                break;

        }
    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        EditText web = (EditText) findViewById(R.id.webtext);
        if (id == R.id.action_save) {
            if(web.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Nothing to Save",Toast.LENGTH_SHORT).show();
            }
            else {
                save();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void save() {
        EditText web = (EditText) findViewById(R.id.webtext);
        keyWord = web.getText().toString();
        String fileName = "Saved Webpage of " + keyWord;
        String toSave = "<html><head><h3>" + keyWord + "</h3></head></html>";
        toSave += webData;
        try
        {
            File sdcard = Environment.getExternalStorageDirectory();
            File directory = new File(sdcard.getAbsolutePath() + "/offlinebrowser");
            directory.mkdirs();
            File file = new File(directory, fileName);
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);
            osw.write(toSave);
            osw.flush();
            osw.close();
            Toast.makeText(getApplicationContext(),"Webpage Saved Successfully",Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
