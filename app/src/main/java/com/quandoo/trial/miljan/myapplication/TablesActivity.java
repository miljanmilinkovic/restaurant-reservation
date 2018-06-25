package com.quandoo.trial.miljan.myapplication;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.quandoo.trial.miljan.myapplication.model.CustomerModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TablesActivity extends AppCompatActivity {
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private boolean mDragHandled = false;

    @BindView(R.id.fullscreen_content)
    View mContentView;
    @BindView(R.id.rvCustomers)
    RecyclerView mRvCustomers;
    @BindView(R.id.rvTables)
    RecyclerView mRvTables;
    @BindView(R.id.reservation_instruction)
    View mReservationInstruction;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tables);
        ButterKnife.bind(this);

        mVisible = true;

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        initCustomersRecyclerView();
        initTablesRecyclerView();
        initReservationInstruction();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void initCustomersRecyclerView() {
        mRvCustomers.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));

        List<CustomerModel> customerList = new ArrayList<>();
        customerList.add(new CustomerModel(0, "Marilyn", "Monroe"));
        customerList.add(new CustomerModel(1, "Abraham", "Lincoln"));
        customerList.add(new CustomerModel(2, "Mother", "Teresa"));
        customerList.add(new CustomerModel(3, "John F.", "Kennedy"));
        customerList.add(new CustomerModel(4, "Martin Luther", "King"));

        CustomersAdapter customerAdapter = new CustomersAdapter(customerList);
        mRvCustomers.setAdapter(customerAdapter);
    }

    private void initTablesRecyclerView() {
        mRvTables.setLayoutManager(new GridLayoutManager(this, 4));

        boolean[] array = new boolean[20];
        array[0] = true;
        array[1] = false;
        array[3] = true;
        array[7] = true;

        TablesAdapter bottomListAdapter = new TablesAdapter(array);
        mRvTables.setAdapter(bottomListAdapter);
    }

    private void initReservationInstruction() {
        Animation alphaAnim = new AlphaAnimation(1.0f, 0.7f);
        alphaAnim.setDuration(1000);
        alphaAnim.setRepeatMode(Animation.REVERSE);
        alphaAnim.setRepeatCount(Animation.INFINITE);
        mReservationInstruction.startAnimation(alphaAnim);
        mReservationInstruction.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                mReservationInstruction.setAlpha(0f);
                return false;
            }
        });
    }
}
