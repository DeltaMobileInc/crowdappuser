package crowdsource.mobileengine.com.crowdsource;

import android.content.Context;

/**
 * Created by praveen on 4/29/2016.
 */
public class BusPOJO {

    private String mBusNo;
    private String mStartPoint;
    private String mEndPoint;
    private String mExpectedTime;
    private Context mContext;

    public BusPOJO(Context context, String busNo, String startPoint, String endPoint, String expectedPoint) {
        mContext = context;
        mBusNo = busNo;
        mStartPoint = startPoint;
        mEndPoint = endPoint;
        mExpectedTime = expectedPoint;
    }

    public String getmExpectedTime() {
        return mExpectedTime;
    }

    public void setmExpectedTime(String mExpectedTime) {
        this.mExpectedTime = mExpectedTime;
    }

    public String getmBusNo() {
        return mBusNo;
    }

    public void setmBusNo(String mBusNo) {
        this.mBusNo = mBusNo;
    }

    public String getmStartPoint() {
        return mStartPoint;
    }

    public void setmStartPoint(String mStartPoint) {
        this.mStartPoint = mStartPoint;
    }

    public String getmEndPoint() {
        return mEndPoint;
    }

    public void setmEndPoint(String mEndPoint) {
        this.mEndPoint = mEndPoint;
    }
}
