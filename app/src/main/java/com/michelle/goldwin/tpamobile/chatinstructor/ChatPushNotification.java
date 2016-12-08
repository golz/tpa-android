package com.michelle.goldwin.tpamobile.chatinstructor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Chronometer;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.michelle.goldwin.tpamobile.activity.ChatRoomActivity;

/**
 * Created by Michelle Neysa on 12/8/2016.
 */

public class ChatPushNotification extends FirebaseMessagingService {

    private static final String TAG = "ChatService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        notification(remoteMessage.getNotification().getBody());
    }

    public void notification(String msg){

        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).setContentTitle("MSGDCoorporate")
                .setContentText(msg)
                .setSound(sound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0,notificationBuilder.build());

    }
}


