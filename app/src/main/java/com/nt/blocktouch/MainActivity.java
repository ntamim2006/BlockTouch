package com.nt.blocktouch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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

        if (!Settings.canDrawOverlays(this)) {
            askPermission();
        }

        findViewById(R.id.buttonCreateWidget).setOnClickListener(this);
        findViewById(R.id.buttonStopNotification).setOnClickListener(this);
        findViewById(R.id.buttonStartNotification).setOnClickListener(this);


    }


    public void startService() {
        serviceIntent = new Intent(this, NotificationService.class);
        serviceIntent.putExtra("inputExtra", "serviceIntentExtra");
        serviceIntent.putExtra("lock_state", "close");
        ContextCompat.startForegroundService(this, serviceIntent);
    }



    public void stopService() {
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

            if(v.getId() == R.id.buttonCreateWidget){
                if (Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(MainActivity.this, FloatingViewService.class);
                    intent.putExtra("keyExtra", "from_main");

                    startService(intent);
                    finish();

                } else {
                    askPermission();
                    Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
                }
            }else if(v.getId() == R.id.buttonStopNotification)
                stopService();
            else if(v.getId() == R.id.buttonStartNotification)
                startService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}