package crowdsource.mobileengine.com.crowdsource;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by praveen on 4/25/2016.
 */
public class UserTrackingService extends Service {

    private Location sLocation;
    private LocationManager sLocationManager;
    private TrackingLocationListener sLocationListner;
    private  final  String USER_NAME = "username";
    private  final  String PHONE_NO = "phoneno";
    private  final String USER_PREF_BUS_NO = "busno";
    private String mName,mPhoneNo,mBusNo;
    @Override
    public void onCreate() {
        super.onCreate();
    }



    @Override
    public void onStart(Intent intent, int startId) {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mName=  intent.getExtras().getString(USER_NAME);
        mPhoneNo = intent.getExtras().getString(PHONE_NO);
        mBusNo = intent.getExtras().getString(USER_PREF_BUS_NO);


        sLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sLocationListner = new TrackingLocationListener();
        //Check Permission

        if(checkPermission())
        {
            sLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 40000, 0, sLocationListner);
            sLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 40000, 0, sLocationListner);
        }
        else {
            onDestroy();
        }

        return Service.START_NOT_STICKY;
}




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class TrackingLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            sLocation = location;
            Toast.makeText(getApplicationContext(), "Speed"+sLocation.getSpeed(), Toast.LENGTH_SHORT).show();
            SmsManager smsManager = SmsManager.getDefault();

            String message = "4/"+mPhoneNo+"/"+mName+"/"+mBusNo+"/"+sLocation.getLatitude()+"/"+sLocation.getLongitude()+"/"+sLocation.getSpeed();

            smsManager.sendTextMessage(Constants.SERVER_PHONE, null, message, null, null);

           /* Toast.makeText(getApplicationContext(), "Loaction "+ sLocation.getLatitude() +" :: "+sLocation.getLongitude()
                    +" speed "+sLocation.getSpeed(), Toast.LENGTH_SHORT).show();*/
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(getApplicationContext(), "onStatusChanged : Provider", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "onProviderEnabled : GPS Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "onProviderDisabled : GPS Disabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(checkPermission()){
            sLocationManager.removeUpdates(sLocationListner);
        }
        stopSelf();

    }

    private boolean checkPermission () {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            return true;
        }
        else
        {
            return false;
        }

    }

}
