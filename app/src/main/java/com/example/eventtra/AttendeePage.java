package com.example.eventtra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AttendeePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    boolean doubleBackToExitPressedOnce = false;
    final private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    TextView tv;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_page);

        drawerLayout = findViewById(R.id.attendee_page_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

       //setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new subEvent_list()).addToBackStack("addEventDetails").commit();
            navigationView.setCheckedItem(R.id.nav_events);
        }

       // tv=findViewById(R.id.toolbar);
        //GlobalData globalData=(GlobalData) getApplicationContext();
       // tv.setText(globalData.getGlobalUser().toString());
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

           /* case R.id.nav_create_event:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new addEventdetails()).addToBackStack("addEventDetails").commit();
                break;

            case R.id.nav_edit_event:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new mainEventList()).commit();
                break;

            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new settings()).commit();
                break;*/

            case R.id.nav_logout:
                mAuth.signOut();
                Intent intent = new Intent(AttendeePage.this, Login.class);
                AttendeePage.this.startActivity(intent);
                AttendeePage.this.finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(AttendeePage.this, Login.class);
        AttendeePage.this.finish();
        AttendeePage.this.startActivity(intent);
    }
}