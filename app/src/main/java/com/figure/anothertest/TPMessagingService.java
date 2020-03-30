package com.figure.anothertest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class TPMessagingService extends FirebaseMessagingService {

    public static final String TOKEN_BROADCAST = "fcmtokenbroadcast";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);


        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
        storeToken(s);
        Log.d("GOtttTokennnnn", "hjhjjn"+s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            showNotifications(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            showNotifications(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private RemoteViews getCustomDesign(String title, String message){

        RemoteViews remoteViews=new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.title,title);
        remoteViews.setTextViewText(R.id.message,message);
        remoteViews.setImageViewResource(R.id.icon,R.drawable.web_hi_res_512);
        return remoteViews;
    }

    private void storeToken(String token){
        SharedPrefs.getInstance(getApplicationContext()).storeToken(token);
    }

    public void showNotifications(String title, String message){
        Intent intent=new Intent(this, ErrandMapActivity.class);
        String channel_id="TP_notification";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),channel_id)
                .setSmallIcon(R.drawable.web_hi_res_512)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            builder=builder.setContent(getCustomDesign(title,message));
        }
        else{
            builder=builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.web_hi_res_512);
        }

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(channel_id,"web_app",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri,null);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        assert notificationManager != null;
        notificationManager.notify(0,builder.build());

    }


}
