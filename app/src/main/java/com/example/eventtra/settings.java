package com.example.eventtra;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import de.hdodenhof.circleimageview.CircleImageView;


public class settings extends Fragment {


    private CircleImageView profilePic,navProfile;
    private TextView usernameText,emailText,updateProfile,resetPassBtn;
    FloatingActionButton changePic;
    private EditText updateFname,updateLname,updateEmail,updatePhone;
    private Button savebtn;
    private AlertDialog loadingDialog;
    private AlertDialog alertDialog;
    private LinearLayout updateLayout;
    final private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private Boolean updateOpen=false;
    GlobalData globalData;
    NavigationView navigationView;
    private FirebaseFirestore database =FirebaseFirestore.getInstance();
    private CollectionReference userCollection = database.collection("User");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        changePic=view.findViewById(R.id.changePicBtn);
        profilePic=view.findViewById(R.id.profilePic);
        updateProfile=view.findViewById(R.id.updateProfileBtn);
        resetPassBtn=view.findViewById(R.id.resetPasswordBtn);
        usernameText=view.findViewById(R.id.usernameText);
        emailText=view.findViewById(R.id.emailText);
        updateFname=view.findViewById(R.id.fnameEditBox);
        updateLname=view.findViewById(R.id.lnameEditBox);
        updateEmail=view.findViewById(R.id.emailEditBox);
        updatePhone=view.findViewById(R.id.phoneEditBox);
        updateLayout=view.findViewById(R.id.updateLayout);
        savebtn=view.findViewById(R.id.saveBtn);
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        navigationView = getActivity().findViewById(R.id.nav_view);
        navProfile=navigationView.getHeaderView(0).findViewById(R.id.profileContainer);

