package ua.opu.cafe;

import org.junit.jupiter.api.Test;
import ua.cafe.models.Detail;
import ua.cafe.models.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ReportTest {

    int iterations = 25;

    @Test
    void testSortByQuantity() {
        Order order = new Order();
        List<Detail> details = new ArrayList<>();
        List<Integer> expectedList = new ArrayList<>();
        int[] actual = new int[iterations];
        int[] expected = new int[iterations];

        for (int i = 0; i < iterations; i++) {
            int randQuantity = ThreadLocalRandom.current().nextInt(1, 10);
            details.add(new Detail(i, 1, randQuantity));
            expectedList.add(randQuantity);
        }

        expectedList.sort(Collections.reverseOrder());
        order.setDetails(details);
        order.sortByQuantity();
        List<Detail> temp = order.getDetails();
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
    void testSortByQuantityIncludeUnordered() {
        Order order = new Order();
        List<Detail> details = new ArrayList<>();
        List<Integer> expectedList = new ArrayList<>();
        int[] actual = new int[iterations];
        int[] expected = new int[iterations];

        for (int i = 0; i < iterations; i++) {
            int randQuantity = ThreadLocalRandom.current().nextInt(1, 10);
            details.add(new Detail(i, 1, randQuantity));
            expectedList.add(randQuantity);
        }

        expectedList.sort(Collections.reverseOrder());
        order.setDetails(details);
        order.sortByQuantity();
        List<Detail> temp = order.getDetails();
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
    void testSortByCost() {
        Order order = new Order();
        List<Detail> details = new ArrayList<>();
        List<Integer> expectedList = new ArrayList<>();
        int[] actual = new int[iterations];
        int[] expected = new int[iterations];

        for (int i = 0; i < iterations; i++) {
            int randQuantity = ThreadLocalRandom.current().nextInt(1, 10);
            details.add(new Detail(i, 1, randQuantity));
            expectedList.add(randQuantity);
        }

        expectedList.sort(Collections.reverseOrder());
        order.setDetails(details);
        order.sortByQuantity();
        List<Detail> temp = order.getDetails();
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
    void testSortByIncludeUnordered() {
        Order order = new Order();
        List<Detail> details = new ArrayList<>();
        List<Integer> expectedList = new ArrayList<>();
        int[] actual = new int[iterations];
        int[] expected = new int[iterations];

        for (int i = 0; i < iterations; i++) {
            int randQuantity = ThreadLocalRandom.current().nextInt(1, 10);
            details.add(new Detail(i, 1, randQuantity));
            expectedList.add(randQuantity);
        }

        expectedList.sort(Collections.reverseOrder());
        order.setDetails(details);
        order.sortByQuantity();
        List<Detail> temp = order.getDetails();
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

}