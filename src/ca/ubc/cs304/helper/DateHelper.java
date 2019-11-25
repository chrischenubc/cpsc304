package ca.ubc.cs304.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    private static final String dateFromat = "yyyy-MM-dd HH:mm";

    public static boolean isThisDateValid(String dateToValidate) throws ParseException{

        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {
            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {
            throw e;
        }

        return true;
    }

    public static long diffInDays(Date d1, Date d2) {
        long diff = d1.getTime() - d2.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    public static long diffInHours(Date d1, Date d2) {
        long diff = d1.getTime() - d2.getTime();
        return diff / (1000 * 60 * 60);
    }

    public static long diffInWeeks(Date d1, Date d2) {
        long diff = d1.getTime() - d2.getTime();
        return diff / (1000 * 60 * 60 * 24 * 7);
    }

    public static Date parse(String dateToValidate) throws ParseException{

        if(dateToValidate == null){
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);
        Date date;
        try {
            //if not valid, it will throw ParseException
             date = sdf.parse(dateToValidate);
             return date;
        } catch (ParseException e) {
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println(isThisDateValid("2007-12-18 17:59"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
