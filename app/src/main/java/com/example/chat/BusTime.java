package com.example.chat;

import static java.util.Locale.*;

import android.content.Context;
import android.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BusTime {
    public Integer hour;
    public Integer minute;
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
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        minute=calendar.get(Calendar.MINUTE);
    }
    boolean if_holiday(){
        if(weekday==1||weekday==7){
            return true;
        }
        return false;
    }
    public boolean isDateAfterJanuary11() {
        Calendar january11 = Calendar.getInstance();
        january11.set(year, Calendar.JANUARY, 11);

        Calendar currentDate = Calendar.getInstance();
        currentDate.set(year, month, day);

        return currentDate.after(january11) || currentDate.equals(january11);
    }

    public boolean isTimeBefore(String timeStr) {
        if(timeStr.contains("不")){
            return true;
        }
        String[] timeParts = timeStr.split(":");
        int timeHour = Integer.parseInt(timeParts[0]);
        int timeMinute = Integer.parseInt(timeParts[1]);

        if (timeHour < this.hour) {
            return true;
        } else if (timeHour == this.hour && timeMinute < this.minute) {
            return true;
        } else {
            return false;
        }
    }
    BusTime(int h,int m){
        hour=h;
        minute=m;
    }
}