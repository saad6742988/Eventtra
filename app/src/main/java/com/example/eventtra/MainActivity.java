package com.example.eventtra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        //19f-0220 i am saad
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
        mAuth.signInWithEmailAndPassword("saad@gmail.com","ok123456ok").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                    Log.d("login","done");

                }else{
                    Toast.makeText(MainActivity.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("login","not done");
                }
            }

        });

//        shift to registration page
//        Intent intent = new Intent(this, Registration.class);
//        startActivity(intent);
//        Intent intent = new Intent(this, AdminPage.class);
//        startActivity(intent);

        Intent intent = new Intent(this,OtpVerification.class);
        startActivity(intent);
    }
}