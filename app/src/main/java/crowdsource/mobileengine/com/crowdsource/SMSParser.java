package crowdsource.mobileengine.com.crowdsource;

import android.content.Context;
import android.text.format.Time;
import android.widget.Toast;

/**
 * Created by praveen on 4/29/2016.
 */
public class SMSParser {

    public static void parserSMS(Context context, String smsMssg){
        //HashMap<String,String> messageMap = new HashMap<String, String>();
        String expectedtime,bannerText;
        String[] decryptedStringMssg = smsMssg.split("/");
        int caseCode = Integer.parseInt(decryptedStringMssg[0]);
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        switch(caseCode){
            //user details
            case 1:
                Toast.makeText(context,"registerUser", Toast.LENGTH_SHORT).show();
                break;
            //to set  information about the current bus
            case 2: expectedtime =  decryptedStringMssg[3];
                bannerText  =decryptedStringMssg[4];
                BusDetail.bindValues(expectedtime,bannerText);
                BusDetail.updateViews();
                Toast.makeText(context,"setBusInfo", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                break;
            //to help server track the location of the bus
            case 4:
                Toast.makeText(context,"context,busRideTracker", Toast.LENGTH_SHORT).show();
                break;
        }



    }



}
