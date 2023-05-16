package com.example.eventtra.AllUsers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventtra.Models.MyUser;
import com.example.eventtra.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Matcher;

public class Registration extends AppCompatActivity {

    private EditText fnameText,lnameText,emailText,phoneText,passwordText,cpasswordText;
    private TextInputLayout fnameTextLayout,lnameTextLayout,emailTextLayout,phoneTextLayout,passwordTextLayout,cpasswordTextLayout;
    private Button registerBtn;
    private AlertDialog loadingDialog;
    private AlertDialog alertDialog;
    FirebaseAuth mAuth;
    private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private FirebaseFirestore database =FirebaseFirestore.getInstance();
    private CollectionReference userCollection = database.collection("User");
    //To be Implemented11
//     public enum fragmentType{OTP,Register}
//     fragmentType opened;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        alertDialog = new AlertDialog.Builder(Registration.this).create();
        mAuth=FirebaseAuth.getInstance();
        //getting input feilds
        fnameText=findViewById(R.id.fnameBox);
        lnameText=findViewById(R.id.lnameBox);
        emailText=findViewById(R.id.emailBox);
        phoneText=findViewById(R.id.phoneBox);
        passwordText=findViewById(R.id.passwordBox);
        cpasswordText=findViewById(R.id.cpasswordBox);
        //getting input feilds layouts
        fnameTextLayout=findViewById(R.id.fnameBoxLayout);
        lnameTextLayout=findViewById(R.id.lnameBoxLayout);
        emailTextLayout=findViewById(R.id.emailBoxLayout);
        phoneTextLayout=findViewById(R.id.phoneBoxLayout);
        passwordTextLayout=findViewById(R.id.passwordBoxLayout);
        cpasswordTextLayout=findViewById(R.id.cpasswordBoxLayout);


        fnameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fnameTextLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        lnameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lnameTextLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailTextLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneTextLayout.setErrorEnabled(false);
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
                passwordTextLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cpasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cpasswordTextLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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
        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this,R.style.CustomAlertDialog);

        // layoutinflater object and use activity to get layout inflater
        LayoutInflater inflater = Registration.this.getLayoutInflater();
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
            fnameTextLayout.setError("First Name Required");
            fnameTextLayout.requestFocus();
            validData = false;
        }
        if(lname.isEmpty())
        {
            lnameTextLayout.setError("Last Name Required");
            lnameTextLayout.requestFocus();
            validData = false;
        }
        if(phone.length()!=11)
        {
            phoneTextLayout.setError("Invalid Phone");
            phoneTextLayout.requestFocus();
            validData = false;
        }
        if(emailValidate==false)
        {
            //otp check
            emailTextLayout.setError("Invalid Email");
            emailTextLayout.requestFocus();
            validData = false;
        }
        if(password.length()<8)
        {
            passwordTextLayout.setError("Password Must Be At Least 8 Characters Long");
            passwordTextLayout.requestFocus();
            validData = false;
        }
        if(!password.equals(cpassword))
        {
            Log.d(password,cpassword);
            cpasswordTextLayout.setError("Password Doesn't Match");
            cpasswordTextLayout.requestFocus();
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
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.setCancelable(false);
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
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
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
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
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
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
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