package com.example.eventtra;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
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

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class attendee_event_enrollment extends Fragment {


    Button addNewParticipant,PaymentBtn;
    EditText attendeeMainRegisterName,attendeeMainRegisterCnic;
    TextInputLayout attendeeMainRegisterCniclayout;
    TextView registeringEventName;
    RecyclerView otherParRecyclerView;
    ArrayList<OtherParticipant> otherParticipantsList = new ArrayList<>();
    GlobalData globalData;

    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration customerConfig;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_event_enrollment, container, false);
        globalData=(GlobalData)getContext().getApplicationContext();
        addNewParticipant = view.findViewById(R.id.addNewParticipantBtn);
        otherParRecyclerView = view.findViewById(R.id.otherParRecyclerView);
        attendeeMainRegisterCnic= view.findViewById(R.id.attendeeMainRegisterCnic);
        attendeeMainRegisterName = view.findViewById(R.id.attendeeMainRegisterName);
        attendeeMainRegisterName.setText(globalData.globalUser.getFname()+" "+globalData.globalUser.getLname());
        registeringEventName = view.findViewById(R.id.registeringEventName);
        registeringEventName.setText(globalData.globalSubEvent.getName());
        addNewParticipant = view.findViewById(R.id.addNewParticipantBtn);
        attendeeMainRegisterCniclayout = view.findViewById(R.id.attendeeMainRegisterCniclayout);
        PaymentBtn=view.findViewById(R.id.PaymentBtn);

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        otherParRecyclerView.setLayoutManager(layoutManager);

        addNewParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOtherParticipant();
            }
        });

        PaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attendeeMainRegisterCnic.getText().toString().equals(""))
                {
                    attendeeMainRegisterCniclayout.setError("Cnic Required");
                }
                else{
                    makePayment();
                }

            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    private void makePayment() {
        int amount  = Integer.parseInt(globalData.globalSubEvent.getPrice())*100*(otherParticipantsList.size()+1);
        Log.d("amount", "makePayment: "+(amount));
        Fuel.INSTANCE.post("https://stripe-payment-production.up.railway.app/payment?amount="+amount+"&email="+globalData.globalUser.getEmail()+"&des=this is test", null).responseString(new Handler<String>() {
            @Override
            public void success(String s) {
                try {
                    final JSONObject result = new JSONObject(s);
                    customerConfig = new PaymentSheet.CustomerConfiguration(
                            result.getString("customer"),
                            result.getString("ephemeralKey")
                    );
                    paymentIntentClientSecret = result.getString("paymentIntent");
                    PaymentConfiguration.init(getContext().getApplicationContext(), result.getString("publishableKey"));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            presentPaymentSheet();
                        }
                    });

                } catch (JSONException e) {
                    Log.d("Error", "success: "+e.getMessage());
                }
            }

            @Override
            public void failure(@NonNull FuelError fuelError) {
                Log.d("Error", "failure: "+fuelError.getMessage());
            }
        });

    }
    private void presentPaymentSheet() {
        final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Eventtra")
                .customer(customerConfig)
                .allowsDelayedPaymentMethods(true)
                .build();
        paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                configuration
        );
    }

    private void onPaymentSheetResult(
            final PaymentSheetResult paymentSheetResult
    ) {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d("Payment status", "onPaymentSheetResult: canncled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.d("App", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            Log.d("Payment status", "onPaymentSheetResult: success");
        }
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
                    editCnicLayout.setError("Please Enter Cnic");
                }
                else
                {
                    OtherParticipant otherParticipant = new OtherParticipant(name,cnic);
                    otherParticipantsList.add(otherParticipant);
                    Log.d("Others list", otherParticipantsList.toString());
                    updateOthersList();
                    alertDialog.dismiss();
                }


                // Handle the name and CNIC input here


            }
        });

        alertDialog.show();

    }

    private void updateOthersList() {
        attendeeOtherParticipantsAdapter adapter= new attendeeOtherParticipantsAdapter();
        otherParRecyclerView.setAdapter(adapter);
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
            holder.otherParNo.setText("Participant "+(position+1));
            int pos=position;

            holder.deletePar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   otherParticipantsList.remove(pos);
                   updateOthersList();
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