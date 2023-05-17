package com.example.eventtra.Attendee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.eventtra.AllUsers.Login;
import com.example.eventtra.AllUsers.settings;
import com.example.eventtra.ChatRooms.chatRoomsList;
import com.example.eventtra.AllUsers.GlobalData;
import com.example.eventtra.R;
import com.google.android.material.navigation.NavigationView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

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
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_page);

        globalData = (GlobalData) getApplicationContext();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        globalData.setRegistrationToken();

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
            toolbar.setTitle("Events");
            navigationView.setCheckedItem(R.id.nav_events);
        }
        if(!checkPermission()) {
            Log.d("Check Per", "onCreate: ");
            requestPermission();
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
                toolbar.setTitle("Events");
                break;
            case R.id.my_enrollments:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new attendee_my_enrollments()).addToBackStack("attendee_my_enrollments").commit();
                toolbar.setTitle("My Enrollments");
                break;
            case R.id.attendee_chat:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new chatRoomsList()).addToBackStack("chatRoomsList").commit();
                toolbar.setTitle("Discussions");
                break;
            case R.id.attendee_event_requests:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new attendeeEventRequest()).addToBackStack("attendeeEventRequest").commit();
                toolbar.setTitle("Event Requests");
                break;
            case R.id.live_stream:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new liveStreamList()).addToBackStack("liveStreamList").commit();
                toolbar.setTitle("Live Streams");
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_attendee, new settings()).commit();
                toolbar.setTitle("Settings");
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
    public Boolean checkPermission()
    {
        int notificationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS);
        int wakeLockPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WAKE_LOCK);
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(notificationPermission == PackageManager.PERMISSION_GRANTED&&wakeLockPermission==PackageManager.PERMISSION_GRANTED&&writePermission==PackageManager.PERMISSION_GRANTED)
            return true;
        Log.d("Check Per", "checkPermission:not ");
        return false;
    }
    public void requestPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.POST_NOTIFICATIONS)
                &&ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WAKE_LOCK)
                &&ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            Toast.makeText(this, "Please Allow Notification Permission From Settings", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK,Manifest.permission.POST_NOTIFICATIONS},
                    101);
        }
    }
}