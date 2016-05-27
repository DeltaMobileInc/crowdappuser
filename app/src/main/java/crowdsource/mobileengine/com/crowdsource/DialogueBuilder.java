package crowdsource.mobileengine.com.crowdsource;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by praveen on 4/24/2016.
 */
public class DialogueBuilder extends DialogFragment implements View.OnClickListener {
    public interface AuthenticationDialogListener {
        void onAuthenticationLoginClicked(String username, String password);

        void onAuthenticationResetClicked(View view, String username);
    }

    private AuthenticationDialogListener mListener;

    private EditText mUsername;
    private EditText mPhone;
    private EditText mServerPhone;
    private Button mCancel;
    private Button mLogin;
    private String mPhoneNumber;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TelephonyManager tMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String getNoFromPhone = tMgr.getLine1Number();
        String[] spiltString =  getNoFromPhone.split("1",2);
        mPhoneNumber = spiltString[1];


        View view = inflater.inflate(R.layout.dialogue_builder_layout, container);
        this.getDialog().setTitle("Please Register");

        mUsername = (EditText) view.findViewById(R.id.username_field);
        mPhone = (EditText) view.findViewById(R.id.password_field);
       // mServerPhone = (EditText)view.findViewById(R.id.servernumber);
        mCancel = (Button) view.findViewById(R.id.reset_button);
        mLogin = (Button) view.findViewById(R.id.login_button);

        mCancel.setOnClickListener(this);
        mLogin.setOnClickListener(this);

        return view;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AuthenticationDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement AuthenticationDialogListener");
        }
    }

    public void onClick(View v) {
        if (v.equals(mLogin)) {
             if(mPhoneNumber == null || mPhoneNumber == "")
            {
                Toast.makeText(getActivity(), "Please register with a valid network provider", Toast.LENGTH_SHORT).show();
                return;
            }
             else if (mUsername.getText().toString().length() < 1) {
                Toast.makeText(getActivity(), "Please Enter a valid username", Toast.LENGTH_SHORT).show();

                return;
            } else if (!mPhoneNumber.equalsIgnoreCase(mPhone.getText().toString())) {
                Toast.makeText(getActivity(), "Please check your phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            else {
                mListener.onAuthenticationLoginClicked(mUsername.getText().toString(), mPhone.getText().toString());
                this.dismiss();
            }
        } else if (v.equals(mCancel)) {
            mListener.onAuthenticationResetClicked(v, mUsername.getText().toString());
        }
    }
}