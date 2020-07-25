package com.kalagala.personalschechuler.broadcastreceivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.kalagala.personalschechuler.R;
import com.kalagala.personalschechuler.activities.SplashActivity;

import java.util.UUID;

public class NotificationReceicer extends BroadcastReceiver {
    public static String TAG = "NotificationReceiver";
    public static final String TASK_ID = "task id";
    final String CHANNEL_ID = "new picture alert";
    public static final String TITLE = "notification title";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "received a task "+intent);
        String tasktitle = (intent.getExtras()).getString(TITLE);
        int notificationId= (intent.getExtras()).getInt(TASK_ID);
        NotificationManager notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent application = new Intent(context, SplashActivity.class);
        application.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        createNotificationChannel(context);
        Notification notification= new NotificationCompat.Builder(context,CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(tasktitle+" time")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();


        notificationManager.notify(notificationId,notification);
    }


    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Task Notification";
            String description = "time for doing your tasks";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
