package ua.opu.cafe.speedtests;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.cafe.CafeApplication;
import ua.cafe.models.Category;
import ua.cafe.models.Detail;
import ua.cafe.models.Dish;
import ua.cafe.models.Order;
import ua.cafe.repositories.CategoriesRepository;
import ua.cafe.repositories.DetailRepository;
import ua.cafe.repositories.DishRepository;
import ua.cafe.repositories.OrderRepository;

@SpringBootTest(classes = CafeApplication.class)
public class SpringDataUpdateComplexTest {

  public Category CATEGORY;
  @Autowired
  CategoriesRepository categoriesRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private DishRepository dishRepository;
  @Autowired
  private DetailRepository detailRepository;
  private Dish testDish;
  private Order testOrder;

  @PostConstruct
  void init() {
    CATEGORY = categoriesRepository.save(new Category(1, "Category One", 1));
  }

  @BeforeEach
  void setUp() {
    orderRepository.deleteAll();
    dishRepository.deleteAll();

    testDish = new Dish(0, CATEGORY, "Test Dish", 100, 20.0, "Test Ingredients");
    testDish = dishRepository.save(testDish);

    testOrder = new Order();
    testOrder.setDateOrdered(new Date());
    testOrder.setTableNum(ThreadLocalRandom.current().nextInt(Order.TABLES_COUNT + 1));
    testOrder.setComments("Test Order");
    testOrder = orderRepository.save(testOrder);

    Detail detail = new Detail(testDish, testOrder);
    detailRepository.save(detail);
  }

  @RepeatedTest(100)
  void updateDishTest() {
    Dish fetchedDish = dishRepository.findById(testDish.getId()).orElseThrow();
    fetchedDish.setName("Updated Dish Name");
    dishRepository.saveAndFlush(fetchedDish);

    Dish updatedDish = dishRepository.findById(testDish.getId()).orElseThrow();
    Assertions.assertEquals("Updated Dish Name", updatedDish.getName());
  }

  @WithinTransaction100
  void updateOrderTest() {
    Order fetchedOrder = orderRepository.findById(testOrder.getId()).orElseThrow();
    fetchedOrder.setComments("Updated Order Comment");
    orderRepository.save(fetchedOrder);

    Order updatedOrder = orderRepository.findById(testOrder.getId()).orElseThrow();
    Assertions.assertEquals("Updated Order Comment", updatedOrder.getComments());
  }
}
