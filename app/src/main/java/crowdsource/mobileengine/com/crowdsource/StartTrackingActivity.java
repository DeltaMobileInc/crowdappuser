package crowdsource.mobileengine.com.crowdsource;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class StartTrackingActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton selectedBus;
    private Button startTracking;
    private String mUsername,mPhone,mLat,mLng,message,mBusno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tracking);
        radioGroup = (RadioGroup)findViewById(R.id.busgroup);
        SharedPreferences userPref = getSharedPreferences(Constants.USER_PREF_NAME,0);
        mUsername = userPref.getString(Constants.USER_NAME,"-");
        mPhone = userPref.getString(Constants.PHONE_NO,"-");
        mLat = userPref.getString(Constants.USER_LAT,"-");
        mLng = userPref.getString(Constants.USER_LNG,"-");


        startTracking = (Button)findViewById(R.id.stratTrack);
        startTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = radioGroup.getCheckedRadioButtonId();
                selectedBus = (RadioButton)findViewById(selected);

                if(selectedBus == null){
                    Toast.makeText(getApplicationContext()," Please select a bus to track",Toast.LENGTH_SHORT).show();
                } else {


                    if (selectedBus.getText() != null) {
                        SharedPreferences.Editor edit = getSharedPreferences(Constants.USER_PREF_NAME, MODE_PRIVATE).edit();
                        edit.putString(Constants.USER_PREF_BUS_NO, selectedBus.getText().toString());
                        edit.commit();
                        Intent startServiceIntent = new Intent(StartTrackingActivity.this, UserTrackingService.class);
                        startServiceIntent.putExtra(Constants.USER_NAME, mUsername);
                        startServiceIntent.putExtra(Constants.PHONE_NO, mPhone);
                        startServiceIntent.putExtra(Constants.USER_PREF_BUS_NO, selectedBus.getText().toString());
                        MainActivity.setVisibility();
                        startService(startServiceIntent);
                        Toast.makeText(getApplicationContext(), "Bus tracking started for : " + selectedBus.getText(), Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), " Please select a bus to track", Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
    }
}
