package com.nt.blocktouch;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askPermission();
        }

        findViewById(R.id.buttonCreateWidget).setOnClickListener(this);
        findViewById(R.id.buttonStopNotification).setOnClickListener(this);
        findViewById(R.id.buttonStartNotification).setOnClickListener(this);
        Intent intent = new Intent(MainActivity.this, FloatingViewService.class);
        intent.putExtra("your_key_here", "from_main");
        startService(intent);

    }



    public void startService() {
        serviceIntent = new Intent(this, NotificationService.class);
        serviceIntent.putExtra("inputExtra", "serviceIntentExtra");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService(View v) {
        serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
    }


    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonCreateWidget:
                if (Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(MainActivity.this, FloatingViewService.class);
                    intent.putExtra("your_key_here", "from_main");
                    startService(intent);
                    finish();
                } else {
                    askPermission();
                    Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.buttonStopNotification:
                Log.d("ddd", "dd");
                stopService(v);
//                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.cancelAll();
                break;

            case R.id.buttonStartNotification:
                startService();
                break;
        }


    }


}