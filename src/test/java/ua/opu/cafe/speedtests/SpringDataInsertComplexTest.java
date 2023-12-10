package ua.opu.cafe.speedtests;

import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.cafe.CafeApplication;
import ua.cafe.models.Detail;
import ua.cafe.models.Dish;
import ua.cafe.models.Order;
import ua.cafe.repositories.DetailRepository;
import ua.cafe.repositories.DishRepository;
import ua.cafe.repositories.OrderRepository;

@SpringBootTest(classes = CafeApplication.class)
public class SpringDataInsertComplexTest {

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private DishRepository dishRepository;
  @Autowired
  private DetailRepository detailRepository;

  private Dish dish1, dish2;

  @BeforeEach
  void setUp() {
    orderRepository.deleteAll();
    dishRepository.deleteAll();

    dish1 = dishRepository.save(new Dish(0, null, "Dish 1", 1, 10.0, "Ingredients 1"));
    dish2 = dishRepository.save(new Dish(0, null, "Dish 2", 1, 15.0, "Ingredients 2"));
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
  void insertOneByOne10_000() {
    baseOneByOne(10_000);
  }

  @WithinTransaction15
  void insertAtOnce10_000() {
    baseTransactionalMethod(10_000);
  }

  @RepeatedTest(15)
  void insertOneByOne100_000() {
    baseOneByOne(100_000);
  }

  @WithinTransaction
  void insertAtOnce100_000() {
    baseTransactionalMethod(100_000);
  }

  private void baseOneByOne(int x) {
    for (int i = 0; i < x; i++) {
      Order order = new Order();
      order.setDateOrdered(new Date());
      order.setTableNum(i % Order.TABLES_COUNT + 1);
      order.setComments("Order " + i);

      orderRepository.saveAndFlush(order);
      detailRepository.save(new Detail(dish1, order));
      detailRepository.save(new Detail(dish2, order));
    }
  }

  private void baseTransactionalMethod(int x) {
    for (int i = 0; i < x; i++) {
      Order order = new Order();
      order.setDateOrdered(new Date());
      order.setTableNum(i % Order.TABLES_COUNT + 1);
      order.setComments("Order " + i);

      orderRepository.save(order);
      detailRepository.save(new Detail(dish1, order));
      detailRepository.save(new Detail(dish2, order));
    }
  }
}
