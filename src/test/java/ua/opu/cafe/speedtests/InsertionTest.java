package ua.opu.cafe.speedtests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.cafe.CafeApplication;
import ua.cafe.models.Category;
import ua.cafe.models.Dish;
import ua.cafe.repositories.CategoriesRepository;
import ua.cafe.repositories.DishRepository;

@SpringBootTest(classes = CafeApplication.class)
public class InsertionTest {

  @Autowired
  DishRepository dishRepository;
  @Autowired
  CategoriesRepository categoriesRepository;

  @Test
  void insertOneByOne() {
    var category = categoriesRepository.save(new Category(0, "Category One", 1));
    for (int i = 0; i < 100; i++) {
      dishRepository.save(new Dish(category, "Dish " + "x".repeat(i), i + 1, 10., "No ingredients"));
    }
    System.out.println("finished");
  }
}
