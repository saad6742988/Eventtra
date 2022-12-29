package com.example.eventtra;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

///used for mail sesnding
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


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
    Bundle userData;
    final private FirebaseAuth mAuth=FirebaseAuth.getInstance();;
    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference userCollection = database.collection("User");
    private AlertDialog alertDialog;
    private  MyUser user;
    private CountDownTimer countDownTimer;
    private AlertDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_otp_verification, container, false);
        userData = this.getArguments();
        timerView=view.findViewById(R.id.viewTime);
        topView=view.findViewById(R.id.topText);
        otpText = view.findViewById(R.id.otpBox);
        resendBtn = view.findViewById(R.id.resendBtn);
        alertDialog = new AlertDialog.Builder(getActivity()).create();
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

    private void sendEmail(String otpToSend) {
        final String sender = "eventtra@outlook.com";
        final String password = "saad&6742988";
        String msgToSend = "Hello "+userData.getString("fname")+" "+userData.getString("lname")+
                "!\n\nYour Verification OTP is "+otpToSend+
                "\n\nYou can ignore this Email, if you have not requested register Eventtra";
        Properties prop = new Properties();
        //gmail
//        prop.put("mail.smtp.auth","true");
//        prop.put("mail.smtp.starttls.enable","true");
//        prop.put("mail.smtp.host","smtp.gmail.com");
//        prop.put("mail.smtp.port","587");
        //outlook
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp-mail.outlook.com");
        prop.put("mail.smtp.port", "587");
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender,password);
            }
        });
        try{
            Message m=new MimeMessage(session);
            m.setFrom(new InternetAddress(sender));
            m.setRecipient(Message.RecipientType.TO,new InternetAddress(userData.getString("email")));
            m.setSubject("OTP For Eventtra");
            m.setText(msgToSend);
            Transport.send(m);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity(),"Send Successfully", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d("email status","Send Successfully");

        }catch (MessagingException e)
        {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity(),"System is unable to send OTP.Please Try again later.", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d("email status","System is unable to send OTP.Please Try again later. "+e.getMessage().toString());

            //throw new RuntimeException(e);
        }
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
    }
    private void verifyOtp(String otpEntered) {
        if(otp.equals(otpEntered)) {
            Log.d("verifyOtp: ", "mtched");
            showLoading();
            countDownTimer.cancel();
            timerView.setText("OTP Verified!");
            resendBtn.setEnabled(false);
            otpText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.valid, 0);
            String email = userData.getString("email");
            String password = userData.getString("password");
            String fname = userData.getString("fname");
            String lname = userData.getString("lname");
            String phone = userData.getString("phone");
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
                        Log.d("Registration", "Registration Successful");

                        //create user object
                        user = new MyUser(fname, lname, email, phone);
                        Log.d("user", user.toString());
                        //add user to database
                        addUser();
                    } else {
                        alertDialog.setTitle("Error!");
                        if(task.getException().getMessage()!=null)
                            alertDialog.setMessage("Error: "+task.getException().getMessage()+"\nTry Again.");
                        else
                            alertDialog.setMessage("Check Your Connection.\nTry Again Later!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().getSupportFragmentManager().popBackStackImmediate("register",0);
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        loadingDialog.dismiss();
                        //Toast.makeText(getActivity(), "Registration Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Registration", "Registration UnSuccessful");
                    }
                }
            });
        }
        else
        {
            Log.d("verifyOtp: ", "not mtched");
            otpText.setError("OTP Not Matched");
        }
    }

    private void showLoading() {
        // adding ALERT Dialog builder object and passing activity as parameter
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // layoutinflater object and use activity to get layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(true);

        loadingDialog = builder.create();
        loadingDialog.show();
    }

    private void timer()
    {
        countDownTimer = new CountDownTimer(60000, 1000) {

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendEmail(otp);
            }
        }).start();

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
    public void addUser()
    {
        //Toast to be removed
        userCollection.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                alertDialog.setTitle("Congrats!");
                alertDialog.setMessage("You Are Successfully Registered.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getActivity(),Login.class);
                                getActivity().finish();
                                startActivity(i);
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                loadingDialog.dismiss();
                //Toast.makeText(getActivity(), "Registered Successfully", Toast.LENGTH_LONG).show();
                Log.d("Add DB","Added");
                //documentReference.getId();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alertDialog.setTitle("Error!");
                alertDialog.setMessage("Check Your Connection.\nTry Again Later!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().getSupportFragmentManager().popBackStackImmediate("register",0);
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                loadingDialog.dismiss();
                //Toast.makeText(getActivity(), "User Not Added", Toast.LENGTH_SHORT).show();
                Log.d("Add not DB",e.getMessage());
            }
        });
    }
}