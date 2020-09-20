package tapsel.test.rewardsimulator.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    public static int getMonth(Date date) {
        int month;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        month = gregorianCalendar.get(Calendar.MONTH);
        month = month + 1;
        return month;
    }

    public static int getYear(Date date) {
        int year;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        year = gregorianCalendar.get(Calendar.YEAR);
        return year;
    }

    public static Date increaseDate(Date date, int num, DateType dateType) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (dateType) {
            case MILLI_SECOND:
                cal.add(Calendar.MILLISECOND, num);
                break;
            case SECOND:
                cal.add(Calendar.SECOND, num);
                break;
            case MINUTE:
                cal.add(Calendar.MINUTE, num);
                break;
            case HOUR:
                cal.add(Calendar.HOUR, num);
                break;
            case DAY:
                cal.add(Calendar.DAY_OF_MONTH, num);
                break;
            case MONTH:
                cal.add(Calendar.MONTH, num);
                break;
            case YEAR:
                cal.add(Calendar.YEAR, num);
                break;
        }
        return cal.getTime();
    }

    public enum DateType {
        MILLI_SECOND, SECOND, MINUTE, HOUR, DAY, MONTH, YEAR
    }
}
