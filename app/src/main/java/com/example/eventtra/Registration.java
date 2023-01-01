package com.example.eventtra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;

public class Registration extends AppCompatActivity {

    private EditText fnameText,lnameText,emailText,phoneText,passwordText,cpasswordText;
    private Button registerBtn;
    private AlertDialog loadingDialog;
    private AlertDialog alertDialog;
    FirebaseAuth mAuth;
    private FirebaseFirestore database =FirebaseFirestore.getInstance();
    private CollectionReference userCollection = database.collection("User");
    //To be Implemented11
//     public enum fragmentType{OTP,Register}
//     fragmentType opened;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        if(savedInstanceState == null){
//            getSupportFragmentManager().beginTransaction().replace(R.id.register_fragment_container, new registration_userData()).addToBackStack("register").commit();
////            setOpened(fragmentType.Register);
//        }
        alertDialog = new AlertDialog.Builder(Registration.this).create();
        mAuth=FirebaseAuth.getInstance();
        fnameText=findViewById(R.id.fnameBox);
        lnameText=findViewById(R.id.lnameBox);
        emailText=findViewById(R.id.emailBox);
        phoneText=findViewById(R.id.phoneBox);
        passwordText=findViewById(R.id.passwordBox);
        cpasswordText=findViewById(R.id.cpasswordBox);
    }


//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
////                if(opened == fragmentType.Register)
////                    Toast.makeText(this, "Going to Log in"+opened, Toast.LENGTH_SHORT).show();
////                else
////                    Toast.makeText(this, "Going to Register"+opened, Toast.LENGTH_SHORT).show();
////                break;
//                if(getSupportFragmentManager().getBackStackEntryCount()==1)
//                {
//                    Intent intent = new Intent(Registration.this,Login.class);
//                    startActivity(intent);
//                }
//                else{
//                    getSupportFragmentManager().popBackStackImmediate("register",0);
//                }
//                break;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        return true;
//    }

    private void showLoading() {
        // adding ALERT Dialog builder object and passing activity as parameter
        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);

        // layoutinflater object and use activity to get layout inflater
        LayoutInflater inflater = Registration.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(true);

        loadingDialog = builder.create();
        loadingDialog.show();
    }

    public void registeration(View v)
    {
        String email=emailText.getText().toString();
        String password =passwordText.getText().toString();
        String cpassword=cpasswordText.getText().toString();
        String fname=fnameText.getText().toString();
        String lname=lnameText.getText().toString();
        String phone = phoneText.getText().toString();
        boolean validData = true;

        //Pattern emailPattern=Patterns.EMAIL_ADDRESS;
        Matcher emailMatcher = Patterns.EMAIL_ADDRESS.matcher(email);
        Boolean emailValidate = emailMatcher.matches();


        /// Validating data
        if(fname.isEmpty())
        {
            fnameText.setError("First Name Required");
            fnameText.requestFocus();
            validData = false;
        }
        if(lname.isEmpty())
        {
            lnameText.setError("Last Name Required");
            lnameText.requestFocus();
            validData = false;
        }
        if(phone.length()!=11)
        {
            phoneText.setError("Invalid Phone");
            phoneText.requestFocus();
            validData = false;
        }
        if(emailValidate==false)
        {
            //otp check
            emailText.setError("Invalid Email");
            emailText.requestFocus();
            validData = false;
        }
        if(password.length()<8)
        {
            passwordText.setError("Password Must Be At Least 8 Characters Long");
            passwordText.requestFocus();
            validData = false;
        }
        if(!password.equals(cpassword))
        {
            Log.d(password,cpassword);
            cpasswordText.setError("Password Doesn't Match");
            cpasswordText.requestFocus();
            validData = false;
        }

        if(validData)
        {
            showLoading();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(Registration.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                        Log.d("Registration","Registration Successful");
                        FirebaseUser mAuthCurrentUser = mAuth.getCurrentUser();
                        mAuthCurrentUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //create user object
                                Toast.makeText(Registration.this, "Verification Email Sent", Toast.LENGTH_LONG).show();
                                MyUser user = new MyUser(fname,lname,email,phone);
                                Log.d("user",user.toString());
                                //add user to database
                                addUser(user,mAuthCurrentUser.getUid());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Registration.this, "Verification Email not Sent", Toast.LENGTH_LONG).show();
                                alertDialog.setTitle("Error!");
                                if(task.getException().getMessage()!=null)
                                    alertDialog.setMessage("Error: "+task.getException().getMessage()+"\nTry Again.");
                                else
                                    alertDialog.setMessage("Check Your Connection.\nTry Again Later!");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                                loadingDialog.dismiss();
                            }
                        });

                    }
                    else
                    {
                        alertDialog.setTitle("Error!");
                        if(task.getException().getMessage()!=null)
                            alertDialog.setMessage("Error: "+task.getException().getMessage()+"\nTry Again.");
                        else
                            alertDialog.setMessage("Check Your Connection.\nTry Again Later!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        loadingDialog.dismiss();
                        //Toast.makeText(Registration.this,"Registration Error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d("Registration","Registration UnSuccessful");
                    }
                }
            });
        }
//        mAuth.createUserWithEmailAndPassword("saad@gmail.com","ok123456ok").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(MainActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
//                    Log.d("Res","Done");
//                }else{
//                    Toast.makeText(MainActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.d("Res","Not Done");
//                }
//            }
//        });

    }


    public void addUser(MyUser user,String uid)
    {
        //Toast to be removed
        userCollection.document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                alertDialog.setTitle("Congrats!");
                alertDialog.setMessage("You Are Successfully Registered.\nPLease checkYour Email For Verification.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Registration.this,Login.class);
                                Registration.this.finish();
                                startActivity(i);
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                loadingDialog.dismiss();
                //Toast.makeText(Registration.this, "User Added", Toast.LENGTH_SHORT).show();
                Log.d("Add DB","Added");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alertDialog.setTitle("Error!");
                alertDialog.setMessage("Check Your Connection.\nTry Again Later!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                loadingDialog.dismiss();
                //Toast.makeText(Registration.this, "User Not Added", Toast.LENGTH_SHORT).show();
                Log.d("Add not DB",e.getMessage());
            }
        });
//        userCollection.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                alertDialog.setTitle("Congrats!");
//                alertDialog.setMessage("You Are Successfully Registered.");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent i = new Intent(Registration.this,Login.class);
//                                Registration.this.finish();
//                                startActivity(i);
//                                dialog.dismiss();
//                            }
//                        });
//                alertDialog.show();
//                loadingDialog.dismiss();
//                //Toast.makeText(Registration.this, "User Added", Toast.LENGTH_SHORT).show();
//                Log.d("Add DB","Added");
//                //documentReference.getId();
//                user.setUserId(documentReference.getId());
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                alertDialog.setTitle("Error!");
//                alertDialog.setMessage("Check Your Connection.\nTry Again Later!");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                alertDialog.show();
//                loadingDialog.dismiss();
//                //Toast.makeText(Registration.this, "User Not Added", Toast.LENGTH_SHORT).show();
//                Log.d("Add not DB",e.getMessage());
//            }
//        });
    }

    public void gotoLogin(View v)
    {
        Intent intent = new Intent(Registration.this,Login.class);
        startActivity(intent);
    }
}