package com.example.eventtra;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class registration_otpVerification extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public registration_otpVerification() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment registration_otpVerification.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static registration_otpVerification newInstance(String param1, String param2) {
//        registration_otpVerification fragment = new registration_otpVerification();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    public TextView timerView,topView;
    public EditText otpText;
    public Button resendBtn;
    private String otp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_otp_verification, container, false);
        Bundle userData = this.getArguments();
        timerView=view.findViewById(R.id.viewTime);
        topView=view.findViewById(R.id.topText);
        otpText = view.findViewById(R.id.otpBox);
        resendBtn = view.findViewById(R.id.resendBtn);
        topView.setText("OTP Email Has Been Sent to "+userData.getString("email")+"\n" +
                "If You Want to Change Your Email You Can Go Back.");
        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpText.setText("");
                otpSender();
                timer();
                resendBtn.setEnabled(false);
            }
        });

        otpSender();
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
        Toast.makeText(getActivity(), userData.getString("email"), Toast.LENGTH_SHORT).show();
        return view;
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
}