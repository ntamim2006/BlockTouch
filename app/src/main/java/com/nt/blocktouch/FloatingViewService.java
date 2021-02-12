package com.nt.blocktouch;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class FloatingViewService extends Service implements View.OnClickListener {


    private WindowManager mWindowManager;
    private View mFloatingView;
    private View collapsedView;
    private View expandedView;
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

        mFloatingView.findViewById(R.id.expClose).setOnLongClickListener(v -> {
            //Initially view will be added to top-left corner
            if(ifLock){
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        LAYOUT_FLAG,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
                         mFloatingView.findViewById(R.id.exp_iv).setVisibility(View.VISIBLE);
                        imageLock.setImageResource(R.drawable.lock_sign_open);

            }else{
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        LAYOUT_FLAG,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
                imageLock.setImageResource(R.drawable.lock_sign_close);
                mFloatingView.findViewById(R.id.exp_iv).setVisibility(View.GONE);

            }
            params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
            params.x = 0;
            params.y = 0;
            ifLock=!ifLock;
            mWindowManager.updateViewLayout(mFloatingView, params);

            return true;
        });


//        //adding an touchlistener to make drag movement of the floating widget
//        mFloatingView.findViewById(R.id.relativeLayoutParent).setOnTouchListener(new View.OnTouchListener() {
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
//
//
//
//
//
//                }
//                return false;
//            }
//        });




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
                //switching views
                if(ifLock){
                    Toast.makeText(this, "לפתיחת נעילת המגע לחץ לחיצה ארוכה", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "לנעילת המגע לחץ לחיצה ארוכה", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.exp_iv:
                //closing the widget

                stopSelf();

                break;

        }
    }
}