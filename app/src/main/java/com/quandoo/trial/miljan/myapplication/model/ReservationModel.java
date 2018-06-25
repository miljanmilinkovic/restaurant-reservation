package com.quandoo.trial.miljan.myapplication.model;

import java.util.List;

public interface ReservationModel {

    void setCallbacks(ModelCallbacks callbacks);

    void init();

    /**
     * Update model to reflect new table reservation
     *
     * @param table table to reserve
     * @param customer reservation for customer
     * @return true if reservation successful
     */
    boolean reserveTable(int table, int customer);

    /**
     * Retrieve Customer data
     *
     * @return list of customer objects
     */
    List<CustomerModel> getCustomer();

    /**
     * Retrieve table reservations
     *
     * @return array of table reservations
     */
    boolean[] getTables();
}
