package com.zenos.thunderstorm.firebase;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.inlocomedia.android.engagement.InLocoEngagement;
import com.inlocomedia.android.engagement.PushMessage;
import com.inlocomedia.android.engagement.request.FirebasePushProvider;
import com.inlocomedia.android.engagement.request.PushProvider;
import com.zenos.thunderstorm.MainActivity;
import com.zenos.thunderstorm.R;

import java.util.Locale;
import java.util.Map;


public class FCMHandlerService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String channelId = getString(R.string.default_notification_channel_id);

        // Getting the Notification Data
        final Map<String, String> data = remoteMessage.getData();

        if (data != null) {
            // Decoding the notification data HashMap
            final PushMessage pushContent = InLocoEngagement.decodeReceivedMessage(this, data);

            if (pushContent != null) {
                Log.d(TAG, "Handling with Inloco");
                // Inloco notification handler
                InLocoEngagement.presentNotification(
                        this, // Context
                        pushContent,  // The notification message hash
                        R.mipmap.ic_launcher_round, // The notification icon drawable resource to display on the status bar. Put your own icon here. You can also use R.drawable.ic_notification for testing.
                        1111111,// Optional: The notification identifier
                        channelId
                );
            } else {
                Log.d(TAG, "Handling with FCM");
                //  Firebase notification handler
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);
                    manager.createNotificationChannel(channel);
                }
                manager.notify(0, builder.build());
            }
        }

    }

    @Override
    public void onNewToken(String firebaseToken) {
        Log.d(TAG, String.format(Locale.ENGLISH, "FCMToken %s", firebaseToken));

        if (firebaseToken != null && !firebaseToken.isEmpty()) {
            final PushProvider pushProvider = new FirebasePushProvider.Builder()
                    .setFirebaseToken(firebaseToken)
                    .build();
            InLocoEngagement.setPushProvider(this, pushProvider);
        }
    }
}