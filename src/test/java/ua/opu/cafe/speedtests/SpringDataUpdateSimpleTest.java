package ua.opu.cafe.speedtests;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.cafe.CafeApplication;
import ua.cafe.models.Category;
import ua.cafe.models.Dish;
import ua.cafe.repositories.CategoriesRepository;
import ua.cafe.repositories.DishRepository;

@SpringBootTest(classes = CafeApplication.class)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SpringDataUpdateSimpleTest {

  public static final List<Dish> DISHES_100_000 = new ArrayList<>(100_000);
  private static final int[] TEST_ITEMS_COUNTS = new int[]{100, 1000, 10_000, 100_000};
  private static int TEST_ITEMS_COUNT_INDEX = 0;
  private final AtomicInteger testsFinished = new AtomicInteger();
  public Category CATEGORY;
  @Autowired
  DishRepository dishRepository;
  @Autowired
  CategoriesRepository categoriesRepository;
  private long minId = 0;

  @PostConstruct
  void init() {
    CATEGORY = categoriesRepository.save(new Category(1, "Category One", 1));
    for (int i = 0; i < 100_000; i++) {
      DISHES_100_000.add(new Dish(i + 1, CATEGORY, "Dish x", i + 1, 10., "No ingredients"));
    }
  }

  @AfterEach
  void increment() {
    int finished = testsFinished.incrementAndGet();
    if (finished == 200 || finished == 400 || finished == 430) {
      TEST_ITEMS_COUNT_INDEX++;
    }
  }

  @BeforeEach
  void clear() {
    TEST_ITEMS_COUNT_INDEX = 3;
    dishRepository.deleteAll();
    dishRepository.saveAll(
        switch (TEST_ITEMS_COUNTS[TEST_ITEMS_COUNT_INDEX]) {
          case 100 -> DISHES_100_000.subList(0, 100);
          case 1000 -> DISHES_100_000.subList(0, 1000);
          case 10000 -> DISHES_100_000.subList(0, 10_000);
          case 100000 -> DISHES_100_000;
          default ->
              throw new IllegalStateException("Unexpected value: " + TEST_ITEMS_COUNTS[TEST_ITEMS_COUNT_INDEX++]);
        });
    dishRepository.flush();
    minId = dishRepository.findMinId();
  }

  @RepeatedTest(100)
  @Order(1)
  void updateOneByOne100() {
    System.err.println("updateOneByOne100 TEST_ITEMS_COUNT_INDEX: " + TEST_ITEMS_COUNT_INDEX);
    baseOneByOne(100);
  }

  @WithinTransaction100
  @Order(1)
  void updateAtOnce100() {
    System.err.println("updateAtOnce100 TEST_ITEMS_COUNT_INDEX: " + TEST_ITEMS_COUNT_INDEX);
    baseTransactionalMethod(100);
  }

  //200
  @RepeatedTest(100)
  @Order(2)
  void updateOneByOne1000() {
    System.err.println("updateOneByOne1000 (1) TEST_ITEMS_COUNT_INDEX: " + TEST_ITEMS_COUNT_INDEX);
//    TEST_ITEMS_COUNT_INDEX = 1;
    baseOneByOne(1000);
  }

  @WithinTransaction100
  @Order(3)
  void updateAtOnce1000() {
    System.err.println("updateAtOnce1000 (1) TEST_ITEMS_COUNT_INDEX: " + TEST_ITEMS_COUNT_INDEX);
    baseTransactionalMethod(1000);
  }

  //400
  @RepeatedTest(15)
  @Order(4)
  void updateOneByOne10000() {
    System.err.println("updateOneByOne10000 (2) TEST_ITEMS_COUNT_INDEX: " + TEST_ITEMS_COUNT_INDEX);
//    TEST_ITEMS_COUNT_INDEX = 2;
    baseOneByOne(10_000);
  }

  @WithinTransaction15
  @Order(5)
  void updateAtOnce10000() {
    System.err.println("updateAtOnce10000 (2) TEST_ITEMS_COUNT_INDEX: " + TEST_ITEMS_COUNT_INDEX);
    baseTransactionalMethod(10_000);
  }

  //430
  @Test
  @Order(6)
  void updateOneByOne100_000() {
    System.err.println("updateOneByOne100_000 (3) TEST_ITEMS_COUNT_INDEX: " + TEST_ITEMS_COUNT_INDEX);
//    TEST_ITEMS_COUNT_INDEX = 3;
    baseOneByOne(100_000);
  }

  @WithinTransaction15
  @Order(7)
  void updateAtOnce100_000() {
    System.err.println("updateAtOnce100_000 (3) TEST_ITEMS_COUNT_INDEX: " + TEST_ITEMS_COUNT_INDEX);
    baseTransactionalMethod(100_000);
  }

  private void baseOneByOne(int x) {
    for (long i = minId; i < minId + x; i++) {
      Dish dish = dishRepository.findById(i).orElseThrow();
      dish.setName("Dish updated");
      dishRepository.saveAndFlush(dish);
    }
  }

  private void baseTransactionalMethod(int x) {
    for (long i = minId; i < minId + x; i++) {
      Dish dish = dishRepository.findById(i).orElseThrow();
      dish.setName("Dish updated");
      dishRepository.save(dish);
    }
  }
}