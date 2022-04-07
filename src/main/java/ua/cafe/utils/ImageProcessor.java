package ua.cafe.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.InputStream;

@Slf4j
public abstract class ImageProcessor {

    public static void saveThumbnail(InputStream originalImage, String fileName) throws Exception {
        log.info("Saving thumbnail for " + fileName);
        Thumbnails.of(originalImage)
                .size(25, 25)
                .outputFormat("jpg")
                .outputQuality(1)
                .toFile(new File(Stats.IMAGES_THUMBNAILS_PATH + fileName));
    }

    /*public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws Exception {
        return Thumbnails.of(originalImage)
                .size(targetWidth, targetHeight)
                .outputFormat("jpeg")
                .outputQuality(1)
                .asBufferedImage();
    }*/
}
