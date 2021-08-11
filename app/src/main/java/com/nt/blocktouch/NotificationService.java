package com.nt.blocktouch;

import static com.nt.blocktouch.App.CHANNEL_ID;

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
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, FloatingViewService.class);

        PendingIntent pendingIntent = PendingIntent.getService(this,
                0, notificationIntent, 0);

        Notification notification;
        if (intent.getStringExtra("lock_state").equals("open")) {
            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Block Touch")
                    .setContentText(getString(R.string.screen_locked_click_to_open))
                    .setSmallIcon(R.drawable.lock_sign_open_new_icon_red)
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.mipmap.lock_sign_open_new_icon_red))
                    .setPriority(NotificationManager.IMPORTANCE_MAX)
                    .setCategory(Notification.CATEGORY_SERVICE)

                    .build();
        }else{
             notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Block Touch")
                    .setContentText(getString(R.string.screen_open_click_to_close))
                    .setSmallIcon(R.drawable.lock_sign_open_new_icon)
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.mipmap.lock_sign_open_new_icon))
                    .setPriority(NotificationManager.IMPORTANCE_MAX)
                    .setCategory(Notification.CATEGORY_SERVICE)

                    .build();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground(pendingIntent, intent);
        else {
            startForeground(1, notification);
        }
        return START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void startMyOwnForeground(PendingIntent pendingIntent, Intent intent){
        String NOTIFICATION_CHANNEL_ID = "com.nt.blocktouch";
        String channelName = "notification";
        NotificationChannel chan = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW);
            chan.setLightColor(Color.WHITE);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }

        Notification notification;
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
                notificationBuilder
                        .setOngoing(false)
                        .setSmallIcon(R.drawable.lock_sign_open_new_icon_red)
                        .setContentTitle("Block Touch")
                        .setPriority(NotificationManager.IMPORTANCE_MAX)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .setContentIntent(pendingIntent)
                        .build();


            if (intent.getStringExtra("lock_state").equals("open")) {
                notificationBuilder
                                .setContentText(getString(R.string.screen_locked_click_to_open))
                                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                        R.mipmap.lock_sign_open_new_icon_red));
            } else {
                 notificationBuilder
                         .setContentText(getString(R.string.screen_open_click_to_close))
                         .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                 R.mipmap.lock_sign_open_new_icon));
            }

        notification = notificationBuilder.build();
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