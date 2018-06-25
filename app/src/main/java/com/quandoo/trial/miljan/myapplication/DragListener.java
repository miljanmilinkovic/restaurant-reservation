package com.quandoo.trial.miljan.myapplication;

import android.view.DragEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class DragListener implements View.OnDragListener {

    private DropListener mListener;

    public DragListener(DropListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {

            case DragEvent.ACTION_DRAG_ENTERED:
                ScaleAnimation scaleAnim = new ScaleAnimation(1f, 1.15f, 1f, 1.15f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnim.setDuration(300);
                scaleAnim.setRepeatCount(1);
                scaleAnim.setRepeatMode(Animation.REVERSE);
                v.startAnimation(scaleAnim);
                break;

            case DragEvent.ACTION_DROP:
                int table = (int) v.getTag();
                int customer = (int) event.getLocalState();
                mListener.dropClientAtTable(table, customer);
                break;
        }

        return true;
    }
}