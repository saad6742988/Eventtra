package com.example.eventtra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;
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
    private AlertDialog loadingDialog;
    private AlertDialog alertDialog;
    private TextInputLayout emailLayout,passwordLayout;
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
        emailLayout = findViewById(R.id.emailBoxInputlayout);
        passwordLayout = findViewById(R.id.passwordBoxInputlayout);
        mAuth = FirebaseAuth.getInstance();
        alertDialog = new AlertDialog.Builder(Login.this).create();
        globalData = (GlobalData) getApplicationContext();

        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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

        final float scale = getResources().getDisplayMetrics().density;
        int width = (int) (200 * scale + 0.5f);
        int height = (int) (200 * scale + 0.5f);
        loadingDialog.show();
        loadingDialog.getWindow().setLayout(width,height);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
    }

    public void login(View view) {

        String email = emailText.getText().toString();
        String pass =passwordText.getText().toString();

        if(email.isEmpty())
        {
            emailLayout.setError("Email is Required");
            passwordLayout.requestFocus();
        }
        else if(pass.isEmpty())
        {
            passwordLayout.setError("Password Can't Be Empty");
            passwordLayout.requestFocus();
        }
        else {
            showLoading();
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if(mAuth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(Login.this, "User logged in successfully", Toast.LENGTH_SHORT).show();

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

                            emailLayout.setError("Invalid Email Format!");
                            emailLayout.requestFocus();
                            loadingDialog.dismiss();
                        } else if (task.getException().getMessage().equals(getString(R.string.no_user))) {
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
                            alertDialog.setTitle("Error!");
                            alertDialog.setMessage("Check Your Connection and Try Again!");
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
        else if (globalData.getGlobalUser().getRole().equals("organizer")) {
            Intent i = new Intent(Login.this, OrganizerPage.class);
            Login.this.startActivity(i);
        }
        loadingDialog.dismiss();
        Login.this.finish();
    }

}