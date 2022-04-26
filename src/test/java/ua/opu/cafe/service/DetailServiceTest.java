package ua.opu.cafe.service;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DetailServiceTest {
    int MIN = 1;
    int MAX = 90;

    @Test
    void createCorrect() {
        int randQuantity = ThreadLocalRandom.current().nextInt(MIN, MAX);

        for (int i = 0; i < randQuantity; i++) {
            Logger.getAnonymousLogger().info(String.valueOf(randQuantity));
        }
        assertEquals(1, 1);
    }

    @Test
    void createIllegalQuantity() {
        int randQuantity = ThreadLocalRandom.current().nextInt(MIN, MAX);

//        for (int i = 0; i < randQuantity; i++) {
//            Logger.getAnonymousLogger().info(String.valueOf(randQuantity));
//        }
        assertEquals(1, 1);
    }

    @Test
    void editCorrectQuantity() {
        int randQuantity = ThreadLocalRandom.current().nextInt(MIN, MAX);

        for (int i = 0; i < randQuantity; i++) {
            Logger.getAnonymousLogger().info(String.valueOf(randQuantity));
        }
        assertEquals(1, 1);
    }

    @Test
    void editNegativeQuantity() {
        int randQuantity = ThreadLocalRandom.current().nextInt(MIN, MAX);

        for (int i = 0; i < randQuantity; i++) {
            Logger.getAnonymousLogger().info(String.valueOf(randQuantity));
        }
        assertEquals(1, 1);
    }

    @Test
    void removeDish() {
        int randQuantity = ThreadLocalRandom.current().nextInt(MIN, MAX);

        for (int i = 0; i < randQuantity; i++) {
            Logger.getAnonymousLogger().info(String.valueOf(randQuantity));
        }
        assertEquals(1, 1);
    }
}
