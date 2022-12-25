package com.example.eventtra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {

    private EditText fnameText,lnameText,emailText,phoneText,passwordText,cpasswordText;
    private Button registerBtn;
    FirebaseAuth mAuth;

    //To be Implemented11

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth=FirebaseAuth.getInstance();
        fnameText=findViewById(R.id.fnameBox);
        lnameText=findViewById(R.id.lnameBox);
    }
    public void registeration(View v)
    {
//        String email=emailText.getText().toString();
//        String password =passwordText.getText().toString();
//        String cpassword=cpasswordText.getText().toString();
//
//        /// Validating data
//        if(email.isEmpty()|| Patterns.EMAIL_ADDRESS.matcher(email).matches())
//        {
//            //otp check
//            emailText.setError("Invalid Email");
//            emailText.requestFocus();
//        }
//        if(password.isEmpty())
//        {
//            passwordText.setError("Password Required");
//            passwordText.requestFocus();
//        }
//        if(cpassword.isEmpty()||password!=cpassword)
//        {
//            cpasswordText.setError("Password Doesn't Match");
//            cpasswordText.requestFocus();
//        }
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
}