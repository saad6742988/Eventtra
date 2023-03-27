package com.example.eventtra;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//{
//        "to":"feMivMi0RW6AHmrPZwbVVS:APA91bH78DblA3UmsKnhMc_3YY-sw2VdW2GMuC11C64S3_6v-r0HaOhImYyRELW2mSEzmcAzsuOHqxsPhqgglXIKZY37wzpdrTDL8Zih_p5gsfEZDbS9fT-_4uwMpd65f0KmRg0khkex",
//        "notification":{
//        "title":"hello",
//        "body":"this is text"
//        },
//        "data":
//        {
//        "activity_name":"MainActivity",
//        "channel_id":"test"
//        }
//        }


public class FCMSend {
    private static String URL = "https://fcm.googleapis.com/fcm/send";
    private static String Serrver_key = "AAAAQBhZjV0:APA91bHgDgXhzUXN16FUE5AFX2lE7HSe0qKk9BGiUOV2PlDhEEvAJvdeqVTVLkiQV_wFd2bs13pkkAe_gGHOcxa5WSZao_UzxYhLwiu6TlzoRPkTogPs7KOS8snBzAspKdpvJxHUKd9M";
    public static void pushNotification(Context context,String to,String title,String message,
                                 String activity,String channel)
    {
        Log.d("SEND DATA", to+"\n"+title+"\n"+message+"\n"+activity+"\n"+channel+"\n");


        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(context);
        try{
            JSONObject body = new JSONObject();
            JSONObject notification = new JSONObject();
            JSONObject data = new JSONObject();
            notification.put("title",title);
            notification.put("body",message);
            data.put("activity_name",activity);
            data.put("channel_id",channel);
            body.put("to",to);
            body.put("notification",notification);
            body.put("data",data);
            JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.POST, URL, body, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("REsponse APi", "onResponse: "+response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("REsponse APi", "onErrorResponse: "+error.getMessage());
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "key=" + Serrver_key);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
