package org.celllife.appointmentreminders.framework.util;

import org.celllife.appointmentreminders.domain.exception.InvalidDateException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static Date getDateFromString(String date) throws InvalidDateException {

        try {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            throw new InvalidDateException("The date " + date + " does not comply with the format dd/MM/yyy");
        }

    }

    public static Date getTimeFromString(String time) throws InvalidDateException {

        try {
            return new SimpleDateFormat("HH:mm", Locale.ENGLISH).parse(time);
        } catch (ParseException e) {
            throw new InvalidDateException("The time " + time + " does not comply with the format HH:mm");
        }

    }

    public static String DateToString(Date date) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);

    }

    public static String TimeToString(Date date) {

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);

    }

    public static boolean isToday(Date date) {

        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);

        Calendar todayCalendar = Calendar.getInstance();
        if (todayCalendar.get(Calendar.YEAR) != dateCalendar.get(Calendar.YEAR))
            return false;
        if (todayCalendar.get(Calendar.MONTH) != dateCalendar.get(Calendar.MONTH))
            return false;
        if (todayCalendar.get(Calendar.DATE) != dateCalendar.get(Calendar.DATE))
            return false;

        return true;

    }


}
