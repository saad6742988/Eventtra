package com.example.eventtra;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class attendee_event_enrollment extends Fragment {


    Button addNewParticipant,PaymentBtn,PayLaterBtn;
    EditText attendeeMainRegisterName,attendeeMainRegisterCnic;
    TextInputLayout attendeeMainRegisterCniclayout;
    TextView registeringEventName,requiredOtherPar;
    RecyclerView otherParRecyclerView;
    ArrayList<OtherParticipant> otherParticipantsList = new ArrayList<>();
    GlobalData globalData;
    PaymentInfo paymentInfo=new PaymentInfo();

    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration customerConfig;

    final private FirebaseFirestore database =FirebaseFirestore.getInstance();
    final private CollectionReference paymentsCollection = database.collection("Payments");
    private AlertDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendee_event_enrollment, container, false);
        globalData=(GlobalData)getContext().getApplicationContext();
        Log.d("user data", "onCreateView: "+globalData.globalUser);
        addNewParticipant = view.findViewById(R.id.addNewParticipantBtn);
        otherParRecyclerView = view.findViewById(R.id.otherParRecyclerView);
        attendeeMainRegisterCnic= view.findViewById(R.id.attendeeMainRegisterCnic);
        attendeeMainRegisterName = view.findViewById(R.id.attendeeMainRegisterName);
        attendeeMainRegisterName.setText(globalData.globalUser.getFname()+" "+globalData.globalUser.getLname());
        registeringEventName = view.findViewById(R.id.registeringEventName);
        registeringEventName.setText(globalData.globalSubEvent.getName());
        addNewParticipant = view.findViewById(R.id.addNewParticipantBtn);
        requiredOtherPar = view.findViewById(R.id.requiredOtherPar);
        requiredOtherPar.setText(" (Required "+(globalData.globalSubEvent.getMinParticipants()-1)+" more)");
        attendeeMainRegisterCniclayout = view.findViewById(R.id.attendeeMainRegisterCniclayout);
        PaymentBtn=view.findViewById(R.id.PaymentBtn);
        PayLaterBtn=view.findViewById(R.id.PayLaterBtn);
        paymentInfo.setMadeBy(globalData.globalUser.getUserId());

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
                else if(otherParticipantsList.size()+1<globalData.globalSubEvent.getMinParticipants())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    AlertDialog dialog;
                    builder.setTitle("Error!");
                    builder.setMessage("Please Add "+(globalData.globalSubEvent.getMinParticipants()-1-otherParticipantsList.size())+
                            " More Participants");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog = builder.create();
                    dialog.show();
                }
                else{
                    makePayment();
                }

            }
        });

        PayLaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payLater();
            }
        });


        // Inflate the layout for this fragment
        return view;
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

    private void payLater() {
        showLoading();
        paymentInfo.setTid("Cash");
        paymentInfo.setAmount(Integer.parseInt(globalData.globalSubEvent.getPrice()));
        paymentInfo.setStatus(false);
        paymentInfo.setSubEventID(globalData.globalSubEvent.getSubEventId());
        paymentInfo.setSubEventName(globalData.globalSubEvent.getName());
        paymentInfo.setParticipantName(attendeeMainRegisterName.getText().toString());
        paymentInfo.setParticipantCnic(attendeeMainRegisterCnic.getText().toString());
        paymentInfo.setTimeStamp(System.currentTimeMillis());

        FirebaseMessaging.getInstance().subscribeToTopic(globalData.globalSubEvent.getName()+"_"+
                globalData.globalSubEvent.getSubEventId());

        paymentsCollection.add(paymentInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                if(otherParticipantsList.size()>0) {
                    for (int i = 0; i < otherParticipantsList.size(); i++) {
                        paymentInfo.setParticipantName(otherParticipantsList.get(i).getName());
                        paymentInfo.setParticipantCnic(otherParticipantsList.get(i).getCnic());
                        Log.d("Registration status", "Registered Successfully, Payment Pending");
                        paymentsCollection.add(paymentInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getContext(), "Registered Successfully, Payment Pending", Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                                gotoStartPage();
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Registered Successfully, Payment Pending", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    gotoStartPage();
                }
            }
        });
    }

    private void makePayment() {
        int amount  = Integer.parseInt(globalData.globalSubEvent.getPrice())*100*(otherParticipantsList.size()+1);
        Log.d("amount", "makePayment: "+(amount));
        showLoading();
        Fuel.INSTANCE.post("https://stripe-payment-production.up.railway.app/payment?amount="+amount+"&email="+globalData.globalUser.getEmail()+"&des=this is test", null).responseString(new Handler<String>() {
            @Override
            public void success(String s) {
                try {
                    final JSONObject result = new JSONObject(s);
                    customerConfig = new PaymentSheet.CustomerConfiguration(
                            result.getString("customer"),
                            result.getString("ephemeralKey")
                    );
                    paymentInfo.setTid(result.getString("pid"));
                    paymentInfo.setTimeStamp(result.getLong("timeStamp")*1000);
                    Log.d("pid", "success: "+result);
                    paymentIntentClientSecret = result.getString("paymentIntent");
                    PaymentConfiguration.init(getContext().getApplicationContext(), result.getString("publishableKey"));
                    loadingDialog.dismiss();
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

    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult)
    {
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d("Payment status", "onPaymentSheetResult: canncled");
            Toast.makeText(getContext(), "Payment canceled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.d("App", "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
            Toast.makeText(getContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
        }
        else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            showLoading();
            //subscribing for this event topic
            FirebaseMessaging.getInstance().subscribeToTopic(globalData.globalSubEvent.getName()+"_"+
                    globalData.globalSubEvent.getSubEventId());



            // Display for example, an order confirmation screen
            paymentInfo.setAmount(Integer.parseInt(globalData.globalSubEvent.getPrice()));
            paymentInfo.setStatus(true);
            paymentInfo.setSubEventID(globalData.globalSubEvent.getSubEventId());
            paymentInfo.setSubEventName(globalData.globalSubEvent.getName());
            paymentInfo.setParticipantName(attendeeMainRegisterName.getText().toString());
            paymentInfo.setParticipantCnic(attendeeMainRegisterCnic.getText().toString());
            Log.d("Payment status", "onPaymentSheetResult: success");
            paymentsCollection.add(paymentInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                    if(otherParticipantsList.size()>0) {
                        for (int i = 0; i < otherParticipantsList.size(); i++) {
                            paymentInfo.setParticipantName(otherParticipantsList.get(i).getName());
                            paymentInfo.setParticipantCnic(otherParticipantsList.get(i).getCnic());
                            Log.d("Registration status", "Registered Successfully, Payment Pending");
                            paymentsCollection.add(paymentInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getContext(), "Registered Successfully, Payment Pending", Toast.LENGTH_SHORT).show();
                                    loadingDialog.dismiss();
                                    showReceipt();
                                }
                            });
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Registered Successfully, Payment Pending", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        showReceipt();
                    }
                }
            });


        }

    }
    private void showReceipt()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.overall_enrollment_receipt, null);
        builder.setView(view);
        final TextView subeventNametv = view.findViewById(R.id.subeventNametv);
        subeventNametv.setText(paymentInfo.getSubEventName());
        final TextView tidtv = view.findViewById(R.id.tidtv);
        tidtv.setText(paymentInfo.getTid());
        final TextView timetv = view.findViewById(R.id.timetv);
        Date d = new Date(paymentInfo.getTimeStamp()*1000);
        timetv.setText(d.toString());
        final TextView parNotv = view.findViewById(R.id.parNotv);
        parNotv.setText(String.valueOf(otherParticipantsList.size()+1));
        final TextView amounttv = view.findViewById(R.id.amounttv);
        amounttv.setText(String.valueOf(paymentInfo.getAmount()*(otherParticipantsList.size()+1)));
        final TextView madeBytv = view.findViewById(R.id.madeBytv);
        madeBytv.setText(globalData.globalUser.getFname()+" "+globalData.globalUser.getLname());
        final TextView statustv = view.findViewById(R.id.statustv);
        statustv.setText("Paid");
        ImageButton closeBtn = view.findViewById(R.id.closeBtn);
        AlertDialog alertDialog = builder.create();

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                gotoStartPage();
            }
        });

        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void gotoStartPage() {
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().popBackStack();
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
                    if((globalData.globalSubEvent.getMinParticipants()-1-otherParticipantsList.size())>=0)
                        requiredOtherPar.setText(" (Required "+(globalData.globalSubEvent.getMinParticipants()-1-otherParticipantsList.size())+" more)");

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
                    requiredOtherPar.setText(" (Required "+(globalData.globalSubEvent.getMinParticipants()-1-otherParticipantsList.size())+" more)");
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