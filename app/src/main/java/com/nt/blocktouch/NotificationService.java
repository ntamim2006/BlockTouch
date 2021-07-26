package com.nt.blocktouch;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.nt.blocktouch.App.CHANNEL_ID;

public class NotificationService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, FloatingViewService.class);



        PendingIntent pendingIntent = PendingIntent.getService(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Block Touch")
                .setContentText("לחץ כאן לנעילת מסך המגע")
                .setSmallIcon(R.drawable.lock_sign_open_new_icon_red)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.mipmap.lock_sign_open_new_icon_red))
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)

                .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground(pendingIntent);
        else
            startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    private void startMyOwnForeground(PendingIntent pendingIntent){
        String NOTIFICATION_CHANNEL_ID = "com.nt.blocktouch";
        String channelName = "notification";
        NotificationChannel chan = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
            chan.setLightColor(Color.WHITE);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.lock_sign_open_new_icon_red)
                .setContentTitle("Block Touch")
                .setContentText("לחץ כאן לנעילת מסך המגע")
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.mipmap.lock_sign_open_new_icon_red))
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(2, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}