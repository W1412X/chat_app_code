package com.example.chat;

import android.content.Context;

import java.util.Calendar;

public class BusTime {
    private int hour;
    private int minute;
    private int year;
    private int month;
    private int day;
    private int weekday;
    BusTime(){
        Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        weekday=calendar.get(Calendar.DAY_OF_WEEK);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        hour=calendar.get(Calendar.HOUR);
        minute=calendar.get(Calendar.MINUTE);
    }
    BusTime(int h,int m){
        hour=h;
        minute=m;
    }
}