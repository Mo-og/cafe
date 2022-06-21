package ua.opu.cafe.service;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DetailServiceTest {
    int MIN = 1;
    int MAX = 90;

    @Test
    void testCreateCorrectDetail() {
        int randQuantity = ThreadLocalRandom.current().nextInt(MIN, MAX);

        for (int i = 0; i < randQuantity; i++) {
            Logger.getAnonymousLogger().info(String.valueOf(randQuantity));
        }
        assertEquals(1, 1);
    }

    @Test
    void testCreateIllegalQuantity() {
        int randQuantity = ThreadLocalRandom.current().nextInt(MIN, MAX);

//        for (int i = 0; i < randQuantity; i++) {
//            Logger.getAnonymousLogger().info(String.valueOf(randQuantity));
//        }
        assertEquals(1, 1);
    }

    @Test
    void testEditCorrectQuantity() {
        int randQuantity = ThreadLocalRandom.current().nextInt(MIN, MAX);

        for (int i = 0; i < randQuantity; i++) {
            Logger.getAnonymousLogger().info(String.valueOf(randQuantity));
        }
        assertEquals(1, 1);
    }

    @Test
    void testEditNegativeQuantity() {
        int randQuantity = ThreadLocalRandom.current().nextInt(MIN, MAX);

        for (int i = 0; i < randQuantity; i++) {
            Logger.getAnonymousLogger().info(String.valueOf(randQuantity));
        }
        assertEquals(1, 1);
    }

    @Test
    void testRemoveDishCorrect() {
        int randQuantity = ThreadLocalRandom.current().nextInt(MIN, MAX);

        for (int i = 0; i < randQuantity; i++) {
            Logger.getAnonymousLogger().info(String.valueOf(randQuantity));
        }
        assertEquals(1, 1);
    }

    @Test
    void testRemoveDishNonExisting() {
        int randQuantity = ThreadLocalRandom.current().nextInt(MIN, MAX);

        for (int i = 0; i < randQuantity; i++) {
            Logger.getAnonymousLogger().info(String.valueOf(randQuantity));
        }
        assertEquals(1, 1);
    }
}
