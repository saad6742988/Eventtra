package com.example.eventtra;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class attendeeEnrollmentAdapter  extends RecyclerView.Adapter<attendeeEnrollmentAdapter.viewHolder>{

    ArrayList<PaymentInfo> paymentInfoArrayList;
    Context context;
    GlobalData globalData;
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
            holder.enrollmentStatus.setTextColor(Color.GREEN);
        }
        else {
            holder.enrollmentStatus.setText("Pending");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
        final TextView statustv = view.findViewById(R.id.statustv);
        if(paymentInfo.getStatus()) {
            statustv.setText("Paid");
            statustv.setTextColor(Color.GREEN);
        }
        else
        {
            statustv.setText("Pending");
            statustv.setTextColor(Color.RED);
        }

        Button recievePaymentBtn=view.findViewById(R.id.recievePaymentBtn);
        recievePaymentBtn.setVisibility(View.GONE);
        ImageButton closeBtn = view.findViewById(R.id.closeBtn);
        AlertDialog alertDialog = builder.create();

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
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
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            enrollmentSubName= itemView.findViewById(R.id.enrollmentSubName);
            enrollmentName= itemView.findViewById(R.id.enrollmentName);
            enrollmentCnic= itemView.findViewById(R.id.enrollmentCnic);
            enrollmentStatus= itemView.findViewById(R.id.enrollmentStatus);
            mainCardView=itemView.findViewById(R.id.enrollmentCard);


        }
    }
}
