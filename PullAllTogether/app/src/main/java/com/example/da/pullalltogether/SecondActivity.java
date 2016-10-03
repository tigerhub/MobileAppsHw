package com.example.da.pullalltogether;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.content.Intent;


public class SecondActivity extends AppCompatActivity
    implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{

    private ImageView imageView_Cat;

    // Announce the Gesture Detector
    private GestureDetector myGestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        imageView_Cat = (ImageView)findViewById(R.id.iv_SleepingCat);

        // Instantiate the Gesture Detector
        myGestureDetector = new GestureDetector(this, this);
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

        if (e1.getX() - e2.getX() > minFlingDistance) {
            Intent intent_GoToSecondActivity =
                    new Intent(getApplicationContext(), FirstActivity.class);
            startActivity(intent_GoToSecondActivity);

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
