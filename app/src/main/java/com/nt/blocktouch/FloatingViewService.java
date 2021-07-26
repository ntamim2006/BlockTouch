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


    private WindowManager mWindowManager;
    private View mFloatingView;
    private View expandedView;
    Handler handler;
    Handler handler2;
    Handler handler3;
    Handler handler4;
    Handler handler_double;
    int i=0;
    boolean ifHasExtra = false;
    private boolean ifLock = false;
    private boolean ifLayoutOpen = true;
    private boolean ifKidMode = false;
    Button b;
    ImageView imageLock, minusBtn, exitBtn, drawer;
    WindowManager.LayoutParams params;
    int LAYOUT_FLAG;
    public FloatingViewService() {

    }

    LinearLayout lin;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ifHasExtra = intent.hasExtra("your_key_here");



        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        drawer = mFloatingView.findViewById(R.id.drawer);
        exitBtn = mFloatingView.findViewById(R.id.close_btn);
        lin = mFloatingView.findViewById(R.id.lin);
        b = mFloatingView.findViewById(R.id.background_btn);

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
        minusBtn.setOnClickListener(this);

        if(!ifHasExtra){
            closePhone(false);
        }

        b.setOnClickListener(v -> {

            i++;

            handler_double = new Handler();
            handler_double.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(i==2){
                        lin.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_with_corner_back));

                        openPhone(true);
                        //if you want close automatically
//                        handler4 = new Handler();
//                        handler4.postDelayed(() -> {
//                            //write your code here to be executed after 1 second
//                            closePhone(true);
//
//                        }, 1500);

                    }
                    i=0;
                }
            }, 300);


        });


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

            case R.id.drawer:


                if(ifLayoutOpen){
                    closeDrawer();


                }else{
                    openDrawer();
                    Animation rightSwipe = AnimationUtils.loadAnimation(this, R.anim.left_to_right_animation);


                    lin.startAnimation(rightSwipe);
                }



                break;

            case R.id.blockTouchButton:


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
                        openPhone(false);
                    }else{
                        closePhone(false);
                    }

                break;


            case R.id.close_btn:
                    stopSelf();
                break;

        }
    }

    private void openDrawer() {
        if(handler3!=null){
            handler3.removeCallbacksAndMessages(null);
        }
        lin.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_with_corner_back));

        if(ifLock){
            imageLock.setImageResource(R.drawable.lock_sign_close_2);
        }
        else{
            imageLock.setImageResource(R.drawable.lock_sign_open);
        }


        exitBtn.setImageResource(R.drawable.close_btn_icon);

        drawer.setImageResource(R.drawable.open_drawer_icon);

        imageLock.setVisibility(View.VISIBLE);
        exitBtn.setVisibility(View.VISIBLE);
        handler3 = new Handler();
        handler3.postDelayed(() -> {
            //write your code here to be executed after 1 second
            dim();
        }, 2000);
        ifLayoutOpen=true;

    }

    private void dim() {
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



    private void closePhone(boolean ifToClose) {
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        imageLock.setImageResource(R.drawable.lock_sign_close_2);
        exitBtn.setImageResource(R.drawable.close_btn_icon);
        drawer.setImageResource(R.drawable.open_drawer_icon);

        Toast.makeText(this, "מסך המכשיר נעול", Toast.LENGTH_SHORT).show();
        ifLock=!ifLock;

        b.setVisibility(View.VISIBLE);
        handler3 = new Handler();
        if(ifToClose) {
            handler3.postDelayed(this::closeDrawer, 1000);


        }else {
            handler3.postDelayed(() -> {
                dim();
                //write your code here to be executed after 1 second
                new Handler().postDelayed(this::closeDrawer, 500);
            }, 1500);
        }

        params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 0;

        mWindowManager.updateViewLayout(mFloatingView, params);

    }

    private void openPhone(boolean fromClickTwice) {
        b.setVisibility(View.GONE);

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

        if(fromClickTwice){
            Animation rightSwipe = AnimationUtils.loadAnimation(this, R.anim.left_to_right_animation);
            lin.startAnimation(rightSwipe);
        }

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
}