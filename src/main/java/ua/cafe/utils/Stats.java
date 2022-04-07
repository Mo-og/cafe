package ua.cafe.utils;

import org.springframework.ui.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Stats {
    public static final String IMAGES_FOLDER_PATH = ".//src//main//resources//static//DishImages//";
    public static final String IMAGES_THUMBNAILS_PATH = ".//src//main//resources//static//DishImages//thumbnails//";
    public static final String URL_THUMBNAILS_PATH = "/DishImages/thumbnails/";

    private static final DateFormat dateFormat = new SimpleDateFormat("_-_dd.MM.yyyy_HH.mm.ss");

    public static String getDateString() {
        return dateFormat.format(new Date());
    }

    public static void markPage(Model model, String pageName) {
        model.addAttribute("page", pageName);
    }
}
