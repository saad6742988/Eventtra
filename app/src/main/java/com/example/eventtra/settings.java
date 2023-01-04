package com.example.eventtra;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class settings extends Fragment {


    private CircleImageView profilePic,navProfile;
    private TextView usernameText,emailText,changePic,updateProfile;
    private AlertDialog loadingDialog;
    private AlertDialog alertDialog;
    final private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private Boolean updateOpen=false;
    GlobalData globalData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        changePic=view.findViewById(R.id.changePicBtn);
        profilePic=view.findViewById(R.id.profilePic);
        updateProfile=view.findViewById(R.id.updateProfileBtn);
        usernameText=view.findViewById(R.id.usernameText);
        emailText=view.findViewById(R.id.emailText);
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navProfile=navigationView.getHeaderView(0).findViewById(R.id.profileContainer);

        globalData = (GlobalData) getActivity().getApplicationContext();
        usernameText.setText(globalData.getGlobalUser().getFname()+" "+globalData.getGlobalUser().getLname());
        emailText.setText(globalData.getGlobalUser().getEmail());
        if(globalData.getGlobalUser().getProfilePic()!=null)
        {
            Picasso.get().load(globalData.getGlobalUser().getProfilePic()).into(profilePic);
        }



        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePic();
            }
        });
        return view;
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
        loadingDialog.show();
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
    }
}