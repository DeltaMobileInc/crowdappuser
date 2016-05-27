package crowdsource.mobileengine.com.crowdsource;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.telephony.SmsManager;

/**
 * Created by praveen on 4/24/2016.
 */
public class SmsWorker {

    private BroadcastReceiver receiver;


    public static void sendingSMStoServer(String  mssg, PendingIntent s, PendingIntent  d  ){

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(Constants.SERVER_PHONE, null, mssg, s, d);

    }


}
