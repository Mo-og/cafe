package ua.cafe.utils;

import net.coobird.thumbnailator.Thumbnails;
import ua.cafe.controllers.DishController;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class ImageProcessor {

    public static void saveThumbnail(InputStream originalImage, String fileName) throws Exception {
        System.out.println("Saving thumbnail for " + fileName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(originalImage)
                .size(25, 25)
                .outputFormat("JPEG")
                .outputQuality(1)
                .toFile(new File(DishController.IMAGES_THUMBNAILS_PATH + fileName));
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        return Thumbnails.of(originalImage)
                .size(targetWidth, targetHeight)
                .outputFormat("JPEG")
                .outputQuality(1)
                .asBufferedImage();
    }
}
