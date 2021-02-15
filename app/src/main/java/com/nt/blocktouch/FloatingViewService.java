package com.nt.blocktouch;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    Button b;
    private boolean ifLock = false;
    ImageView imageLock;
    WindowManager.LayoutParams params;
    int LAYOUT_FLAG;
    public FloatingViewService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


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


        //getting the collapsed and expanded view from the floating view
//        collapsedView = mFloatingView.findViewById(R.id.layoutCollapsed);

//        collapsedView.setOnClickListener(v -> {
//            Toast.makeText(FloatingViewService.this, "click", Toast.LENGTH_SHORT).show();
//            collapsedView.setVisibility(View.GONE);
//            expandedView.setVisibility(View.VISIBLE);
//        });

        expandedView = mFloatingView.findViewById(R.id.layoutExpanded);
        imageLock = mFloatingView.findViewById(R.id.expClose);
        //adding click listener to close button and expanded view
//        mFloatingView.findViewById(R.id.buttonClose).setOnClickListener(this);
        imageLock.setOnClickListener(this);
        mFloatingView.findViewById(R.id.exp_iv).setOnClickListener(this);
        expandedView.setOnClickListener(this);
         b = expandedView.findViewById(R.id.button);




//        mFloatingView.findViewById(R.id.expClose).setOnClickListener(v -> {
//            //Initially view will be added to top-left corner
//            if(ifLock){
//                params = new WindowManager.LayoutParams(
//                        WindowManager.LayoutParams.WRAP_CONTENT,
//                        WindowManager.LayoutParams.WRAP_CONTENT,
//                        LAYOUT_FLAG,
//                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                        PixelFormat.TRANSLUCENT);
//                         mFloatingView.findViewById(R.id.exp_iv).setVisibility(View.VISIBLE);
//                        imageLock.setImageResource(R.drawable.lock_sign_open);
//
//            }else{
//                params = new WindowManager.LayoutParams(
//                        WindowManager.LayoutParams.MATCH_PARENT,
//                        WindowManager.LayoutParams.MATCH_PARENT,
//                        LAYOUT_FLAG,
//                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                        PixelFormat.TRANSLUCENT);
//                imageLock.setImageResource(R.drawable.lock_sign_close_2);
//                mFloatingView.findViewById(R.id.exp_iv).setVisibility(View.INVISIBLE);
//
//            }
//            params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
//            params.x = 0;
//            params.y = 0;
//            ifLock=!ifLock;
//            mWindowManager.updateViewLayout(mFloatingView, params);
//
//
//        });


//        //adding an touchlistener to make drag movement of the floating widget
//        mFloatingView.findViewById(R.id.layoutExpanded).setOnTouchListener(new View.OnTouchListener() {
//            private int initialX;
//            private int initialY;
//            private float initialTouchX;
//            private float initialTouchY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        initialX = params.x;
//                        initialY = params.y;
//                        initialTouchX = event.getRawX();
//                        initialTouchY = event.getRawY();
//                        return true;
//
//                    case MotionEvent.ACTION_UP:
////                        //when the drag is ended switching the state of the widget
////                        collapsedView.setVisibility(View.GONE);
////                        expandedView.setVisibility(View.VISIBLE);
////                        Toast.makeText(FloatingViewService.this, "expanded", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(FloatingViewService.this, "param.x: " + params.x , Toast.LENGTH_SHORT).show();
//                        return true;
//
//                    case MotionEvent.ACTION_MOVE:
//                        //this code is helping the widget to move around the screen with fingers
//                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
//                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
//
//                        mWindowManager.updateViewLayout(mFloatingView, params);
//                        return true;
//                }
//                return false;
//            }
//        });

            b.setOnClickListener(v -> {
                Toast.makeText(this, "d", Toast.LENGTH_SHORT).show();
            });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.expClose:
                if(handler!=null){
                    handler.removeCallbacksAndMessages(null);
                }
                if(handler2!=null){
                    handler2.removeCallbacksAndMessages(null);
                }
                if(handler3!=null){
                    handler3.removeCallbacksAndMessages(null);
                }

                //switching views
                if(ifLock){

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


                }else{
//                    if(over_time){
//                        ((ImageView)mFloatingView.findViewById(R.id.exp_iv)).setImageResource(R.drawable.close_btn_icon);
//                        mFloatingView.findViewById(R.id.exp_iv).setVisibility(View.VISIBLE);
//                        imageLock.setImageResource(R.drawable.lock_sign_open);
//
//                        over_time = false;
//
//                        handler2 = new Handler();
//                        handler2.postDelayed(() -> {
//                            //write your code here to be executed after 1 second
//                            imageLock.setImageResource(R.drawable.lock_sign_open_dimmed);
//                            ((ImageView)mFloatingView.findViewById(R.id.exp_iv)).setImageResource(R.drawable.close_btn_icon_dimmed);
//
//                            over_time = true;
//
//                        }, 2000);
//                    }else {
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

//                    }


                }
                params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
                params.x = 0;
                params.y = 0;

                mWindowManager.updateViewLayout(mFloatingView, params);

                break;

            case R.id.exp_iv:
                //closing the widget

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
}