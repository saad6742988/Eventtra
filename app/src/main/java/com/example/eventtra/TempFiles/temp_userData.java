package com.example.eventtra.TempFiles;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eventtra.AllUsers.Login;
import com.example.eventtra.R;

import java.util.regex.Matcher;


public class temp_userData extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment registration_userData.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static registration_userData newInstance(String param1, String param2) {
//        registration_userData fragment = new registration_userData();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public registration_userData() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        if (getArguments() != null) {
////            mParam1 = getArguments().getString(ARG_PARAM1);
////            mParam2 = getArguments().getString(ARG_PARAM2);
////        }
//
//    }

    public EditText fnameText,lnameText,emailText,phoneText,passwordText,cpasswordText;
    public Button registerBtn;
    public TextView loginView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.temp_user_data, container, false);
        registerBtn=view.findViewById(R.id.registerBtn);
        fnameText=view.findViewById(R.id.fnameBox);
        lnameText=view.findViewById(R.id.lnameBox);
        emailText=view.findViewById(R.id.emailBox);
        phoneText=view.findViewById(R.id.phoneBox);
        passwordText=view.findViewById(R.id.passwordBox);
        cpasswordText=view.findViewById(R.id.cpasswordBox);
        loginView=view.findViewById(R.id.loginV);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registeration(view);
            }
        });
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Login.class);
                getActivity().startActivity(i);
                getActivity().finish();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    public void registeration(View v) {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String cpassword = cpasswordText.getText().toString();
        String fname = fnameText.getText().toString();
        String lname = lnameText.getText().toString();
        String phone = phoneText.getText().toString();
        boolean validData = true;

        //Pattern emailPattern=Patterns.EMAIL_ADDRESS;
        Matcher emailMatcher = Patterns.EMAIL_ADDRESS.matcher(email);
        Boolean emailValidate = emailMatcher.matches();


        /// Validating data
        if (fname.isEmpty()) {
            fnameText.setError("First Name Required");
            fnameText.requestFocus();
            validData = false;
        }
        if (lname.isEmpty()) {
            lnameText.setError("Last Name Required");
            lnameText.requestFocus();
            validData = false;
        }
        if (phone.length() != 11) {
            phoneText.setError("Invalid Phone");
            phoneText.requestFocus();
            validData = false;
        }
        if (emailValidate == false) {
            //otp check
            emailText.setError("Invalid Email");
            emailText.requestFocus();
            validData = false;
        }
        if (password.length() < 8) {
            passwordText.setError("Password Must Be At Least 8 Characters Long");
            passwordText.requestFocus();
            validData = false;
        }
        if (!password.equals(cpassword)) {
            Log.d(password, cpassword);
            cpasswordText.setError("Password Doesn't Match");
            cpasswordText.requestFocus();
            validData = false;
        }

        if (validData) {
            Bundle userData=new Bundle();
            userData.putString("fname",fname);
            userData.putString("lname",lname);
            userData.putString("email",email);
            userData.putString("phone",phone);
            userData.putString("password",password);
            temp_otpVerification otpVerify = new temp_otpVerification();
            otpVerify.setArguments(userData);
            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.register_fragment_container, otpVerify).addToBackStack(null).commit();


        }
//        mAuth.createUserWithEmailAndPassword("saad@gmail.com","ok123456ok").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(MainActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
//                    Log.d("Res","Done");
//                }else{
//                    Toast.makeText(MainActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.d("Res","Not Done");
//                }
//            }
//        });

    }

    public void gotoOtp(View view) {
        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.register_fragment_container, new registration_otpVerification()).addToBackStack(null).commit();
    }



}