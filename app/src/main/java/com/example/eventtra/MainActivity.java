package com.example.eventtra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    final private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    final private CollectionReference userCollection = database.collection("User");
    GlobalData globalData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        globalData = (GlobalData) getApplicationContext();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (mAuth.getCurrentUser() != null&&mAuth.getCurrentUser().isEmailVerified()) {
                    userCollection.document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                MyUser user = doc.toObject(MyUser.class);
                                Log.d("User ID", "onSuccess: " + doc.getId());
                                user.setUserId(doc.getId());

                                //get picture
                                StorageReference file = storageReference.child("Users/Profile/"+doc.getId()+"/profile.jpg");
                                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.d("main get image", "onSuccess: fetch success");
                                        if(uri!=null)
                                        {
                                            Log.d("Image uri", "onSuccess: "+uri);
                                            user.setProfilePic(uri);
                                            moveToOtherActivity(user);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Error", "onFailure: "+e.getMessage());
                                        user.setProfilePic(null);
                                        moveToOtherActivity(user);
                                    }
                                });


                            } else {
                                Toast.makeText(MainActivity.this, "Failed to get user data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(MainActivity.this, Login.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }
            }

        }, 3000);

//        shift to registration page
//        Intent intent = new Intent(this, Registration.class);
//        startActivity(intent);
//        Intent intent = new Intent(this, AdminPage.class);
//        startActivity(intent);

//        Intent intent = new Intent(this,OtpVerification.class);
//        startActivity(intent);
//        Intent intent = new Intent(this,Login.class);
//        startActivity(intent);
    }

    private void moveToOtherActivity(MyUser user) {
        globalData.setglobalUser(user);
        Log.d("check global USer", "onComplete: " + globalData.getGlobalUser().toString());
        if (globalData.getGlobalUser().getRole().equals("attendee")) {
            Intent i = new Intent(MainActivity.this, AttendeePage.class);
            MainActivity.this.startActivity(i);
            MainActivity.this.finish();
        } else if (globalData.getGlobalUser().getRole().equals("admin")) {
            Intent i = new Intent(MainActivity.this, AdminPage.class);
            MainActivity.this.startActivity(i);
            MainActivity.this.finish();
        }
    }

    private Uri getProfilePic(String id) {
        Uri pic=null;
        StorageReference file = storageReference.child("Users/Profile/"+id+"/profile.jpg");
        file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(uri!=null)
                {

                }
            }
        });
        return pic;
    }
}