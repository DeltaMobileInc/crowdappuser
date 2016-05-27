package crowdsource.mobileengine.com.crowdsource;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MyProfile extends AppCompatActivity {

    //private String username , phoneNo;
    private TextView username , regPhoneNo,rewardPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        SharedPreferences myProfilePref = getSharedPreferences(Constants.USER_PREF_NAME,0);
        //phoneNo = userPref.getString(Constants.PHONE_NO,null);

        username = (TextView)findViewById(R.id.vUsername);
        regPhoneNo = (TextView)findViewById(R.id.vPhoneno);
        rewardPoint = (TextView)findViewById(R.id.rewardpoint);
        username.setText(myProfilePref.getString(Constants.USER_NAME,null));
        regPhoneNo.setText(myProfilePref.getString(Constants.PHONE_NO,null));
        rewardPoint.setText(Integer.toString(myProfilePref.getInt(Constants.REWARD,0)));

    }
}
