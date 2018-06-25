package com.quandoo.trial.miljan.myapplication.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

public class ReservationModelImpl implements ReservationModel {

    static final String URL_TABLE = "https://s3-eu-west-1.amazonaws.com/quandoo-assessment/table-map.json";
    static final String URL_CUSTOMERS = "https://s3-eu-west-1.amazonaws.com/quandoo-assessment/customer-list.json";

    static final long REFRESH_INTERVAL = 900000;//15min

    /* Customers JSON fields */
    static final String FIELD_ID = "id";
    static final String FIELD_FIRST_NAME = "customerFirstName";
    static final String FIELD_LAST_NAME = "customerLastName";

    class RetrieveData extends TimerTask {
        @Override
        public void run() {
            try {
                mCustomersLst = parseCustomers(getJSON(URL_CUSTOMERS));
                mCallbacks.onShowCustomers(mCustomersLst);

                mTablesArray = parseTables(getJSON(URL_TABLE));
                mCallbacks.onShowReservedTable(mTablesArray);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (JSONException e3) {
                e3.printStackTrace();
            }
        }
    }

    private ModelCallbacks mCallbacks;
    private boolean[] mTablesArray;
    private List<CustomerModel> mCustomersLst;

    public void setCallbacks(ModelCallbacks callBacks) {
        mCallbacks = callBacks;
    }

    public void init() {
        mCustomersLst = new ArrayList<>();
        mCustomersLst.add(new CustomerModel(1, "loading...", "loading..."));
        mCallbacks.onShowCustomers(mCustomersLst);

        Timer t = new Timer();
        t.schedule(new RetrieveData(), 0, REFRESH_INTERVAL);
    }

    public boolean reserveTable(int table, int customer) {
        mTablesArray[table] = true;
        mCallbacks.onShowReservedTable(mTablesArray);

        mCustomersLst.remove(customer);
        mCallbacks.onShowCustomers(mCustomersLst);
        return true;
    }

    public List<CustomerModel> getCustomer() {
        return mCustomersLst;
    }

    public boolean[] getTables() {
        return mTablesArray;
    }

    private String getJSON(String url) throws IOException {
        URL requestUrl = new URL(url);
        HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(60 * 1000);
        urlConnection.setConnectTimeout(60 * 1000);
        urlConnection.setDoInput(true);
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("X-Environment", "android");

        urlConnection.connect();

        final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), Charset.forName("US-ASCII")));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            total.append(line);
        }
        if (reader != null) {
            reader.close();
        }
        urlConnection.disconnect();

        return total.toString();
    }

    private List<CustomerModel> parseCustomers(String jsonArr) throws JSONException {
        final List<CustomerModel> customerList = new ArrayList<>();
        final JSONArray jsonArray = new JSONArray(jsonArr);
        JSONObject jsonCustomer;
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonCustomer = jsonArray.getJSONObject(i);
            customerList.add(new CustomerModel(
                    jsonCustomer.getInt(FIELD_ID),
                    jsonCustomer.getString(FIELD_FIRST_NAME),
                    jsonCustomer.getString(FIELD_LAST_NAME)
            ));
        }

        return customerList;
    }

    private boolean[] parseTables(String jsonArr) throws JSONException {
        final JSONArray jsonArray = new JSONArray(jsonArr);
        boolean[] tablesArray = new boolean[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            tablesArray[i] = jsonArray.getBoolean(i);
        }

        return tablesArray;
    }
}