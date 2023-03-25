package com.example.eventtra;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class GlobalData extends Application {
     public MyUser globalUser;
     MyEvent globalEvent;
     subEventsModel globalSubEvent;

    public GlobalData() {
        this.globalUser = new MyUser();
    }

    public MyUser getGlobalUser() {
        return this.globalUser;
    }
    public void setglobalUser(MyUser u) {
        this.globalUser = u;
    }
    public void setRegistrationToken() {
        final FirebaseFirestore database =FirebaseFirestore.getInstance();
        final CollectionReference userCollection = database.collection("User");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);
        Log.d("check", "setRegistrationToken: ");

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful())
                {

                    if(!task.getResult().equals(globalUser.getDeviceToken()))
                    {
                        Log.d("setRegistrationToken", "first run: "+task.getResult());
                        globalUser.setDeviceToken(task.getResult());
                        HashMap<String,Object> updateToken = new HashMap<>();
                        updateToken.put("deviceToken",globalUser.getDeviceToken());
                        userCollection.document(globalUser.getUserId()).update(updateToken);
                        FirebaseMessaging.getInstance().subscribeToTopic("All").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())

                                    Log.d("check Subscription", "onComplete: ");
                            }
                        });
                    }
                }

            }
        });

        if (isFirstRun) {
            // Run your code here that you want to execute only on first install
            Toast.makeText(this, "first run", Toast.LENGTH_SHORT).show();
            Log.d("first run", "first run: ");

            // Set the isFirstRun flag to false so that the code doesn't run again
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();
        }
        else
        {
            Log.d("first run", "not first run: ");

        }


    }

}
