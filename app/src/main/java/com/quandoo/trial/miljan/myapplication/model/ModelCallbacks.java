package com.quandoo.trial.miljan.myapplication.model;

import java.util.List;

public interface ModelCallbacks {

    /**
     * Method that allows ReservationModel to push customer data on ReservationPresenter.
     *
     * @param customerList new list of customers
     */
    void onShowCustomers(List<CustomerModel> customerList);

    /**
     * Method that allows ReservationModel to push tables reservations data on ReservationPresenter.
     *
     * @param tablesArray new tables reservations array
     */
    void onShowReservedTable(boolean[] tablesArray);
}
