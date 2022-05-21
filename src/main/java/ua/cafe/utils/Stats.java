package ua.cafe.utils;

import org.springframework.ui.Model;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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
    private static final SimpleDateFormat dateFormatForPath = new SimpleDateFormat("dd.MM.yyyy_HH.mm");

    private static final DateFormat dateFormat = new SimpleDateFormat("_-_dd.MM.yyyy_HH.mm.ss");

    public static final SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("ua", "UA"));

    public static String formatNumber(double money) {
        return numberFormat.format(money);
    }

    public static String formatForUser(Date date) {
        return dateFormatForUser.format(date);
    }

    public static String getDateString() {
        return dateFormat.format(new Date());
    }

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
                    if (from1.getTime() > to1.getTime()) {
                        Date temp = from1;
                        from1 = to1;
                        to1 = temp;
                    }
                } catch (ParseException e) {
                    from1 = getTodaySince();
                    to1 = getTodayTill();
                }
            }
            this.to = to1;
            this.from = from1;
        }

        public Interval() {
            from = getTodaySince();
            to = getTodayTill();
        }

        public String getFromAsHtmlString() {
            return dateParser.format(from);
        }

        public String getToAsHtmlString() {
            return dateParser.format(to);
        }

        public String formatFromForUser() {
            return dateFormatForUser.format(from);
        }

        public String formatForPath() {
            return dateFormatForPath.format(from) + "-" + dateFormatForPath.format(to);
        }

        public String formatToForUser() {
            return dateFormatForUser.format(to);
        }

        @Override
        public String toString() {
            return formatFromForUser() + " - " + formatToForUser();
        }
    }

    public static void markPage(Model model, String pageName) {
        model.addAttribute("page", pageName);
    }
}
