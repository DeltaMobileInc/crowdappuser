package crowdsource.mobileengine.com.crowdsource;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BusDetail extends AppCompatActivity {


    private static ImageView loader,bannerImageView;
    private ImageView img;
    private String mUsername,mPhone,mLat,mLng,message ;
    private static String mBusno;
    private static  View bannerView ;
    private static TextView busNo,expArrivalTime,startPoint,endPoint,warningText,bannerTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);
        SharedPreferences userPref = getSharedPreferences(Constants.USER_PREF_NAME,0);
        mUsername = userPref.getString(Constants.USER_NAME,"-");
        mPhone = userPref.getString(Constants.PHONE_NO,"-");
        mLat = userPref.getString(Constants.USER_LAT,"-");
        mLng = userPref.getString(Constants.USER_LNG,"-");
        mBusno = userPref.getString(Constants.USER_PREF_BUS_NO,"-");

        busNo = (TextView)findViewById(R.id.busno);
        startPoint = (TextView)findViewById(R.id.startStop);
        endPoint = (TextView)findViewById(R.id.endstop);
        expArrivalTime = (TextView)findViewById(R.id.arrivaltime);
        warningText = (TextView)findViewById(R.id.warningtextview);
        bannerView = findViewById(R.id.bannerlayout);
        bannerTextView = (TextView)bannerView.findViewById(R.id.bannerText);
        bannerImageView =(ImageView)bannerView.findViewById(R.id.bannerImage);






        RotateAnimation anim = new RotateAnimation(360.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);



        //img.setAnimation(animation);

        sendSMS(mUsername,mPhone);

        loader = (ImageView)findViewById(R.id.loader);
        loader.startAnimation(anim);
        bindValues("Loading .....","NA");
    }

    public static void updateViews(){

        loader.clearAnimation();
        loader.setVisibility(View.GONE);
        warningText.setVisibility(View.GONE);
        bannerImageView.clearAnimation();
       // bannerView.setVisibility(View.GONE);

    }


    //setting values of text fields
    public static void bindValues(String expTime, String bannerText)
    {



        busNo.setText(mBusno.toString());
        expArrivalTime.setText(expTime);
        if(bannerText.equalsIgnoreCase("NA"))
        {
            bannerView.setVisibility(View.GONE);

        }else {
            bannerView.setVisibility(View.VISIBLE);
            bannerTextView.setText(bannerText);
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(bannerImageView, "alpha",  1f, .3f);
            fadeOut.setDuration(1000);
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(bannerImageView, "alpha", .3f, 1f);
            fadeIn.setDuration(1000);

            final AnimatorSet mAnimationSet = new AnimatorSet();

            mAnimationSet.play(fadeIn).after(fadeOut);

            mAnimationSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mAnimationSet.start();
                }
            });
            mAnimationSet.start();
            //bannerImageView.setAnimation(animation);
        }


        switch (mBusno){

            case "RC-10"  :
                startPoint.setText(Constants.START_POINT_1);
                endPoint.setText(Constants.END_POINT_1);
                break;
            case "SB-210" :
                startPoint.setText(Constants.START_POINT_2);
                endPoint.setText(Constants.END_POINT_2);
                break;
            case "CR-71"  :
                startPoint.setText(Constants.START_POINT_3);
                endPoint.setText(Constants.END_POINT_3);
                break;
            default:
                    break;
        }


    }

    // layout to set when sms is sent
    public static void onSmsSend()
    {




    }

    public static void onReceivedSms(String[] message){




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
                        Toast.makeText(getBaseContext(), "Woohooo server has received your request",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "Please register with a network provider to use this app",
                                Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Please try again you device radio is off/ or incompatible",
                                Toast.LENGTH_SHORT).show();
                        finish();
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
                        finish();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        //send sms
        //String message = Integer.toString(Constants.CODE_GET_BUS_DETAIL)+"/"+phoneNo+"/"+username;

        //TODO : Change the location for live location
        //Hard coded location will change in future


        message = Integer.toString(Constants.CODE_GET_BUS_DETAIL)+"/"+mPhone+"/"+mUsername+"/"+mBusno+"/"+Constants.POMONO_LAT+"/"+Constants.POMONO_LNG;
        SmsWorker.sendingSMStoServer(message,sentIntent,deliveredIntent );



    }


}
