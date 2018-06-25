package com.quandoo.trial.miljan.myapplication.view;

import com.quandoo.trial.miljan.myapplication.model.CustomerModel;

import java.util.List;

public interface ReservationView {

    void updateCustomer(List<CustomerModel> customerList);

    void updateTable(boolean[] tablesArray);
}
