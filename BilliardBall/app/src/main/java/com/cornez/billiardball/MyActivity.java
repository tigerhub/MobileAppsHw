package com.cornez.billiardball;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MyActivity extends Activity implements
        GestureDetector.OnGestureListener {

    private GestureDetector aGesture;

    //ANIMATION IS SPLIT INTO TWO THREADS:
    //     BACKGROUND THREAD: BILLIARD MOVEMENT
    //     UI THREAD        : VIEW UPDATE
    private FrameLayout mainLayout;
    private Thread calculateThread;

    private ImageView ballImageView;
    private Ball mBall;

    private SeekBar sb_Acceleration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TASK 1: WINDOW PROPERTIES ARE SET
        //THIS ANDROID WINDOW WILL NOT FEATURE A TITLE
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //TASK 2: SET COORDINATE SYSTEM OFFSET
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int windowWidth = metrics.widthPixels;
        int windowHeight = metrics.heightPixels;

        //TASK 3: SET THE LAYOUT VIEW
        setContentView(R.layout.activity_my);
        mainLayout = (FrameLayout) findViewById(R.id.frameLayout);

        //TASK 4: INSTANTIATE A BILLIARD BALL
        buildBall(windowWidth, windowHeight);

        aGesture = new GestureDetector(this, this);

        //TASK 4: INSTANTIATE THE BACKGROUND THREAD
        calculateThread = new Thread(calculateAction);

        // Set the SeekBar of Acceleration
        sb_Acceleration = (SeekBar) findViewById(R.id.sb_Acceleration);
        // Set the SeekBar-Change-Listener
        sb_Acceleration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // t1.setTextSize(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // do nothing
            }
        });


    }


    private void buildBall(int windowWidth, int windowHeight) {

        //TASK 1: CREATE A LAYOUT INFLATER TO ADD VISUAL VIEWS TO THE LAYOUT
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //TASK 2: SPECIFY BILLIARD BALL ATTRIBUTES
        int initialXPosition = (int) (-200);
        int initialYPosition = (int) (200);

        mBall = new Ball();
        mBall.setX(initialXPosition);
        mBall.setY(initialYPosition);
        mBall.setVelocityX(0);
        mBall.setVelocityY(0);
        mBall.setRadius(120);
        mBall.setCollisionBoundaries(
                -windowWidth / 2, windowWidth / 2, -windowHeight / 2, windowHeight / 2);

        //TASK 3: ADD THE BALL TO THE LAYOUT
        ballImageView = (ImageView) layoutInflater.inflate(R.layout.ball_layout, null);
        ballImageView.setScaleX((float) .3);
        ballImageView.setScaleY((float) .3);
        ballImageView.setX((float) mBall.getX());
        ballImageView.setY((float) mBall.getY());
        mainLayout.addView(ballImageView, 0);
    }


    //******************* RUNNABLES **********************
    private Runnable calculateAction = new Runnable() {
        private static final int DELAY = 50;

        public void run() {
            try {
                while (true) {
                    mBall.move(sb_Acceleration.getProgress());
                    Thread.sleep(DELAY);
                    threadHandler.sendEmptyMessage(0);
                }
            } catch (InterruptedException e) {
            }
        }
    };


    //******************* HANDLER FOR UPDATING THE UI VIEW  **********************
    public Handler threadHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //SET THE BEE AT THE CORRECT XY LOCATION
            ballImageView.setX((float) mBall.getX());
            ballImageView.setY((float) mBall.getY());
        }
    };

    @Override
    protected void onResume() {
        calculateThread.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }


    //***********************TOUCH GESTURES*************************
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return aGesture.onTouchEvent(event);
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        final float ADJUST = 0.025f;
        mBall.setVelocityX((int) velocityX * ADJUST);
        mBall.setVelocityY((int) velocityY * ADJUST);
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
