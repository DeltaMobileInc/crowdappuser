package crowdsource.mobileengine.com.crowdsource;


import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements DialogueBuilder.AuthenticationDialogListener , GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private SharedPreferences userPref;
    private SharedPreferences.Editor editor,locEditor;
    private String mUsername,mPhone,mPhoneNumberFromDevice;

    private PendingIntent sentIntent,deliveryIntent;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private static ImageButton startButton,stopButton;
    private static TextView warningText,menuText3;
    private static ViewGroup mainview;
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       /* TelephonyManager tMgr = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumberFromDevice = tMgr.getLine1Number();*/
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        startButton = (ImageButton)findViewById(R.id.btnStartTrace);
        stopButton = (ImageButton)findViewById(R.id.btnStopTrace);
        warningText = (TextView)findViewById(R.id.warningtextview);
        menuText3 = (TextView)findViewById(R.id.trackandstoptext);
        mainview = (ViewGroup)findViewById(R.id.viewGroup);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.serverno:
                //startActivity(new Intent(this,LogActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new DialogueBuilder();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    private void sendSMS(String username , String phoneNo){

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";


        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "Registration request sent to server",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "Please register with a network provider to use this app",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Please try again you device radio is off/ or incompatible",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "Server is processing your request...",
                                Toast.LENGTH_SHORT).show();


                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "Please try again application request failed",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        //send sms
        String resultMssg = phoneNo+"/"+username;

        SmsWorker.sendingSMStoServer(resultMssg,sentIntent,deliveredIntent );



    }

    public void onClicked(View view){

        switch (view.getId()){
            case R.id.btnBus :  startActivity(new Intent(this,Buses.class));
                break;
            case R.id.btnReward :  startActivity(new Intent(this,MyProfile.class));
                break;
            case R.id.btnStartTrace :
                                  /*  Intent startServiceIntent = new Intent(MainActivity.this,UserTrackingService.class);
                                    userPref = getSharedPreferences(Constants.USER_PREF_NAME,0);
                                    startServiceIntent.putExtra(Constants.PHONE_NO,userPref.getString(Constants.PHONE_NO,null));
                                    this.startService(startServiceIntent);*/

                                    Intent intent = new Intent(MainActivity.this,StartTrackingActivity.class);
                                    startActivity(intent);

                break;
            case R.id.btnStopTrace :   Toast.makeText(this,"Bus Tracking Stopped",Toast.LENGTH_SHORT).show();
                                        setInvisibility();
                                        Intent stopServiceIntent = new Intent(MainActivity.this,UserTrackingService.class);
                                    this.stopService(stopServiceIntent);
                break;


        }


    }

    /**
     * Method to set view visible on start tracking
     */
    public static void setVisibility(){
        startButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.VISIBLE);
        warningText.setVisibility(View.VISIBLE);
        menuText3.setText("Stop Tracking");
        //mainview.invalidate();
    }

    /**
     * Method to set view invisible on stop tracking
     */
    public static void setInvisibility(){
        stopButton.setVisibility(View.GONE);
        warningText.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
        menuText3.setText("Track And Earn");
    }


    @Override
    public void onAuthenticationLoginClicked(String username, String phoneNo) {
        Log.d(LOG_TAG,"onDialogPositiveClick " );
        //Constants.SERVER_PHONE = serverPhone;
        editor = getSharedPreferences(Constants.USER_PREF_NAME,MODE_PRIVATE).edit();
        editor.putString(Constants.USER_NAME, username.trim());
        editor.putString(Constants.PHONE_NO, phoneNo.trim());
        editor.putInt(Constants.REWARD,Constants.INITIAL_REWARD_POINT);
        editor.commit();

        if(username != null  && phoneNo != null)
        {
            sendSMS(username,phoneNo);
        }


    }

    @Override
    public void onAuthenticationResetClicked(View view,String username) {
        Log.d(LOG_TAG,"onAuthenticationResetClicked " );
        finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(!checkPermission())
        {
            requestPermission();
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            /*Toast.makeText(MainActivity.this, String.valueOf(mLastLocation.getLatitude())+" & "+
                    String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();*/
            locEditor = getSharedPreferences(Constants.USER_PREF_NAME,MODE_PRIVATE).edit();
            locEditor.putString(Constants.USER_LAT, String.valueOf(mLastLocation.getLatitude()).trim());
            locEditor.putString(Constants.USER_LNG, String.valueOf(mLastLocation.getLongitude()).trim());
            locEditor.commit();

        }
        userPref = getSharedPreferences(Constants.USER_PREF_NAME,0);
        mUsername = userPref.getString(Constants.USER_NAME,null);
        mPhone = userPref.getString(Constants.PHONE_NO,null);

        if (mUsername == null  && mPhone == null)
        {
            showNoticeDialog();
        }



    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG,"onConnectionSuspended " );
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(LOG_TAG,"onConnectionFailed " );
    }

    //Checking Location permission

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(this.getApplicationContext(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this.getApplicationContext(),"Permission Granted, Now you can access location data.",Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(this.getApplicationContext(),"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();


                }
                break;
        }
    }



    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
