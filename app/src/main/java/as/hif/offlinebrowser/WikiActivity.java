package as.hif.offlinebrowser;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class WikiActivity extends AppCompatActivity {

    IntentFilter intentFilter;
    FileOutputStream fos;
    OutputStreamWriter osw;
    String webData;
    public static String keyWord;


    private BroadcastReceiver intentReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context,Intent intent ) {
            WebView wv = (WebView) findViewById(R.id.vwebView);
            final String mimeType = "text/html";
            final String encoding = "UTF-8";
            webData = intent.getExtras().getString("sms");
            wv.loadData(webData, mimeType, encoding);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki);

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        EditText edt2;
        final Button btn_wiki;
        edt2 = (EditText) findViewById(R.id.editText2);
        btn_wiki  = (Button) findViewById(R.id.btn_wikisearch);

        WebView wv = (WebView) findViewById(R.id.vwebView);
        wv.loadUrl("file:///android_asset/wiki.html");
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    /*    Transition enterTrans = new Explode();
        getWindow().setEnterTransition(enterTrans);

        Transition returnTrans = new Slide();
        getWindow().setReturnTransition(returnTrans);
*/
        final EditText editText2 = (EditText) findViewById(R.id.editText2);
        editText2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        editText2.setVisibility(View.GONE);
                        btn_wiki.setVisibility(View.GONE);
                        forwardRequest(editText2.getText().toString());
                    } catch (Exception e) {
                        //
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }

        });
        btn_wiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (editText2.getText().toString().isEmpty()) {
                        Toast.makeText(getApplication(), "Enter a search query", Toast.LENGTH_SHORT).show();
                    } else
                        forwardRequest(editText2.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        editText2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        if (!(editText2.getText().toString().isEmpty()))
                            forwardRequest(editText2.getText().toString());
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
        getMenuInflater().inflate(R.menu.menu_wiki, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Button btn;
        btn = (Button) findViewById(R.id.btn_wikisearch);
        EditText edt_wiki;
        edt_wiki = (EditText) findViewById(R.id.editText2);
        WebView wiki = (WebView) findViewById(R.id.vwebView);
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_save) {
            if (edt_wiki.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Nothing to Save",Toast.LENGTH_SHORT).show();
            }
            else {
                save();
            }

        }
        if (id == R.id.action_reset) {
            wiki.loadUrl("file:///android_asset/wiki.html");
            edt_wiki.setText("");
            edt_wiki.setVisibility(View.VISIBLE);
            btn.setVisibility(View.VISIBLE);

        }

        return super.onOptionsItemSelected(item);
    }


    public void save() {
        EditText ed2 = (EditText) findViewById(R.id.editText2);
        keyWord = ed2.getText().toString();
        String fileName ="Wikipedia Document of " + keyWord;
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

    public void forwardRequest(String whatToSend) throws Exception {
        EditText EDT2 = (EditText) findViewById(R.id.editText2);
        Button btn_wiki = (Button) findViewById(R.id.btn_wikisearch);
        WebView wv = (WebView) findViewById(R.id.vwebView);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String extractorType = sharedPreferences.getString("extractorType", "Article Extractor");
        String outputMode = sharedPreferences.getString("outputType", "Plain Text");
        wv.loadUrl("file:///android_asset/src/wait.html");
        Toast.makeText(getApplication(), "Initiating Communication with Server", Toast.LENGTH_LONG).show();
        EDT2.setVisibility(View.GONE);
        btn_wiki.setVisibility(View.GONE);
        String send_msg = "http://offlinebrowser-web.appspot.com/ExtractServlet?url=https://en.wikipedia.org/wiki/"+whatToSend+"&OutputType=1&ExtractorType=1";
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("9947753535", null, send_msg, null, null);
    }

    @Override
    public void onBackPressed() {
//        Intent goBackIntent = new Intent(this,MainActivity.class);
//        startActivity(goBackIntent);
        super.onBackPressed();
    }
    public static void forwardRequestTest(String whatToSend) throws Exception{
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("9947753535", null, whatToSend, null, null);
    }
}
