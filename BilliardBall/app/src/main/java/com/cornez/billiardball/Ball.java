package com.cornez.billiardball;


public class Ball {
    private int mX;
    private int mY;
    private int mRadius;
    private double mVelocityX;
    private double mVelocityY;
    private int left, right, top, bottom;

    private int REVERSE = -1;
    private double FRICTION = .93;
    private double ACCELERATION1 = 1.02;
    private double ACCELERATION2 = 1.06;


    public void setVelocityX(double velocity){
        mVelocityX = velocity;
    }
    public double getVelocityX(){
        return mVelocityX;
    }

    public void setVelocityY(double velocity){
        mVelocityY = velocity;
    }
    public double getVelocityY(){
        return mVelocityY;
    }

    public void setRadius(int radius){
        mRadius = radius;
    }
    public int getRadius(){
        return mRadius;
    }

    public void setX(int x){
        mX = x;
    }
    public int getX(){
        return mX;
    }
    public void setY(int y){
        mY = y;
    }
    public int getY(){
        return mY;
    }

    public void move(int curAcceleration){
        //MOVE BALL
        mX +=  mVelocityX;
        mY +=  mVelocityY;

        if (curAcceleration == 0) {
            //SLOW THE BALL DOWN BY APPLYING FRICTION
            mVelocityX *= FRICTION;
            mVelocityY *= FRICTION;
        } else if (curAcceleration == 1) {
            mVelocityX *= ACCELERATION1;
            mVelocityY *= ACCELERATION1;
        } else if(curAcceleration == 2) {
            //SLOW THE BALL DOWN BY APPLYING FRICTION
            mVelocityX *= ACCELERATION2;
            mVelocityY *= ACCELERATION2;
        }

        if (Math.abs(mVelocityX) < 1){
            mVelocityX = 0;
        }
        if (Math.abs(mVelocityY) < 1){
            mVelocityY = 0;
        }

        //CHECK FOR COLLISIONS
        checkCollision();
    }

    public void setCollisionBoundaries(int l, int r, int t, int b){
        left = l + mRadius;
        right = r - mRadius;
        top = t + mRadius;
        bottom = b - mRadius;
    }

    private void checkCollision() {
        // Collision on the Left or Right side
        if (mX < left){
            mX = left;
            mVelocityX *= REVERSE;
        }
        else if (mX > right){
            mX = right;
            mVelocityX *= REVERSE;
        }

        // Collision on the Top or Bottom side
        if (mY < top){
            mY = top;
            mVelocityY *= REVERSE;
        }
        else if (mY > bottom){
            mY = bottom;
            mVelocityY *= REVERSE;
        }
    }
}
