package com.quandoo.trial.miljan.myapplication;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
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
import com.quandoo.trial.miljan.myapplication.model.ReservationModelImpl;
import com.quandoo.trial.miljan.myapplication.presenter.ReservationPresenter;
import com.quandoo.trial.miljan.myapplication.presenter.ReservationPresenterImpl;
import com.quandoo.trial.miljan.myapplication.view.ReservationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TablesActivity extends AppCompatActivity
        implements ReservationView {
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private CustomersAdapter mCustomerAdapter;
    private TablesAdapter mTableAdapter;

    private ReservationPresenter mPresenter;

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

        mPresenter = ReservationPresenterImpl.getPresenter(this, new ReservationModelImpl());

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
        mPresenter.init();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mPresenter = null;
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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRvCustomers.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        } else {
            mRvCustomers.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        }

        mCustomerAdapter = new CustomersAdapter();
        mRvCustomers.setAdapter(mCustomerAdapter);
    }

    private void initTablesRecyclerView() {
        mRvTables.setLayoutManager(new GridLayoutManager(this, 7));

        mTableAdapter = new TablesAdapter(mPresenter);
        mRvTables.setAdapter(mTableAdapter);
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

    public void updateCustomer(final List<CustomerModel> customerList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCustomerAdapter.updateList(customerList);
                mCustomerAdapter.notifyDataSetChanged();
            }
        });
    }

    public void updateTable(final boolean[] tablesArray) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTableAdapter.updateArray(tablesArray);
                mTableAdapter.notifyDataSetChanged();
            }
        });
    }
}
