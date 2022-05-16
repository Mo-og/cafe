package ua.cafe.utils;

import lombok.ToString;
import org.springframework.ui.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public abstract class Stats {
    public static final String IMAGES_FOLDER_PATH = ".//src//main//resources//static//DishImages//";
    public static final String IMAGES_THUMBNAILS_PATH = ".//src//main//resources//static//DishImages//thumbnails//";
    public static final String URL_THUMBNAILS_PATH = "/DishImages/thumbnails/";

    public static Date getTodaySince() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getTodayTill() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    private static final SimpleDateFormat dateFormatForUser = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private static final DateFormat dateFormat = new SimpleDateFormat("_-_dd.MM.yyyy_HH.mm.ss");

    public static final SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    public static String formatForUser(Date date) {
        return dateFormatForUser.format(date);
    }

    public static String getDateString() {
        return dateFormat.format(new Date());
    }

    @ToString
    public static class Interval {
        public final Date from;
        public final Date to;

        public Interval(String from, String to) {
            Date to1;
            Date from1;
            if (from == null || to == null) {
                from1 = getTodaySince();
                to1 = getTodayTill();
            } else {
                try {
                    from1 = Stats.dateParser.parse(from);
                    to1 = Stats.dateParser.parse(to);
                } catch (ParseException e) {
                    from1 = getTodaySince();
                    to1 = getTodayTill();
                }
            }
            this.to = to1;
            this.from = from1;
        }

        public String getFromAsHtmlString() {
            return dateParser.format(from);
        }

        public String getToAsHtmlString() {
            return dateParser.format(to);
        }
    }

    public static void markPage(Model model, String pageName) {
        model.addAttribute("page", pageName);
    }
}
