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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendeePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private DrawerLayout drawerLayout;
    final private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private TextView username,email;
    boolean doubleBackToExitPressedOnce = false;
    CircleImageView profile;
    GlobalData globalData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_page);

        globalData = (GlobalData) getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.attendee_page_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        username=navigationView.getHeaderView(0).findViewById(R.id.usernameContainer);
        email=navigationView.getHeaderView(0).findViewById(R.id.emailContainer);
        profile=navigationView.getHeaderView(0).findViewById(R.id.profileContainer);
        username.setText(globalData.getGlobalUser().getFname()+" "+globalData.getGlobalUser().getLname());
        email.setText(globalData.getGlobalUser().getEmail());
        Log.d("admin pic", "onCreate: "+globalData.getGlobalUser().getProfilePic());
        if(globalData.getGlobalUser().getProfilePic()!=null)
        {
            Picasso.get().load(globalData.getGlobalUser().getProfilePic()).into(profile);
        }
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new attendee_main_event_list()).addToBackStack("attendeeMainEventList").commit();
            navigationView.setCheckedItem(R.id.nav_events);
        }
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            Log.d("in else", "onBackPressed: "+getSupportFragmentManager().getBackStackEntryCount());;
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                if (doubleBackToExitPressedOnce) {
                    this.finish();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
            else {
                getSupportFragmentManager().popBackStack();
            }

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_events:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new attendee_main_event_list()).addToBackStack("attendeeMainEventList").commit();
                break;
            case R.id.my_enrollments:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new attendee_my_enrollments()).addToBackStack("attendee_my_enrollments").commit();
                break;
            case R.id.attendee_event_requests:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new attendeeEventRequest()).addToBackStack("attendeeEventRequest").commit();
                break;
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