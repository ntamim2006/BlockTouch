package com.nt.blocktouch;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class FloatingViewService extends Service implements View.OnClickListener{
    private static final String ACTION_EXIT 		= "com.nt.ourgame.game_zone_activities.action_exit";


    private WindowManager mWindowManager;
    private View mFloatingView;
    private View expandedView;
    Handler handler = new Handler();;
    Handler handler_close_auto = new Handler();;

    Handler handler_double;
    int i=0;
    boolean ifHasExtra = false;
    private boolean ifLock = false;
    private boolean ifLayoutOpen = true;
    Button btn;
    ImageView imageLock, exitBtn, drawer;
    WindowManager.LayoutParams params;
    int LAYOUT_FLAG;
    public FloatingViewService() {

    }

    LinearLayout lin;
    Intent intent = new Intent();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        ifHasExtra = intent.hasExtra("keyExtra");


        if(!ifHasExtra){
            //click from notification
            if (ifLock){
                openPhoneFromNotification();
            }else{
                closePhoneFromNotification();
            }

        }else{
            //click from MainActivity button
            updateNotification(false);
        }


        //click from system panel
        if(intent.hasExtra("lock_state")) {
            if (intent.getStringExtra("lock_state").equals("open")) {
                closePhoneFromNotification();
            }else{
                openPhoneFromNotification();
            }
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
        ifHasExtra = intent.hasExtra("extra_key");


        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        drawer = mFloatingView.findViewById(R.id.drawer);
        exitBtn = mFloatingView.findViewById(R.id.close_btn);
        lin = mFloatingView.findViewById(R.id.lin);
        btn = mFloatingView.findViewById(R.id.background_btn);

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
        imageLock = mFloatingView.findViewById(R.id.blockTouchButton);

        //adding click listener to close button and expanded view
        imageLock.setOnClickListener(this);
        mFloatingView.findViewById(R.id.close_btn).setOnClickListener(this);
        expandedView.setOnClickListener(this);
        drawer.setOnClickListener(this);


        btn.setOnClickListener(v -> {

            i++;
            handler_double = new Handler();
            handler_double.postDelayed(() -> {
                if(i==2){
                    lin.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_with_corner_back));
                    openPhoneFromClickTwice();
                }
                i=0;
            }, 300);


        });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public void onClick(View v) {
            if(v.getId() == R.id.drawer){
                if(ifLayoutOpen){
                    closeDrawer();

                }else{
                    openDrawer(false, ifLock,true);
                }
            }else if (v.getId() == R.id.blockTouchButton) {

                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                }
                if (handler_close_auto != null) {
                    handler_close_auto.removeCallbacksAndMessages(null);
                }
                if (handler_double != null) {
                    handler_double.removeCallbacksAndMessages(null);
                }

                //switching views
                if (ifLock) {
                    openPhone();
                    updateNotification(false);
                } else {
                    closePhone();
                    updateNotification(true);
                }

            }else if(v.getId() == R.id.close_btn){
                    // click on close btn
                    Intent serviceIntent = new Intent(this, NotificationService.class);
                    stopService(serviceIntent);
                    stopSelf();

        }
    }



    private void openDrawer(boolean dim, boolean screenLocked, boolean enableClick) {
        // change the drawer ui if the screen is locked or open

        lin.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_with_corner_back));
        if(dim){
            if(screenLocked){
                imageLock.setImageResource(R.drawable.lock_sign_close_2_dimmed);
            }
            else{
                imageLock.setImageResource(R.drawable.lock_sign_open_dimmed);
            }
            exitBtn.setImageResource(R.drawable.close_btn_icon_dimmed);
            drawer.setImageResource(R.drawable.open_drawer_icon_dim);
        }else{
            if(screenLocked){
                imageLock.setImageResource(R.drawable.lock_sign_close_2);
            }
            else{
                imageLock.setImageResource(R.drawable.lock_sign_open);
            }
            exitBtn.setImageResource(R.drawable.close_btn_icon);
            drawer.setImageResource(R.drawable.open_drawer_icon);
        }

        imageLock.setVisibility(View.VISIBLE);
        exitBtn.setVisibility(View.VISIBLE);

        ifLayoutOpen=true;

        // check if the lock button need to be enabled or disabled
        if(enableClick){
            imageLock.setEnabled(true);
        }

        Animation rightSwipe = AnimationUtils.loadAnimation(this, R.anim.left_to_right_animation);
        lin.startAnimation(rightSwipe);

    }

    private void dim() {
        // change all the ui elements to there dimmed images

        if(ifLock){
            imageLock.setImageResource(R.drawable.lock_sign_close_2_dimmed);
        }
        else{
            imageLock.setImageResource(R.drawable.lock_sign_open_dimmed);
        }

        exitBtn.setImageResource(R.drawable.close_btn_icon_dimmed);
        drawer.setImageResource(R.drawable.open_drawer_icon_dim);
    }

    private void closeDrawer() {
        Animation rightSwipe = AnimationUtils.loadAnimation(this, R.anim.right_to_left_animation);
        rightSwipe.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lin.setBackground(null);
                imageLock.setVisibility(View.GONE);
                exitBtn.setVisibility(View.GONE);
                ifLayoutOpen=false;

                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(1300);
                drawer.startAnimation(fadeIn);

                dim();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        lin.startAnimation(rightSwipe);
        ifLayoutOpen = false;

    }



    private void closePhone() {
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        imageLock.setImageResource(R.drawable.lock_sign_close_2);
        exitBtn.setImageResource(R.drawable.close_btn_icon);
        drawer.setImageResource(R.drawable.open_drawer_icon);

        Toast.makeText(this, R.string.screen_is_close, Toast.LENGTH_SHORT).show();
        ifLock=!ifLock;

        btn.setVisibility(View.VISIBLE);
        handler_close_auto = new Handler();

        handler_close_auto.postDelayed(() -> {
            dim();
            //write your code here to be executed after 1 second
            new Handler().postDelayed(this::closeDrawer, 500);
        }, 1500);


        params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 0;

        mWindowManager.updateViewLayout(mFloatingView, params);
    }

    private void closePhoneFromNotification() {
        openDrawer(false, true, true);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        ifLock = true;

        Toast.makeText(this, R.string.screen_is_close, Toast.LENGTH_SHORT).show();


        btn.setVisibility(View.VISIBLE);
        handler_close_auto = new Handler();
        handler_close_auto.postDelayed(() -> {
                dim();
                //write your code here to be executed after 1 second
                new Handler().postDelayed(this::closeDrawer, 500);
            }, 1500);

        updateNotification(true);

        params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 0;

        mWindowManager.updateViewLayout(mFloatingView, params);
    }

    private void openPhoneFromNotification() {

        handler.removeCallbacksAndMessages(null);
        openDrawer(false, false, true);
        btn.setVisibility(View.GONE);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        ifLock = false;

        Toast.makeText(this, R.string.screen_is_open, Toast.LENGTH_SHORT).show();


        handler_close_auto = new Handler();
        handler_close_auto.postDelayed(() -> {
            dim();
            //write your code here to be executed after 1 second
            new Handler().postDelayed(this::closeDrawer, 500);
        }, 1500);

        updateNotification(false);

        params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 0;

        mWindowManager.updateViewLayout(mFloatingView, params);





    }

    private void openPhoneFromClickTwice(){

        openDrawer(true, false, false);
        btn.setVisibility(View.GONE);
        ifLock = false;

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        Toast.makeText(this, R.string.screen_is_open, Toast.LENGTH_SHORT).show();
        ifLock=!ifLock;


        //write your code here to be executed after 1 second
        handler.postDelayed(() -> {
            imageLock.setImageResource(R.drawable.lock_sign_close_2_dimmed);
            btn.setVisibility(View.VISIBLE);
            ifLock = true;
            closeDrawer();
            Toast.makeText(this, R.string.screen_is_close, Toast.LENGTH_SHORT).show();
        }, 3000);


        params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 0;

        mWindowManager.updateViewLayout(mFloatingView, params);


    }

    private void openPhone() {
        btn.setVisibility(View.GONE);
        lin.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_with_corner_back));

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        ((ImageView)mFloatingView.findViewById(R.id.close_btn)).setImageResource(R.drawable.close_btn_icon);
        ((ImageView)mFloatingView.findViewById(R.id.close_btn)).setVisibility(View.VISIBLE);
        imageLock.setVisibility(View.VISIBLE);
        drawer.setImageResource(R.drawable.open_drawer_icon);


        imageLock.setImageResource(R.drawable.lock_sign_open);

        Toast.makeText(this, "מסך המכשיר פעיל", Toast.LENGTH_SHORT).show();
        ifLock=!ifLock;

        handler = new Handler();
        handler.postDelayed(() -> {
            //write your code here to be executed after 1 second
            dim();
            }, 1500);


        params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 0;

        mWindowManager.updateViewLayout(mFloatingView, params);
    }

    private void updateNotification(boolean state){
        // update the notification with the change of the screen lock state
        Intent serviceIntent;
        serviceIntent = new Intent(this, NotificationService.class);
        serviceIntent.putExtra("inputExtra", "serviceIntentExtra");
        if (state){
            serviceIntent.putExtra("lock_state", "open");
        }else{
            serviceIntent.putExtra("lock_state", "close");
        }
        ContextCompat.startForegroundService(this, serviceIntent);

    }



}