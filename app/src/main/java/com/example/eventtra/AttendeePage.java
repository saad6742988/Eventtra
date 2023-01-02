package com.example.eventtra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AttendeePage extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    final private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_page);
        tv=findViewById(R.id.tv);
        GlobalData globalData=(GlobalData) getApplicationContext();
        tv.setText(globalData.getGlobalUser().toString());
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void logout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(this, Login.class);
        finish();
        startActivity(intent);
    }
}