package com.example.eventtra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;

public class Registration extends AppCompatActivity {

    private EditText fnameText,lnameText,emailText,phoneText,passwordText,cpasswordText;
    private Button registerBtn;
    FirebaseAuth mAuth;
    private FirebaseFirestore database =FirebaseFirestore.getInstance();
    private CollectionReference userCollection = database.collection("User");
    //To be Implemented11
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth=FirebaseAuth.getInstance();

        fnameText=findViewById(R.id.fnameBox);
        lnameText=findViewById(R.id.lnameBox);
        emailText=findViewById(R.id.emailBox);
        phoneText=findViewById(R.id.phoneBox);
        passwordText=findViewById(R.id.passwordBox);
        cpasswordText=findViewById(R.id.cpasswordBox);
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
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(Registration.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                        Log.d("Registration","Registration Successful");

                        //create user object
                        MyUser user = new MyUser(fname,lname,email,phone);
                        Log.d("user",user.toString());

                        //add user to database
                        addUser(user);

                        Log.d("user",user.toString());
                    }
                    else
                    {
                        Toast.makeText(Registration.this,"Registration Error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
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


    public void addUser(MyUser user)
    {
        //Toast to be removed
        userCollection.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(Registration.this, "User Added", Toast.LENGTH_SHORT).show();
                Log.d("Add DB","Added");
                //documentReference.getId();
                user.setUserId(documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registration.this, "User Not Added", Toast.LENGTH_SHORT).show();
                Log.d("Add not DB",e.getMessage());
            }
        });
    }
}