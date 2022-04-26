package ua.opu.cafe.service;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PdfServiceTest {
    int MIN = 100;
    int MAX = 600;

    @Test
    void create() {
        int randQuantity = ThreadLocalRandom.current().nextInt(MIN, MAX);

        for (int i = 0; i < randQuantity; i++) {
            Logger.getAnonymousLogger().info(String.valueOf(randQuantity));
        }
        assertEquals(1, 1);
    }

    @Test
    void edit() {
        int randQuantity = ThreadLocalRandom.current().nextInt(MIN, MAX);

        for (int i = 0; i < randQuantity; i++) {
            Logger.getAnonymousLogger().info(String.valueOf(randQuantity));
        }
        assertEquals(1, 1);
    }

    @Test
    void remove() {
        int randQuantity = ThreadLocalRandom.current().nextInt(MIN, MAX);

        for (int i = 0; i < randQuantity; i++) {
            Logger.getAnonymousLogger().info(String.valueOf(randQuantity));
        }
        assertEquals(1, 1);
    }
}
