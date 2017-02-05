package by.verus.debts.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);

    public static String getStrFromDate(Date date) {
        return sdf.format(date);
    }

    public static Date getDateFromStr(String dateStr) {
        Date date = null;

        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

}
