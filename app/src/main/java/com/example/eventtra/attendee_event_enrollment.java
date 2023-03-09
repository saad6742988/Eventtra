package com.example.eventtra;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class attendee_event_enrollment extends Fragment {


    Button addNewParticipant;
    EditText attendeeMainRegisterName;
    TextView registeringEventName;
    RecyclerView otherParRecyclerView;
    ArrayList<OtherParticipant> otherParticipantsList = new ArrayList<>();
    GlobalData globalData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_event_enrollment, container, false);
        globalData=(GlobalData)getContext().getApplicationContext();
        addNewParticipant = view.findViewById(R.id.addNewParticipantBtn);
        otherParRecyclerView = view.findViewById(R.id.otherParRecyclerView);
        attendeeMainRegisterName = view.findViewById(R.id.attendeeMainRegisterName);
        attendeeMainRegisterName.setText(globalData.globalUser.getFname()+" "+globalData.globalUser.getLname());
        registeringEventName = view.findViewById(R.id.registeringEventName);
        registeringEventName.setText(globalData.globalSubEvent.getName());
        addNewParticipant = view.findViewById(R.id.addNewParticipantBtn);


        //temp code
        attendeeOtherParticipantsAdapter adapter= new attendeeOtherParticipantsAdapter();
        otherParRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        otherParRecyclerView.setLayoutManager(layoutManager);


        addNewParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOtherParticipant();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void addOtherParticipant() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.attendee_add_other_dialog, null);
        builder.setView(view);
        final TextInputLayout editNameLayout = view.findViewById(R.id.otherParNameInputlayout);
        final TextInputLayout editCnicLayout = view.findViewById(R.id.otherParCnicInputlayout);
        final EditText editName = view.findViewById(R.id.otherParNameInput);
        final EditText editCnic = view.findViewById(R.id.otherParCnicInput);
        Button btnSubmit = view.findViewById(R.id.btn_submit);
        AlertDialog alertDialog = builder.create();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String cnic = editCnic.getText().toString();
                if(name.equals(""))
                {
                    editNameLayout.setError("Please Enter Name");
                }
                else if(cnic.equals(""))
                {
                    editNameLayout.setError("Please Enter Cnic");
                }
                else
                {
                    OtherParticipant otherParticipant = new OtherParticipant(name,cnic);
                    otherParticipantsList.add(otherParticipant);
                    Log.d("Others list", otherParticipantsList.toString());
                    alertDialog.dismiss();
                }


                // Handle the name and CNIC input here


            }
        });

        alertDialog.show();

    }


    public class OtherParticipant
    {
        private String Name;
        private String cnic;

        public OtherParticipant(String name, String cnic) {
            Name = name;
            this.cnic = cnic;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getCnic() {
            return cnic;
        }

        public void setCnic(String cnic) {
            this.cnic = cnic;
        }

        @Override
        public String toString() {
            return "OtherParticipant{" +
                    "Name='" + Name + '\'' +
                    ", cnic='" + cnic + '\'' +
                    '}';
        }
    }

    public class attendeeOtherParticipantsAdapter extends RecyclerView.Adapter<attendeeOtherParticipantsAdapter.viewHolder>{

        @NonNull
        @Override
        public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getContext()).inflate(R.layout.attendee_other_participants_card, parent, false);
            return new viewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull viewHolder holder, int position) {
            OtherParticipant otherParticipant = otherParticipantsList.get(position);

            holder.otherParName.setText(otherParticipant.getName());
            holder.otherParCnic.setText(otherParticipant.getCnic());
            holder.otherParNo.setText("Participant "+position);
            int pos=position;

            holder.deletePar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   otherParticipantsList.remove(pos);
                }
            });
        }

        @Override
        public int getItemCount() {
            return otherParticipantsList.size();
        }

        public class viewHolder extends RecyclerView.ViewHolder
        {
            TextView otherParNo,otherParName,otherParCnic;
            ImageButton deletePar;

            public viewHolder(@NonNull View itemView) {
                super(itemView);
                otherParNo = itemView.findViewById(R.id.otherParNo);
                otherParName = itemView.findViewById(R.id.otherParName);
                otherParCnic = itemView.findViewById(R.id.otherParCnic);
                deletePar=itemView.findViewById(R.id.deletePar);
            }
        }
    }
}