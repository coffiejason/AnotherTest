package com.figure.anothertest;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

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
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("TAG1", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("TAG2", "Message data payload: " + remoteMessage.getData());

            /* Check if data needs to be processed by long running job */
            // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
            //scheduleJob();
            // Handle message within 10 seconds
            //handleNow();

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("TAG3", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void storeToken(String token){
        SharedPrefs.getInstance(getApplicationContext()).storeToken(token);
    }
}
