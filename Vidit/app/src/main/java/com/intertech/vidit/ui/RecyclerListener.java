package com.intertech.vidit.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.intertech.vidit.R;


/**
 * Copyright (C) Intertech, Inc. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Jim White <jwhite@intertech.com>, February 2015
 * <p/>
 * Per http://stackoverflow.com/questions/24471109/recyclerview-onclick/26196831#26196831
 * <p/>
 * Created by jwhite on 12/24/2014.
 */
public class RecyclerListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mListener;

    GestureDetector mGestureDetector;

    public RecyclerListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        // get card view
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null) {
            // get Relative layout inside card view
            View layoutView = ((ViewGroup) childView).getChildAt(0);
            // get check box inside of relative layout
            View chb = layoutView.findViewById(R.id.favoriteItemCB);
            if (!isCheckBoxHit(chb, e) && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildPosition(childView));
            }
        }
        return false;
    }

    private boolean isCheckBoxHit(View view, MotionEvent motionEvent) {
        if (view == null) {
            return false;
        }
        Rect rect = new Rect();
        int[] listViewCoords = new int[2];
        view.getLocationOnScreen(listViewCoords);
        int x = (int) motionEvent.getRawX() - listViewCoords[0];
        int y = (int) motionEvent.getRawY() - listViewCoords[1];
        view.getHitRect(rect);
        return rect.contains(x, y);
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }
}