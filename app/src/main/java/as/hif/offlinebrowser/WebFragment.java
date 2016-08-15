package as.hif.offlinebrowser;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


/**
 * fragment for displaying main search feature
 * developed by Ashif Ismail
 * www.ashif.me
 * admin@ashif.me
 */
public class WebFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG = "web";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_web, container, false);



    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        WebView wv = (WebView) getActivity().findViewById(R.id.mwebView);
        wv.loadUrl("file:///android_asset/intro.html");
        final WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);

        final EditText webtext = (EditText) getActivity().findViewById(R.id.webtext);
        webtext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        if(!(webtext.getText().toString().isEmpty()))
                        textToServer(webtext.getText().toString());
                        else
                            Toast.makeText(getActivity(), "Enter a web address", Toast.LENGTH_LONG).show();
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
    public void textToServer(String whatToSend) throws Exception {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String extractorType = sharedPreferences.getString("extractorType", "Article Extractor");
        String outputMode = sharedPreferences.getString("outputType", "Plain Text");
        WebView wv = (WebView) getActivity().findViewById(R.id.mwebView);
        wv.loadUrl("file:///android_asset/src/wait.html");
        Toast.makeText(getActivity(), "Initiating Communication with Server", Toast.LENGTH_LONG).show();
        String send_msg = "http://offlinebrowser-web.appspot.com/ExtractServlet?url=http://"+whatToSend+"&OutputType="+outputMode+"&ExtractorType="+extractorType;
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("9947753535", null, send_msg, null, null);

    }

}




