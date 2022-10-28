package com.fynuls.controllers.greensales;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public interface Codes {
    public static final DecimalFormat twoDigit = new DecimalFormat("00");
    public static final DecimalFormat df = new DecimalFormat("##,###,###.00");
    public static final DecimalFormat nonDecimal = new DecimalFormat("##,###,###");
    public static final String ALL_OK = "200";
    public static final String NOT_FOUND = "404";
    public static final String SOMETHING_WENT_WRONG = "502";
    public static final String ALREADY_LOGGED_IN = "504";
    public static final String FORCED_LOGOUT = "444";
    public static final String INVALID_TOKEN = "300";
    public static final String INVALID_CREDENTIALS = "404";
    public static final int FYNALS_APP_CODE = 2;
    public static final String CURRENT_MONTH_NUMBER = twoDigit.format(Calendar.getInstance().get(Calendar.MONTH)+1);
    public static final int CALENDER_CURRENT_MONTH_NUMBER = Calendar.getInstance().get(Calendar.MONTH)+1;
    public static final int CURRENT_YEAR_NUMBER = Calendar.getInstance().get(Calendar.YEAR);
    public static final int FIRST_FISCAL_MONTH  = Calendar.JULY+1;
    public static final String E_QTY  = "E_QTY";
    public static final String TP_SALE_VALUE  = "TP_SALE_VALUE";
    public static final String NET_SALE_VALUE  = "NET_SALE_VALUE";
    public static final String DISCOUNTS  = "DISCOUNTS";
    public static final int BARCHART = 1;
    public static final int LINECHART = 2;

    public static NumberFormat nf = NumberFormat.getInstance();
    public static final String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static String ALREADY_EXISTS = "409";
}