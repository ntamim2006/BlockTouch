package com.nt.blocktouch;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class FloatingViewService extends Service implements View.OnClickListener {


    private WindowManager mWindowManager;
    private View mFloatingView;
    private View collapsedView;
    private View expandedView;
    boolean over_time = false;
    Handler handler;
    Handler handler2;
    Handler handler3;
    Handler handler4;
    Handler handler_double;
    Button b;
    int i=0;
    boolean ifHasExtra = false;

    private boolean ifLock = false;
    ImageView imageLock;
    WindowManager.LayoutParams params;
    int LAYOUT_FLAG;
    public FloatingViewService() {

    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("eee", intent.getStringExtra("your_key_here")+"");

        ifHasExtra = intent.hasExtra("your_key_here");



        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //setting the layout parameters
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 0;


        //getting windows services and adding the floating view to it
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);


        expandedView = mFloatingView.findViewById(R.id.layoutExpanded);
        imageLock = mFloatingView.findViewById(R.id.actionButton);
        //adding click listener to close button and expanded view
        imageLock.setOnClickListener(this);
        mFloatingView.findViewById(R.id.exp_iv).setOnClickListener(this);
        expandedView.setOnClickListener(this);
        b = expandedView.findViewById(R.id.button);


        b.setOnClickListener(v -> {

            i++;

            handler_double = new Handler();
            handler_double.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(i==1) {

                        //single click

                    }else if(i==2){

                        openPhone();


                        handler4 = new Handler();
                        handler4.postDelayed(() -> {
                            //write your code here to be executed after 1 second
                            closePhone();

                        }, 2000);

                    }
                    i=0;
                }
            }, 300);


        });


        if(!ifHasExtra){
            closePhone();
        }




        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
            return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.actionButton:
                if(handler!=null){
                    handler.removeCallbacksAndMessages(null);
                }
                if(handler2!=null){
                    handler2.removeCallbacksAndMessages(null);
                }
                if(handler3!=null){
                    handler3.removeCallbacksAndMessages(null);
                }
                if(handler4!=null){
                    handler4.removeCallbacksAndMessages(null);
                }
                if(handler_double!=null){
                    handler_double.removeCallbacksAndMessages(null);
                }

                //switching views
                if(ifLock){
                    openPhone();

                }else{

                    closePhone();

                }

                break;

            case R.id.exp_iv:
                //closing the widget
                if(handler!=null){
                    handler.removeCallbacksAndMessages(null);
                }
                if(handler2!=null){
                    handler2.removeCallbacksAndMessages(null);
                }
                if(handler3!=null){
                    handler3.removeCallbacksAndMessages(null);
                }
                if(handler4!=null){
                    handler4.removeCallbacksAndMessages(null);
                }
                if(handler_double!=null){
                    handler_double.removeCallbacksAndMessages(null);
                }
                stopSelf();

                break;

//                case R.id.button:
//                //closing the widget
//                    b = v.findViewById(R.id.button);
//                    b.setVisibility(View.GONE);
//
//                break;



        }
    }

    private void closePhone() {
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        imageLock.setImageResource(R.drawable.lock_sign_close_2);
        mFloatingView.findViewById(R.id.exp_iv).setVisibility(View.INVISIBLE);
        Toast.makeText(this, "מסך המכשיר נעול", Toast.LENGTH_SHORT).show();
        b.setVisibility(View.VISIBLE);
        handler3 = new Handler();

        handler3.postDelayed(() -> {
            //write your code here to be executed after 1 second
            imageLock.setImageResource(R.drawable.lock_sign_close_2_dimmed);

        }, 2000);
        ifLock=!ifLock;

        params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 0;

        mWindowManager.updateViewLayout(mFloatingView, params);

    }

    private void openPhone() {
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mFloatingView.findViewById(R.id.exp_iv).setVisibility(View.VISIBLE);
        ((ImageView)mFloatingView.findViewById(R.id.exp_iv)).setImageResource(R.drawable.close_btn_icon);

        imageLock.setImageResource(R.drawable.lock_sign_open);
        Toast.makeText(this, "מסך המכשיר פעיל", Toast.LENGTH_SHORT).show();

        handler = new Handler();
        handler.postDelayed(() -> {
            //write your code here to be executed after 1 second
            imageLock.setImageResource(R.drawable.lock_sign_open_dimmed);
            ((ImageView)mFloatingView.findViewById(R.id.exp_iv)).setImageResource(R.drawable.close_btn_icon_dimmed);
            over_time = true;


            }, 2000);
        ifLock=!ifLock;
        over_time = false;
        b.setVisibility(View.GONE);

        params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 0;

        mWindowManager.updateViewLayout(mFloatingView, params);
    }
}