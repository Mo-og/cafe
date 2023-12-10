package ua.opu.cafe.speedtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.cafe.CafeApplication;
import ua.cafe.models.Category;
import ua.cafe.models.Dish;
import ua.cafe.repositories.CategoriesRepository;
import ua.cafe.repositories.DishRepository;

@SpringBootTest(classes = CafeApplication.class)
public class SpringDataInsertSimpleTest {

  @Autowired
  DishRepository dishRepository;
  @Autowired
  CategoriesRepository categoriesRepository;

  @BeforeEach
  void clear() {
    dishRepository.deleteAll();
    categoriesRepository.deleteAll();
  }

  @RepeatedTest(100)
  void insertOneByOne100() {
    baseOneByOne(100);
  }

  @WithinTransaction100
  void insertAtOnce100() {
    baseTransactionalMethod(100);
  }

  @RepeatedTest(100)
  void insertOneByOne1000() {
    baseOneByOne(1000);
  }

  @WithinTransaction100
  void insertAtOnce1000() {
    baseTransactionalMethod(1000);
  }

  @RepeatedTest(15)
  void insertOneByOne10000() {
    baseOneByOne(10_000);
  }

  @WithinTransaction15
  void insertAtOnce10000() {
    baseTransactionalMethod(10_000);
  }

  @Test
  void insertOneByOne100_000() {
    baseOneByOne(100_000);
  }

  @WithinTransaction
  void insertAtOnce100_000() {
    baseTransactionalMethod(100_000);
  }

  private void baseOneByOne(int x) {
    var category = categoriesRepository.saveAndFlush(new Category(0, "Category One", 1));
    for (int i = 0; i < x; i++) {
      dishRepository.saveAndFlush(new Dish(i + 1, category, "Dish x", i + 1, 10., "No ingredients"));
    }
  }

  private void baseTransactionalMethod(int x) {
    var category = categoriesRepository.save(new Category(0, "Category One", 1));
    for (int i = 0; i < x; i++) {
      dishRepository.save(new Dish(i + 1, category, "Dish x", i + 1, 10., "No ingredients"));
    }
  }
}
