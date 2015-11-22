package com.book.kitchen.kitchenbook.interfaces;

import java.util.Date;

/**
 * Created by User on 19/11/2015.
 */
public interface FindInterface {

    public void filter(String constraint);

    public void initFilter();

    public void dateFilter(int year, int monthOfYear, int dayOfMonth, Date date);

}
