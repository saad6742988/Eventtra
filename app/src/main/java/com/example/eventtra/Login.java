package com.example.eventtra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {

    // to be implemented
    private EditText emailText,passwordText;
    private TextView errorView;
    FirebaseAuth mAuth;
    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference userCollection = database.collection("User");
    GlobalData globalData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText=findViewById(R.id.emailBox);
        passwordText=findViewById(R.id.passwordBox);
        mAuth = FirebaseAuth.getInstance();
        errorView=findViewById(R.id.errorView);
        globalData = (GlobalData) getApplicationContext();
        errorView.setAlpha(0f);
    }

    public void gotoRegistration(View v)
    {
        Intent intent = new Intent(Login.this,Registration.class);
        startActivity(intent);
    }

    public void login(View view) {
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
                        Toast.makeText(Login.this, "User logged in successfully", Toast.LENGTH_SHORT).show();


                        userCollection.whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    MyUser user = documentSnapshot.toObject(MyUser.class);
                                    Log.d("User ID", "onSuccess: " + documentSnapshot.getId());
                                    user.setUserId(documentSnapshot.getId());
                                    globalData.setglobalUser(user);
                                    Log.d("check global USer", "onComplete: " + globalData.getGlobalUser().toString());
                                    if (globalData.getGlobalUser().getRole().equals("attendee")) {
                                        Intent i = new Intent(Login.this, AttendeePage.class);
                                        Login.this.startActivity(i);
                                        Login.this.finish();
                                    }
                                }
                            }
                        });

//                        MyUser userTemp=new MyUser(globalData.getUser());
                        //Log.d("check global", "onComplete: " + globalData.getGlobalUser().toString());
//                        if(globalData.getUser().getRole().equals("attendee"))
//                        {
//                            Intent i = new Intent(Login.this,AttendeePage.class);
//                            Login.this.startActivity(i);
//                            Login.this.finish();
//                        }
                        Log.d("login", "done");

                    } else {
//                    Log.d(task.getException().getMessage(), R.string.invalid_email);
                        if (task.getException().getMessage().equals(getString(R.string.invalid_email))) {

                            emailText.setError("Invalid Email Format!");
                            emailText.requestFocus();
                        } else if (task.getException().getMessage().equals(getString(R.string.no_user))) {
                            errorView.setText("User Not Found!!!\nIf Your New Make Sure To Register First.");
                            errorView.setAlpha(1f);
                            errorView.animate().alpha(0f).setDuration(3000);
//                        emailText.setError("User Not Found!");
                            emailText.requestFocus();
                        } else if (task.getException().getMessage().equals(getString(R.string.invalid_password))) {
                            errorView.setText("Incorrect Email or Password!!!");
                            errorView.setAlpha(1f);
                            errorView.animate().alpha(0f).setDuration(3000);
//                        passwordText.setError("Incorrect Password");
//                        passwordText.requestFocus();
                        } else {
                            Toast.makeText(Login.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("login", "not done " + task.getException().getMessage());
                        }
                    }
                }

            });
        }
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