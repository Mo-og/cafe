package ua.cafe.services;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.cafe.models.Dish;
import ua.cafe.repositories.DishRepository;
import ua.cafe.utils.ImageProcessor;
import ua.cafe.utils.Stats;
import ua.cafe.utils.Translit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DishService {

    private DishRepository repository;

    @Autowired
    public void setRepository(DishRepository repository) {
        this.repository = repository;
    }

    public void saveDish(Dish dish) {
        repository.save(dish);
    }

    public List<Dish> getAllDishes() {
        List<Dish> list = repository.findAll();
        list.sort(Dish::compareTo);
        return list;
    }

    public void removeById(long id) {
        repository.deleteById(id);
    }

    public boolean isNoneWithId(long id) {
        return !repository.existsById(id);
    }

    public Dish getById(long id) {
        return repository.findById(id).orElse(null);
    }

    public void resolveImage(MultipartFile file, @NotNull Dish dish) {
        dish.setName(dish.getName().replace("\"", "'"));
        Path path = null;
        try {
            if (file.isEmpty()) throw new NullPointerException();
            int index = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf('.');
            if (index == -1) index = 0;
            //generating file name
            String pathString = Translit.cyr2lat(dish.getName()) + Stats.getDateString() + Objects.requireNonNull(file.getOriginalFilename()).substring(index);
            path = Paths.get(Stats.IMAGES_FOLDER_PATH + pathString);
            byte[] bytes = file.getBytes();
            Files.write(path, bytes);
            ImageProcessor.saveThumbnail(file.getInputStream(), pathString);
            log.info("Saved thumbnail successfully");
        } catch (IOException | NullPointerException e) {
            log.info("*-Dish image update wasn't successful due to invalid image file.");
            log.info(e.getCause() + ": " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Dish old = getById(dish.getId());
        if (path != null && old != null && old.getClearImagePath() != null && old.getClearImagePath().length() > 11) {
            try {
                log.info("Trying to delete old image and thumbnail");
                Files.delete(Paths.get(Stats.IMAGES_FOLDER_PATH, old.getClearImagePath().substring(11)));
                Files.delete(Paths.get(Stats.IMAGES_THUMBNAILS_PATH, old.getClearImagePath().substring(11)));
                log.info("Removed old dish images: " + old.getClearImagePath().substring(11));
            } catch (IOException | InvalidPathException e) {
                log.info("Unable to delete old DishImage: " + e.getMessage());
            }
        }
        if (path != null)
            dish.setImagePath(path.toString().substring(28).replace("\\", "/"));
    }
}
