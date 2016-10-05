package com.example.da.pullalltogether;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.content.Intent;


public class SecondActivity extends AppCompatActivity
    implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{

    private ImageView imageView_Cat;

    // Announce the Gesture Detector
    private GestureDetector myGestureDetector;

    // Accelerometer related variables
    private float lastX, lastY, lastZ;  //old coordinate positions from accelerometer, needed to calculate delta.
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    // value used to determine whether user shook the device "significantly"
    private static int SIGNIFICANT_SHAKE = 500;   //tweak this as necessary
    private static int X_MOVEMENT_THRESHOLD = 10;  //tweak this as necessary
    private static int Y_MOVEMENT_THRESHOLD = 10;  //tweak this as necessary

    // register xml animation file
    Animation anim_ShakeOnce;
    Animation anim_MoveRight;
    Animation anim_MoveLeft;
    Animation anim_MoveUp;
    Animation anim_MoveDown;

    Animation anim_Rotate5Times;
    Animation anim_Rotate10Times;
    Animation anim_Rotate10TimesCounter;
    Animation anim_Rotate5TimesCounter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        imageView_Cat = (ImageView)findViewById(R.id.iv_SleepingCat);

        // Bind Animation XML files to Animation Variables
        anim_ShakeOnce = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.shake_once);
        anim_MoveLeft = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.move_left);
        anim_MoveRight = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.move_right);
        anim_MoveUp = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.move_up);
        anim_MoveDown = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.move_down);

        // Instantiate the Gesture Detector
        myGestureDetector = new GestureDetector(this, this);


        // Initialize acceleration values
        acceleration = 0.00f;                                  // Initializing Acceleration data
        currentAcceleration = SensorManager.GRAVITY_EARTH;  // We live on Earth
        lastAcceleration = SensorManager.GRAVITY_EARTH;     // Ctrl-Click to see where else we could use our phone
    }


    // My Sensor Event Listener
    private final SensorEventListener mySensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // get x, y, and z values for the SensorEvent
            //each time the Event fires, we have access to three dimensions.
            //compares these values to previous values to determine how "fast" the device was shaken.
            //Ref: http://developer.android.com/reference/android/hardware/SensorEvent.html

            float x = event.values[0];   //always do this first
            float y = event.values[1];
            float z = event.values[2];

            // save previous acceleration value
            lastAcceleration = currentAcceleration;

            // Calculate the current acceleration
            // This is a simplified calculation, to be real we would need time and a square root
            currentAcceleration = (x-lastX)*(x-lastX) + (y-lastY)*(y-lastY) + (z-lastZ)*(z-lastZ);

            // calculate the change in acceleration
            // Also simplified, but good enough to determine random shaking.
            acceleration = currentAcceleration *  (currentAcceleration - lastAcceleration);

            // if the acceleration is above a certain threshold
            if (currentAcceleration >= SIGNIFICANT_SHAKE){
                imageView_Cat.startAnimation(anim_ShakeOnce);
            }
            else if (currentAcceleration < (SIGNIFICANT_SHAKE)) {

                if ((x - lastX) > X_MOVEMENT_THRESHOLD)
                {
                    imageView_Cat.startAnimation(anim_MoveRight);
                }
                else if ((lastX - x) > X_MOVEMENT_THRESHOLD)
                {
                    imageView_Cat.startAnimation(anim_MoveLeft);
                }

                if ((y - lastY) > Y_MOVEMENT_THRESHOLD)
                {
                    imageView_Cat.startAnimation(anim_MoveUp);
                }
                else if ((lastY - y) > Y_MOVEMENT_THRESHOLD)
                {
                    imageView_Cat.startAnimation(anim_MoveDown);
                }
            }



            lastX = x;
            lastY = y;
            lastZ = z;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        enableAccelerometerListening();
    }
    @Override
    protected void onStop() {
        disableAccelerometerListening();
        super.onStop();
    }
    // enable listening for accelerometer events
    private void enableAccelerometerListening() {
        // The Activity has a SensorManager Reference.
        // This is how we get the reference to the device's SensorManager.
        SensorManager sensorManager =
                (SensorManager) this.getSystemService(
                        Context.SENSOR_SERVICE);    //The last parm specifies the type of Sensor we want to monitor
        //Now that we have a Sensor Handle, let's start "listening" for movement (accelerometer).
        //3 parms, The Listener, Sensor Type (accelerometer), and Sampling Frequency.
        sensorManager.registerListener(mySensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);   //don't set this too high, otw you will kill user's battery.
    }
    // disable listening for accelerometer events
    private void disableAccelerometerListening() {
        //Disabling Sensor Event Listener is two step process.
        //1. Retrieve SensorManager Reference from the activity.
        //2. call unregisterListener to stop listening for sensor events
        //THis will prevent interruptions of other Apps and save battery.

        // get the SensorManager
        SensorManager sensorManager =
                (SensorManager) this.getSystemService(
                        Context.SENSOR_SERVICE);
        // stop listening for accelerometer events
        sensorManager.unregisterListener(mySensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));

        anim_Rotate5Times = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.rotate_5_clock);

        anim_Rotate5TimesCounter = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.rotate_5_counter);

        anim_Rotate10Times = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.rotate_10);

        anim_Rotate10TimesCounter = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.rotate_10_counter);

    }


    // ==================== onTouchEvent ====================
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.myGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    // ==================== Functions of OnGestureListener ====================

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        final float minFlingDistance = 30;
        final float velocity_threshold = 19;        // can be set to anything!
        //counter-clockwise for left flings
        if (e1.getX() - e2.getX() > minFlingDistance) {
            if (velocityX < velocity_threshold) {
                imageView_Cat.startAnimation(anim_Rotate5TimesCounter);
            }
            else{
                imageView_Cat.startAnimation(anim_Rotate10TimesCounter);
            }

            return true;
        }
        //clockwise for right flings
        else if (e2.getX() - e1.getX() > minFlingDistance){
            if ((-1*velocityX) < velocity_threshold) {
                imageView_Cat.startAnimation(anim_Rotate5Times);
            }
            else{
                imageView_Cat.startAnimation(anim_Rotate10Times);
            }
            return true;
        }

        return false;
    }


    // ==================== Functions of OnDoubleTapListener ====================

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

}
