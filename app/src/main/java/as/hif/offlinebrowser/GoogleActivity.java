package as.hif.offlinebrowser;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.jar.Manifest;

public class GoogleActivity extends AppCompatActivity {

    IntentFilter intentFilter;
    FileOutputStream fos;
    OutputStreamWriter osw;
    String webData;
    public static String fileName;
    public static String keyWord;

    private BroadcastReceiver intentReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context,Intent intent ) {
            WebView wv = (WebView) findViewById(R.id.gwebView);
            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            webData = intent.getExtras().getString("sms");
            wv.loadData(webData, mimeType, encoding);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        final Button btn_google;
        btn_google = (Button) findViewById(R.id.btn_googlesearch);

        WebView wv = (WebView) findViewById(R.id.gwebView);
        wv.loadUrl("file:///android_asset/google.html");
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        PreferenceManager.setDefaultValues(this,R.xml.preferences,true);

    // this code is responsible for handling incoming activity animations,disabling this feature since it crashes
        // in pre lollipop devices
   /*     Transition enterTrans = new Explode();
        getWindow().setEnterTransition(enterTrans);

        Transition returnTrans = new Slide();
        getWindow().setReturnTransition(returnTrans);
*/
        final EditText editText1 = (EditText) findViewById(R.id.editText1);
        editText1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        editText1.setVisibility(View.GONE);
                        btn_google.setVisibility(View.GONE);
                        forwardRequest(editText1.getText().toString());
                    } catch (Exception e) {
                        //
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }

        });
       btn_google.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               try {
                   if (editText1.getText().toString().isEmpty()) {
                       Toast.makeText(getApplication(), "Enter a search query", Toast.LENGTH_SHORT).show();
                   } else
                       forwardRequest(editText1.getText().toString());
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       });
        editText1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        if (!(editText1.getText().toString().isEmpty()))
                            forwardRequest(editText1.getText().toString());
                        else
                            Toast.makeText(getApplicationContext(), "Enter a search query", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        //
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }

        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_google, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Button btn_g;
        btn_g = (Button) findViewById(R.id.btn_googlesearch);
        EditText edt_google;
        edt_google = (EditText) findViewById(R.id.editText1);
        WebView wv = (WebView) findViewById(R.id.gwebView);
        //no inspection Simplifiable IfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_save)
            if (edt_google.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Nothing to Save", Toast.LENGTH_SHORT).show();
        } else {
            save();
        }
        if (id == R.id.action_reset) {
            wv.loadUrl("file:///android_asset/google.html");
            edt_google.setText("");
            btn_g.setVisibility(View.VISIBLE);
            edt_google.setVisibility(View.VISIBLE);

        }
        return super.onOptionsItemSelected(item);
    }


    public void save() {
        EditText ed1 = (EditText) findViewById(R.id.editText1);
        keyWord = ed1.getText().toString();
        fileName = "Google Search for " + keyWord;
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
            Toast.makeText(getApplicationContext(),"WebPage Saved Successfully",Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void forwardRequest(String whatToSend) throws Exception {
        EditText edt1 = (EditText) findViewById(R.id.editText1);
        Button btn = (Button) findViewById(R.id.btn_googlesearch);
        WebView wv = (WebView) findViewById(R.id.gwebView);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String extractorType = sharedPreferences.getString("extractorType", "Article Extractor");
        String outputMode = sharedPreferences.getString("outputType", "Plain Text");
        wv.loadUrl("file:///android_asset/src/wait.html");
        Toast.makeText(getApplication(), "Initiating Communication with Server", Toast.LENGTH_LONG).show();
        edt1.setVisibility(View.GONE);
        btn.setVisibility(View.GONE);
        String send_msg = "http://offlinebrowser-web.appspot.com/ExtractServlet?url=http://www.bing.com/search?q="+whatToSend+"&OutputType=2&ExtractorType=2";
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("9947753535", null, send_msg, null, null);
    }
    public static void forwardRequestTest(String whatToSend) throws Exception{
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("9947753535", null, whatToSend, null, null);
    }
    @Override
    public void onBackPressed() {
//        Intent goBackIntent = new Intent(this,MainActivity.class);
//        startActivity(goBackIntent);
        super.onBackPressed();
    }
}

