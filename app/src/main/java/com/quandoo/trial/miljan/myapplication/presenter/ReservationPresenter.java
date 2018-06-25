package com.quandoo.trial.miljan.myapplication.presenter;

import com.quandoo.trial.miljan.myapplication.view.ReservationView;

public interface ReservationPresenter {

    void init();

    void setView(ReservationView view);

    /**
     * Logic to reserve a table
     *
     * @param table number of table received from UI
     * @param customer number of customer received from UI
     */
    void reserveTable(int table, int customer);

    /**
     * Search for customer
     *
     * @param text text to search for
     */
    void filterCustomers(String text);
}
