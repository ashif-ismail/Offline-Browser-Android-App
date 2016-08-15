package as.hif.offlinebrowser;

/**
 * java class for listening incoming sms
 * developed by Ashif Ismail
 * www.ashif.me
 * admin@ashif.me
 */
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;
import java.util.ArrayList;
import as.hif.offlinebrowser.MainActivity;

public class SMSListener extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //get the SMS message from intent
            SmsMessage[] msgs = null;
            String msg_from;
            String msgBody = "";
            if (bundle != null){
                //retrieve the SMS message received
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        if (msg_from.contains("CDMLAB"))
                        //filtering the sender,so that messages only from backend
                        // is picked up and displayed in the view
                        {
                            msgBody+= msgs[i].getMessageBody();
                            //registering the broadcast
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
                            broadcastIntent.putExtra("sms", msgBody);
                            context.sendBroadcast(broadcastIntent);
                        }
                    }
                }
                catch(Exception e){
                    Log.d("Exception caught", e.getMessage());
                }
            }
        }
    }
}

