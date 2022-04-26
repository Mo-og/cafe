package ua.cafe.utils;

import org.springframework.ui.Model;

import java.text.DateFormat;
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

    private static final DateFormat dateFormat = new SimpleDateFormat("_-_dd.MM.yyyy_HH.mm.ss");

    public static final SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    public static String getDateString() {
        return dateFormat.format(new Date());
    }

    public static void markPage(Model model, String pageName) {
        model.addAttribute("page", pageName);
    }
}
