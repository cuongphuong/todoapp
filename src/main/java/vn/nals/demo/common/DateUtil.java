package vn.nals.demo.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String YYYYMMDDHH24MMSS_FORMAT = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";

    public static boolean isDefaultDate(String strDate) {
        return isDate(strDate, DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
    }

    public static Date strToDate(String strDate) {
        return convertToDate(strDate, DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
    }

    public static boolean isDate(String strDate, String formatPattern) {
        SimpleDateFormat df;
        Date testDate;

        try {
            df = new SimpleDateFormat(formatPattern);
            testDate = df.parse(strDate);
        } catch (Exception e) {
            return false;
        }

        if (!df.format(testDate).equals(strDate)) {
            return false;
        }
        return true;
    }

    public static Date getDate(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month - 1, date);
        return cal.getTime();
    }

    public static Date getMoveDay(int move, Date date) {
        long time = date.getTime();
        time += (long) ((long) move * 1000 * 60 * 60 * 24);
        Date rc = new Date(time);
        return rc;
    }

    public static String toYYYYMMDDHH24MISS(Date date) {
        return new SimpleDateFormat(YYYYMMDDHH24MMSS_FORMAT).format(date);
    }

    public static Date convertToDate(String dateStr, String pattern) {
        Date date = null;
        if (dateStr == null || pattern == null) {
            return (date);
        }

        if (dateStr.length() < pattern.length()) {
            return (null);
        }

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setLenient(false);
        try {
            date = format.parse(dateStr);
        } catch (Exception ex) {
        }

        return date;
    }

}
