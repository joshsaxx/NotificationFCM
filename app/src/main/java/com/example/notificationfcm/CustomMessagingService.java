package com.example.notificationfcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class CustomMessagingService extends FirebaseMessagingService {

    NotificationManager notificationManager;
    Notification notification;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() !=null){
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();

            generateNotification(title,message);
           // Toast.makeText(this,"notification received",Toast.LENGTH_SHORT).show();

            Log.e("NOTIFICATION",title+"message");

        }

    }

    public void generateNotification(String noteTitle, String noteMSg){

        Intent intent = new Intent(CustomMessagingService.this,MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(CustomMessagingService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager =  (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //Include Notification Sound/Tone
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT>=26){

            String channelId = "com.example.notificationfcm";
            String channelName = "FCMDemo";

            NotificationChannel notificationChannel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            assert notificationManager!= null;
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(CustomMessagingService.this,channelId);
            notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(Color.BLUE)
                    .setAutoCancel(true)
                    .setTicker(noteMSg)
                    .setContentTitle(noteTitle)
                    .setContentText(noteMSg)
                    .setSound(alarmSound)
                    .setDefaults(Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS)
                    .setContentIntent(pendingIntent);

            notification = notificationBuilder.build();
        }else {

            Notification.Builder nb = new Notification.Builder(CustomMessagingService.this);
            nb.setSmallIcon(R.mipmap.ic_launcher);
                    nb.setContentTitle(noteTitle);
                    nb.setContentText(noteMSg);
                    nb.setContentIntent(pendingIntent);
                    nb.setSound(alarmSound);
                    nb.build();

                    notification = nb.getNotification();
                    notification.flags =Notification.FLAG_AUTO_CANCEL;


        }

        if (Build.VERSION.SDK_INT>=26){

            startForeground(0,notification);
        }else {
            notificationManager.notify(0,notification);
        }


    }


    //foreground, display message in textview
    //background , show notification, on tapped, display message in textview
}
