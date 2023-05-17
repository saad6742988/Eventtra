package com.example.eventtra.Attendee;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.eventtra.AllUsers.GlobalData;
import com.example.eventtra.Models.PaymentInfo;
import com.example.eventtra.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

public class attendeeEnrollmentAdapter  extends RecyclerView.Adapter<attendeeEnrollmentAdapter.viewHolder>{

    ArrayList<PaymentInfo> paymentInfoArrayList;
    Context context;
    GlobalData globalData;
    final private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private Uri certificateUri;
    public attendeeEnrollmentAdapter(ArrayList<PaymentInfo> list, Context context) {
        this.paymentInfoArrayList = list;
        this.context = context;
        this.globalData = (GlobalData) context.getApplicationContext();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.enrollment_list_layout, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        PaymentInfo paymentInfo = paymentInfoArrayList.get(position);
        Log.d("Payment data", "onBindViewHolder: "+paymentInfo);

        holder.enrollmentSubName.setText(paymentInfo.getSubEventName());
        holder.enrollmentName.setText(paymentInfo.getParticipantName());
        holder.enrollmentCnic.setText(paymentInfo.getParticipantCnic());
        if(paymentInfo.getStatus()) {
            holder.enrollmentStatus.setText("Paid");
            holder.animationView.setAnimation(R.raw.paidanimation);
            holder.enrollmentStatus.setTextColor(context.getColor(R.color.DarkGreen));
        }
        else {
            holder.enrollmentStatus.setText("Pending");
            holder.animationView.setAnimation(R.raw.pendinganimation);
            holder.enrollmentStatus.setTextColor(Color.RED);
        }

        holder.mainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReceipt(paymentInfo);
            }
        });
    }
    private void showReceipt(PaymentInfo paymentInfo)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.CustomAlertDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.participant_enrollment_receipt, null);
        builder.setView(view);
        final TextView subeventNametv = view.findViewById(R.id.subeventNametv);
        subeventNametv.setText(paymentInfo.getSubEventName());
        final TextView tidtv = view.findViewById(R.id.tidtv);
        tidtv.setText(paymentInfo.getTid());
        final TextView timetv = view.findViewById(R.id.timetv);
        Date d = new Date(paymentInfo.getTimeStamp());
        timetv.setText(d.toString());
        final TextView parNametv = view.findViewById(R.id.parNametv);
        parNametv.setText(paymentInfo.getParticipantName());
        final TextView parCnictv = view.findViewById(R.id.parCnictv);
        parCnictv.setText(paymentInfo.getParticipantCnic());
        final TextView madeBytv = view.findViewById(R.id.madeBytv);
        madeBytv.setText(globalData.globalUser.getFname()+" "+globalData.globalUser.getLname());
        final LottieAnimationView lottieAnimationView = view.findViewById(R.id.animationView);

        Button recievePaymentBtn=view.findViewById(R.id.recievePaymentBtn);
        Button downloadCertificateBtn=view.findViewById(R.id.downloadCertificateBtn);
        downloadCertificateBtn.setEnabled(false);
        recievePaymentBtn.setVisibility(View.GONE);
        final TextView statustv = view.findViewById(R.id.statustv);
        if(paymentInfo.getStatus()) {
            statustv.setText("Paid");
            lottieAnimationView.setAnimation(R.raw.paidanimation);
            statustv.setTextColor(context.getColor(R.color.DarkGreen));
        }
        else
        {
            statustv.setText("Pending");
            lottieAnimationView.setAnimation(R.raw.pendinganimation);
            statustv.setTextColor(Color.RED);
        }
        AlertDialog alertDialog = builder.create();
        //get event certificate

        StorageReference certificatefile = storageReference.child("SubEvent/"+paymentInfo.getSubEventID()+"/certificate.pdf");
        certificatefile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(uri!=null&&paymentInfo.getStatus())
                {
                    certificateUri=uri;
                   downloadCertificateBtn.setEnabled(true);
                   downloadCertificateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downloadCertificate(uri, paymentInfo.getParticipantName(), paymentInfo.getSubEventName(),v);
                            alertDialog.dismiss();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "No certificate Found", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton closeBtn = view.findViewById(R.id.closeBtn);


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
//        downloadCertificateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                downloadCertificate(uri,paymentInfo.getSubEventID(),paymentInfo.getParticipantName(),paymentInfo.getSubEventName());
//            }
//        });


        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void downloadCertificate(Uri uri, String participantName, String subEventName, View v) {
        DownloadManager.Request request = new DownloadManager.Request(uri);

//        Log.d("downloadCertificate", "downloadCertificate: "+uri);

        // Set the download directory path and file name
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, participantName+"_"+subEventName+".pdf");

        // Optionally, set other request parameters such as title, description, etc.
         request.setTitle("Download");
         request.setDescription("Downloading file...");

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        // Enqueue the download and get the download ID
        long downloadId = downloadManager.enqueue(request);
        Toast.makeText(context, "Certificate Has Been Downloaded", Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() {
        return paymentInfoArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView enrollmentSubName;
        TextView enrollmentName;
        TextView enrollmentCnic;
        TextView enrollmentStatus;
        CardView mainCardView;
        LottieAnimationView animationView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            enrollmentSubName= itemView.findViewById(R.id.enrollmentSubName);
            enrollmentName= itemView.findViewById(R.id.enrollmentName);
            enrollmentCnic= itemView.findViewById(R.id.enrollmentCnic);
            enrollmentStatus= itemView.findViewById(R.id.enrollmentStatus);
            mainCardView=itemView.findViewById(R.id.enrollmentCard);
            animationView=itemView.findViewById(R.id.animationView);


        }
    }
}