        globalData = (GlobalData) getActivity().getApplicationContext();
        usernameText.setText(globalData.getGlobalUser().getFname()+" "+globalData.getGlobalUser().getLname());
        emailText.setText(globalData.getGlobalUser().getEmail());
        updateFname.setText(globalData.getGlobalUser().getFname());
        updateLname.setText(globalData.getGlobalUser().getLname());
        updateEmail.setText(globalData.getGlobalUser().getEmail());
        updatePhone.setText(globalData.getGlobalUser().getPhone());
        if(globalData.getGlobalUser().getProfilePic()!=null)
        {
            Picasso.get().load(globalData.getGlobalUser().getProfilePic()).into(profilePic);
        }

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final float scale = getContext().getResources().getDisplayMetrics().density;
                if(updateOpen)
                {
                    Log.d("update profile", "onClick: true");
                    ViewGroup.LayoutParams params = updateLayout.getLayoutParams();
                    Log.d("height", "onClick: "+params.height);
                    int pixels = (int) (43 * scale + 0.5f);
                    params.height=pixels;
                    updateLayout.setLayoutParams(params);
                    updateOpen=false;

                }
                else
                {
                    Log.d("update profile", "onClick: false");
                    ViewGroup.LayoutParams params = updateLayout.getLayoutParams();
                    Log.d("height", "onClick: "+params.height);
                    int pixels = (int) (350 * scale + 0.5f);
                    params.height=pixels;
                    updateLayout.setLayoutParams(params);
                    updateOpen=true;
                }
            }
        });
        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePic();
            }
        });
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });
        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                FirebaseAuth.getInstance().sendPasswordResetEmail(globalData.getGlobalUser().getEmail())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Password Reset Email Sent", Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });
            }
        });

        updateFname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkChange();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        updateLname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkChange();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        updatePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkChange();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void checkChange() {
        boolean fname=updateFname.getText().toString().equals(globalData.getGlobalUser().getFname());
        boolean lname=updateLname.getText().toString().equals(globalData.getGlobalUser().getLname());
        boolean phone=updatePhone.getText().toString().equals(globalData.getGlobalUser().getPhone());
        if(!fname||!lname||!phone)
        {
            savebtn.setEnabled(true);
        }
        else
        {
            savebtn.setEnabled(false);
        }
    }

    private void updateUserProfile() {
        String fname=updateFname.getText().toString();
        String lname=updateLname.getText().toString();
        String phone = updatePhone.getText().toString();
        boolean validData = true;

        /// Validating data
        if(fname.isEmpty())
        {
            updateFname.setError("First Name Required");
            updateFname.requestFocus();
            validData = false;
        }
        else if(lname.isEmpty())
        {
            updateLname.setError("Last Name Required");
            updateLname.requestFocus();
            validData = false;
        }
        else if(phone.length()!=11)
        {
            updatePhone.setError("Invalid Phone");
            updatePhone.requestFocus();
            validData = false;
        }
        else {
            MyUser updateUser = globalData.getGlobalUser();
            updateUser.setFname(fname);
            updateUser.setLname(lname);
            updateUser.setPhone(phone);
            Log.d("Updated user", "updateUserProfile: " + updateUser.toString());
            Map<String, Object> toUpdate = new HashMap<>();
            toUpdate.put("fname",updateUser.getFname());
            toUpdate.put("lname",updateUser.getLname());
            toUpdate.put("phone",updateUser.getPhone());
            userCollection.document(updateUser.getUserId()).update(toUpdate);
            usernameText.setText(globalData.getGlobalUser().getFname()+" "+globalData.getGlobalUser().getLname());
            emailText.setText(globalData.getGlobalUser().getEmail());
            TextView nav_name = navigationView.getHeaderView(0).findViewById(R.id.usernameContainer);
            TextView nav_email = navigationView.getHeaderView(0).findViewById(R.id.emailContainer);
            nav_name.setText(globalData.getGlobalUser().getFname()+" "+globalData.getGlobalUser().getLname());
            nav_email.setText(globalData.getGlobalUser().getEmail());
            savebtn.setEnabled(false);
        }
    }

    private void changePic() {
        //open gallery
        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGallery,1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                Uri pictureUri = data.getData();
                Log.d("picture URi", "onActivityResult: "+pictureUri.toString());
                //profilePic.setImageURI(pictureUri);
                showLoading();
                uploadToFirebase(pictureUri);

            }
        }
    }

    private void uploadToFirebase(Uri pictureUri) {
        Log.d("picture and id", "uploadToFirebase: "+globalData.getGlobalUser().getUserId()+"  "+pictureUri);
        StorageReference file = storageReference.child("Users/Profile/"+globalData.getGlobalUser().getUserId()+"/profile.jpg");
        file.putFile(pictureUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("upload image", "onSuccess: done");
                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if(uri!=null)
                        {
                            Log.d("Image uri", "onSuccess: "+uri);
                            globalData.getGlobalUser().setProfilePic(uri);
                            Picasso.get().load(globalData.getGlobalUser().getProfilePic()).into(profilePic);
                            Picasso.get().load(globalData.getGlobalUser().getProfilePic()).into(navProfile);
                            alertDialog.setTitle("Congrats!");
                            alertDialog.setMessage("Profile Picture Changed Successfully!");
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
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
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("upload image", "onFailure: fail");
                Picasso.get().load(globalData.getGlobalUser().getProfilePic()).into(profilePic);
                Picasso.get().load(globalData.getGlobalUser().getProfilePic()).into(navProfile);
                alertDialog.setTitle("Error!");
                alertDialog.setMessage("Unable to change Profile Picture!\nTry Agian");
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
    private void showLoading() {
        // adding ALERT Dialog builder object and passing activity as parameter
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // layoutinflater object and use activity to get layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(true);

        loadingDialog = builder.create();

        final float scale = getContext().getResources().getDisplayMetrics().density;
        int width = (int) (200 * scale + 0.5f);
        int height = (int) (200 * scale + 0.5f);
        loadingDialog.show();
        loadingDialog.getWindow().setLayout(width,height);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
    }
}