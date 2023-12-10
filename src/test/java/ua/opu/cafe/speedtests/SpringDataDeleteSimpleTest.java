package ua.opu.cafe.speedtests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.cafe.CafeApplication;
import ua.cafe.models.Category;
import ua.cafe.models.Detail;
import ua.cafe.models.Dish;
import ua.cafe.models.Order;
import ua.cafe.models.ReadyStatus;
import ua.cafe.repositories.CategoriesRepository;
import ua.cafe.repositories.DishRepository;
import ua.cafe.repositories.OrderRepository;


@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = CafeApplication.class)
public class SpringDataDeleteSimpleTest {

  private static final List<Detail> DETAILS = new ArrayList<>();
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private DishRepository dishRepository;
  @Autowired
  private CategoriesRepository categoriesRepository;
  private Dish testDish;
  private Order testOrder;
  private Category testCategory;

  @BeforeEach
  void setUp() {
    orderRepository.deleteAll();
    dishRepository.deleteAll();
    categoriesRepository.deleteAll();

    testCategory = new Category(Integer.MAX_VALUE, "Test Category", 1);
    testCategory = categoriesRepository.save(testCategory);

    testDish = new Dish(0, testCategory, "Test Dish", 100, 20.0, "Test Ingredients");
    testDish = dishRepository.save(testDish);

    testOrder = new Order(0, new Date(), "", 1, ReadyStatus.NEW, DETAILS);
    orderRepository.save(testOrder);
  }

  @WithinTransaction100
  @org.junit.jupiter.api.Order(1)
  void deleteOrderTest() {
    orderRepository.deleteById(testOrder.getId());

    Assertions.assertFalse(orderRepository.existsById(testOrder.getId()));
  }

  @WithinTransaction100
  @org.junit.jupiter.api.Order(2)
  void deleteDishTest() {
    dishRepository.deleteById(testDish.getId());

    Assertions.assertFalse(dishRepository.existsById(testDish.getId()));
  }

  @WithinTransaction100
  @org.junit.jupiter.api.Order(3)
  void deleteCategoryTest() {
    dishRepository.deleteAll();
    categoriesRepository.deleteById(testCategory.getId());

    Assertions.assertFalse(categoriesRepository.existsById(testCategory.getId()));
  }
}
