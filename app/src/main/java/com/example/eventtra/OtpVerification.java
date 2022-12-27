package com.example.eventtra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class OtpVerification extends AppCompatActivity {

    public TextView timerView;
    public EditText otpText;
    public Button resendBtn;
//    private HashMap<String,String> optData =new HashMap<String,String>();
    private String otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        timerView=findViewById(R.id.viewTime);
        otpText = findViewById(R.id.otpBox);
        resendBtn = findViewById(R.id.resendBtn);
        otpSender();
//        optData.put("toCheck","");
//        optData.put("toSend","");
        timer();
        otpText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(otpText.getText().toString().length() == 6)
                {
                    verifyOtp(otpText.getText().toString());
                }
                else{
                    otpText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void verifyOtp(String otpEntered) {
        if(otp.equals(otpEntered))
        {
            Log.d("verifyOtp: ", "mtched");
            otpText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.valid, 0);
        }
        else
        {
            Log.d("verifyOtp: ", "not mtched");
            otpText.setError("OTP Not Matched");
        }
    }

    private void timer()
    {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerView.setText("Resend OTP in " + millisUntilFinished / 1000 + " Seconds.");
            }

            public void onFinish() {
                resendBtn.setEnabled(true);
                timerView.setText("Now You Can Resend The OTP.");
            }
        }.start();
    }
    private void otpSender()
    {
        //Email Sending Code Has to BE done Here
        otp=otpGenerator();
        Log.d("OTP Generated", "otpSender: "+otp);
        //Send this otp via mail
    }
    private String otpGenerator()
    {
        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }


    public void resendOtp(View view) {
        otpSender();
        timer();
        resendBtn.setEnabled(false);
    }
}