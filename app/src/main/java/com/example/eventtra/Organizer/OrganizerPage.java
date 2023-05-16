package com.example.eventtra.Organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventtra.AllUsers.Login;
import com.example.eventtra.AllUsers.settings;
import com.example.eventtra.ChatRooms.chatRoomsList;
import com.example.eventtra.AllUsers.GlobalData;
import com.example.eventtra.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrganizerPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    final private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private TextView username,email;
    boolean doubleBackToExitPressedOnce = false;
    CircleImageView profile;
    GlobalData globalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_page);
        globalData = (GlobalData) getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        globalData.setRegistrationToken();

        drawerLayout = findViewById(R.id.organizer_page_layout);
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_organizer, new subEvent_list()).addToBackStack("addEventDetails").commit();
            navigationView.setCheckedItem(R.id.nav_create_event);
        }
        if(!checkPermission()) {
            Log.d("Check Per", "onCreate: ");
            requestPermission();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_edit_subEvent:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_organizer, new subEvent_list()).addToBackStack("addEventDetails").commit();
                break;
            case R.id.organizer_enrollments:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_organizer, new organizer_event_enrollments()).addToBackStack("organizer_event_enrollments").commit();
                break;
            case R.id.organizer_chat:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_organizer, new chatRoomsList()).addToBackStack("chatRoomsList").commit();
                break;
            case R.id.organizer_notification:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_organizer, new organizerNotification()).addToBackStack("organizerNotification").commit();
                break;

            case R.id.nav_settings:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_organizer, new settings()).addToBackStack("settings").commit();
                break;

            case R.id.nav_logout:
                mAuth.signOut();
                Intent intent = new Intent(OrganizerPage.this, Login.class);
                OrganizerPage.this.startActivity(intent);
                OrganizerPage.this.finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
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
            } else {
                getSupportFragmentManager().popBackStack();
            }

        }
    }
    public Boolean checkPermission()
    {
        int notificationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS);
        int wakeLockPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WAKE_LOCK);
        if(notificationPermission == PackageManager.PERMISSION_GRANTED&&wakeLockPermission==PackageManager.PERMISSION_GRANTED)
            return true;
        Log.d("Check Per", "checkPermission:not ");
        return false;
    }
    public void requestPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.POST_NOTIFICATIONS)
                &&ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WAKE_LOCK))
        {
            Toast.makeText(this, "Please Allow Notification Permission From Settings", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.WAKE_LOCK},
                    101);
        }
    }
}