package com.quandoo.trial.miljan.myapplication.presenter;

import com.quandoo.trial.miljan.myapplication.model.CustomerModel;
import com.quandoo.trial.miljan.myapplication.model.ModelCallbacks;
import com.quandoo.trial.miljan.myapplication.model.ReservationModel;
import com.quandoo.trial.miljan.myapplication.view.ReservationView;

import java.util.List;

public class ReservationPresenterImpl implements ReservationPresenter, ModelCallbacks {

    static ReservationPresenter sPresenter;
    static boolean sIsInitialized = false;

    private ReservationView mView;
    private ReservationModel mModel;

    private ReservationPresenterImpl(ReservationView view, ReservationModel reservationModel) {
        mView = view;
        mModel = reservationModel;
    }

    public static ReservationPresenter getPresenter(ReservationView view, ReservationModel reservationModel) {
        if (sPresenter == null) {
            sPresenter = new ReservationPresenterImpl(view, reservationModel);
        } else {
            sPresenter.setView(view);
        }
        return sPresenter;
    }

    public void init() {
        if (!sIsInitialized) {
            mModel.setCallbacks(this);
            mModel.init();
            sIsInitialized = true;
        } else {
            mView.updateTable(mModel.getTables());
            mView.updateCustomer(mModel.getCustomer());
        }
    }

    public void setView(ReservationView view) {
        mView = view;
    }

    public void reserveTable(int table, int customer) {
        mModel.reserveTable(table, customer);
    }

    public void onShowCustomers(List<CustomerModel> customerList) {
        mView.updateCustomer(customerList);
    }

    public void onShowReservedTable(boolean[] tablesArray) {
        mView.updateTable(tablesArray);
    }
}
