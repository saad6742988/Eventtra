package com.example.eventtra.AllUsers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.eventtra.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseNotificationMessaging extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        String activityToOpen = message.getData().get("activity_name");
        String channel_id = message.getData().get("channel_id");
        Log.d("Notification Test", "onMessageReceived: ");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    channel_id,channel_id,NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication(),channel_id);
        builder.setContentTitle(message.getNotification().getTitle());
        builder.setContentText(message.getNotification().getBody());
        builder.setSmallIcon(R.drawable.logo);
        builder.setColor(0xFF017703);
        builder.setAutoCancel(true);
        try {
            Intent intent =new Intent(getApplication(), Class.forName(getPackageName()+"."+message.getData().get("activity_name")));
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("message",message);
            PendingIntent pendingIntent=PendingIntent.getActivity(getApplication(),0,intent,PendingIntent.FLAG_IMMUTABLE);
            builder.setContentIntent(pendingIntent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(getApplication());
        managerCompat.notify(1,builder.build());



    }
}
