package com.example.eventtra;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class addSubeventsAndHeads extends Fragment {


    private Button addSubEventBtn;
    private TextInputLayout mainHeadLayout,mainSubEventLayout;
    private EditText mainHeadText,mainSubEventText;
    private LinearLayout dynamicContainer;
    private int subEventCount;
    private ArrayList<Map<String,String>> subEventsList=new ArrayList<Map<String,String>>();

    private String[] emails = {"ms6742988@gmail.com","hello@gmail.com","abc@gmail.com"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_subeventandheads, container, false);
        mainHeadLayout = view.findViewById(R.id.mainHeadlayout);
        mainSubEventLayout = view.findViewById(R.id.mainSubEventlayout);

        mainHeadText = view.findViewById(R.id.mainHeadBox);
        mainSubEventText = view.findViewById(R.id.mainSubEventBox);

        addSubEventBtn=view.findViewById(R.id.addSubEventBtn);

        dynamicContainer=view.findViewById(R.id.dynamicContainer);

        addSubEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFields(mainSubEventText.getText().toString(),mainHeadText.getText().toString());
            }
        });

        subEventCount=0;



        return view;
    }

    private void addFields(String subEventName, String head) {
        if(subEventName.isEmpty())
        {
            mainSubEventLayout.setError("Sub Event Name Can't Be Empty");
        }
        else if(!Arrays.asList(emails).contains(head))
        {
            mainHeadLayout.setError("User Not Found");
        }
        else
        {
            subEventCount++;
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.sub_event_portion, null);

            TextInputLayout headLayout = addView.findViewById(R.id.headInputlayout);
            TextInputLayout subEventLayout = addView.findViewById(R.id.subBoxInputlayout);
            EditText headText = addView.findViewById(R.id.headBox);
            EditText subEventText = addView.findViewById(R.id.subBox);
            ImageButton menuBtn = addView.findViewById(R.id.subEventMenuBtn);
            int indexLocal = subEventCount;

            ///setting menu of layouts
            menuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(getActivity(), v);
                    popup.inflate(R.menu.subevent_menu);
                    MenuItem saveItem = popup.getMenu().findItem(R.id.saveSubEvent);
                    MenuItem removeItem = popup.getMenu().findItem(R.id.removeSubEvent);
                    saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem item) {

                            Toast.makeText(getActivity(), "save clicked"+indexLocal, Toast.LENGTH_SHORT).show();
                            Log.d("sub events before updated", subEventsList.toString());
                            Map<String,String> subEventTemp =new HashMap<>();
                            subEventTemp.put(subEventText.getText().toString(),headText.getText().toString());
                            subEventsList.set(indexLocal-1,subEventTemp);
                            Log.d("sub events updated", subEventsList.toString());
                            return true;
                        }
                    });
                    removeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem item) {
                            Toast.makeText(getActivity(), "remove clicked ok"+subEventText.getText().toString(), Toast.LENGTH_SHORT).show();
                            subEventsList.remove(indexLocal-1);
                            ((LinearLayout)addView.getParent()).removeView(addView);
                            Log.d("sub events removesd", subEventsList.toString());
                            return true;
                        }
                    });
                    popup.show();
                }
            });


            Map<String,String> subEventTemp =new HashMap<>();
            subEventTemp.put(subEventName,head);
            subEventsList.add(subEventTemp);
            Log.d("sub events added", subEventsList.toString());

            headText.setText(head);
            subEventText.setText(subEventName);
            dynamicContainer.addView(addView);

            //setting botttom margin
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) addView.getLayoutParams();
            final float scale = getContext().getResources().getDisplayMetrics().density;
            int pixels = (int) (5 * scale + 0.5f);
            params.setMargins(0, 0, 0,pixels );
        }
    }


}