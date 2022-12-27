package com.example.eventtra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OtpVerification extends AppCompatActivity {

    public TextView timerView;
    public EditText otpText;
    public Button resendBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        timerView=findViewById(R.id.viewTime);
        otpText = findViewById(R.id.otpBox);
        resendBtn = findViewById(R.id.resendBtn);
        timer();
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
    public void resendOtp(View view){
        timer();
        resendBtn.setEnabled(false);
    }

}