package com.me.remenber.services;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class MyScaleGestures implements View.OnTouchListener, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {
    private View view;
    private ScaleGestureDetector gestureScale;
    private float scaleFactor = 1;
    private boolean inScale = false;

    public MyScaleGestures (Context c){
        gestureScale = new ScaleGestureDetector(c, this);
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        this.view = view;
        gestureScale.onTouchEvent(event);
        return true;
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();
        scaleFactor = (scaleFactor < 1 ? 1 : scaleFactor); // prevent our view from becoming too small //
        scaleFactor = ((float)((int)(scaleFactor * 100))) / 100; // Change precision to help with jitter when user just rests their fingers //
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        inScale = true;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) { inScale = false; }


    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float x, float y) {
        float newX = view.getX();
        float newY = view.getY();
        if(!inScale){
            newX -= x;
            newY -= y;
        }
        WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);

        if (newX > (view.getWidth() * scaleFactor - p.x) / 2){
            newX = (view.getWidth() * scaleFactor - p.x) / 2;
        } else if (newX < -((view.getWidth() * scaleFactor - p.x) / 2)){
            newX = -((view.getWidth() * scaleFactor - p.x) / 2);
        }

        if (newY > (view.getHeight() * scaleFactor - p.y) / 2){
            newY = (view.getHeight() * scaleFactor - p.y) / 2;
        } else if (newY < -((view.getHeight() * scaleFactor - p.y) / 2)){
            newY = -((view.getHeight() * scaleFactor - p.y) / 2);
        }

        view.setX(newX);
        view.setY(newY);

        return true;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }





}