package com.example.eventtra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Login extends AppCompatActivity {

    // to be implemented
    private EditText emailText,passwordText;
    private TextView errorView;
    private AlertDialog loadingDialog;
    private AlertDialog alertDialog;
    FirebaseAuth mAuth;
    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference userCollection = database.collection("User");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    GlobalData globalData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText=findViewById(R.id.emailBox);
        passwordText=findViewById(R.id.passwordBox);
        mAuth = FirebaseAuth.getInstance();
        errorView=findViewById(R.id.errorView);
        alertDialog = new AlertDialog.Builder(Login.this).create();
        globalData = (GlobalData) getApplicationContext();
        errorView.setAlpha(0f);
    }

    public void gotoRegistration(View v)
    {
        Intent intent = new Intent(Login.this,Registration.class);
        startActivity(intent);
    }
    private void showLoading() {
        // adding ALERT Dialog builder object and passing activity as parameter
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

        // layoutinflater object and use activity to get layout inflater
        LayoutInflater inflater = Login.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(true);

        loadingDialog = builder.create();
        loadingDialog.show();
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
    }

    public void login(View view) {
        showLoading();
        String email = emailText.getText().toString();
        String pass =passwordText.getText().toString();

        if(email.isEmpty())
        {
            emailText.setError("Email Can't Be Empty");
            emailText.requestFocus();
        }
        else if(pass.isEmpty())
        {
            passwordText.setError("Password Can't Be Empty");
            passwordText.requestFocus();
        }
        else {

            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if(mAuth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(Login.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
//                            userCollection.whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                @Override
//                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                                        MyUser user = documentSnapshot.toObject(MyUser.class);
//                                        Log.d("User ID", "onSuccess: " + documentSnapshot.getId());
//                                        user.setUserId(documentSnapshot.getId());
//                                        globalData.setglobalUser(user);
//                                        Log.d("check global USer", "onComplete: " + globalData.getGlobalUser().toString());
//                                        if (globalData.getGlobalUser().getRole().equals("attendee")) {
//                                            Intent i = new Intent(Login.this, AttendeePage.class);
//                                            Login.this.startActivity(i);
//                                            Login.this.finish();
//                                        }
//                                    }
//                                }
//                            });
                            userCollection.document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot doc =task.getResult();
                                    if(doc.exists()) {
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
                                    }
                                    else
                                    {
                                        Toast.makeText(Login.this, "Failed to get user data", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else
                        {
//                            errorView.setText("Email Not Verified!\nPlease Check your Email and Verify it First.");
//                            errorView.setAlpha(1f);
//                            errorView.animate().alpha(0f).setDuration(3000);
                            alertDialog.setTitle("Error!");
                            alertDialog.setMessage("Email Not Verified!\nPlease Check your Email and Verify it First.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            loadingDialog.dismiss();
                        }
                        Log.d("login", "done");

                    } else {
//                    Log.d(task.getException().getMessage(), R.string.invalid_email);
                        if (task.getException().getMessage().equals(getString(R.string.invalid_email))) {

                            emailText.setError("Invalid Email Format!");
                            emailText.requestFocus();
                        } else if (task.getException().getMessage().equals(getString(R.string.no_user))) {
//                            errorView.setText("User Not Found!!!\nIf Your New Make Sure To Register First.");
//                            errorView.setAlpha(1f);
//                            errorView.animate().alpha(0f).setDuration(3000);
                            alertDialog.setTitle("Error!");
                            alertDialog.setMessage("User Not Found!!!\nIf Your New Make Sure To Register First.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            loadingDialog.dismiss();
//                        emailText.setError("User Not Found!");
                            emailText.requestFocus();
                        } else if (task.getException().getMessage().equals(getString(R.string.invalid_password))) {
//                            errorView.setText("Incorrect Email or Password!!!");
//                            errorView.setAlpha(1f);
//                            errorView.animate().alpha(0f).setDuration(3000);
//                        passwordText.setError("Incorrect Password");
//                        passwordText.requestFocus();
                            alertDialog.setTitle("Error!");
                            alertDialog.setMessage("Incorrect Email or Password!!!");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            loadingDialog.dismiss();
                        } else {
                            Toast.makeText(Login.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("login", "not done " + task.getException().getMessage());
                        }
                    }
                }

            });
        }
    }
    private void moveToOtherActivity(MyUser user) {
        globalData.setglobalUser(user);
        Log.d("check global USer", "onComplete: " + globalData.getGlobalUser().toString());
        if (globalData.getGlobalUser().getRole().equals("attendee")) {
            Intent i = new Intent(Login.this, AttendeePage.class);
            Login.this.startActivity(i);
        } else if (globalData.getGlobalUser().getRole().equals("admin")) {
            Intent i = new Intent(Login.this, AdminPage.class);
            Login.this.startActivity(i);

        }
        loadingDialog.dismiss();
        Login.this.finish();
    }
    private void getUser(String email) {
        userCollection.whereEqualTo("email",email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    MyUser user = documentSnapshot.toObject(MyUser.class);
                    Log.d("User ID", "onSuccess: "+documentSnapshot.getId());
                    user.setUserId(documentSnapshot.getId());
//                    globalData.setglobalUser(user.getFname(),user.getLname(),user.getEmail(),user.getPhone(),user.getRole(),user.getUserId());
                    Log.d("writing", user.toString());
                    globalData.setglobalUser(user);
                    Log.d("User Data out", user.toString());
                }
            }
        });
    }
}