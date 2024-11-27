package com.digiview.workwell.data.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    /**
     * Formats a date to the format "yyyy MM dd hh mm aa".
     *
     * @param date The date to format.
     * @return The formatted date as a string.
     */
    public static String formatDate(Date date) {
        if (date == null) return "";
        // Correct format: yyyy MM dd hh mm aa
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.getDefault());
        return formatter.format(date);
    }
}
