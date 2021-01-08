package ua.opu.kurs_gorbik_kozyrevych;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    int iterations = 25;

    @Test
    void sortByQuantity() {
        Order order = new Order();
        List<Details> details = new ArrayList<>();
        List<Integer> expectedList = new ArrayList<>();
        int[] actual = new int[iterations];
        int[] expected = new int[iterations];

        for (int i = 0; i < iterations; i++) {
            int randQuantity = ThreadLocalRandom.current().nextInt(1, 10);
            details.add(new Details(i, 1, randQuantity));
            expectedList.add(randQuantity);
        }

        expectedList.sort(Collections.reverseOrder());
        order.setDetails(details);
        order.sortByQuantity();
        List<Details> temp = order.getDetails();
        for (int i = 0; i < iterations; i++) {
            actual[i] = temp.get(i).getQuantity();
            expected[i] = expectedList.get(i);
        }
        StringBuilder actualStr = new StringBuilder();
        StringBuilder expectedStr = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            actualStr.append(actual[i]);
            expectedStr.append(expected[i]);
        }
        System.out.println("Actual quantities list: " + actualStr + "\nExpected quantities list: " + expectedStr);
        assertArrayEquals(expected, actual);
    }


    @Test
    void removeFromOrder() {
        Order order = new Order();
        List<Details> details = new ArrayList<>();
        List<Details> expectedList = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            int randQuantity = ThreadLocalRandom.current().nextInt(1, 11);
            //System.out.print(randQuantity+"; ");
            details.add(new Details(i, 1, randQuantity, new Dish(i)));
            if (i != 1)
                expectedList.add(new Details(i, i, randQuantity, new Dish(i)));
        }
        order.setDetails(details);
        order.removeFromOrder(1);
        long[] actual = new long[iterations];
        long[] expected = new long[iterations];
        List<Details> temp = order.getDetails();
        for (int i = 0; i < iterations - 1; i++) {
            actual[i] = temp.get(i).getId();
            expected[i] = expectedList.get(i).getId();
        }
        assertArrayEquals(actual, expected);
        System.out.println(order.getDetails());
    }

    @Test
    void removeFromOrderNotExisting() {
        Order order = new Order();
        List<Details> details = new ArrayList<>();
        List<Details> expectedList = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            int randQuantity = ThreadLocalRandom.current().nextInt(1, 11);
            details.add(new Details(i, 1, randQuantity, new Dish(i)));
        }
        order.setDetails(details);
        assertThrows(NoSuchElementException.class, () -> order.removeFromOrder(35));
        try {
            order.removeFromOrder(35);
        } catch (NoSuchElementException e) {
            System.out.println("Исключение выявлено.");
        }
    }

    @Test
    void getDishNames() {
        Details details1 = new Details(new Dish("dish1"), 2);
        Details details2 = new Details(new Dish("dish2"), 1);
        Details details3 = new Details(new Dish("dish3"), 2);
        Order order = new Order(new ArrayList<>(Arrays.asList(details1, details2, details3)));
        String actual = order.getDishNames();
        String expected = "dish1 (2 шт.), dish2 (1 шт.), dish3 (2 шт.)";
        System.out.println("Expected: " + expected);
        System.out.println("Actual  : " + actual);
        assertEquals(actual, expected);
    }
}