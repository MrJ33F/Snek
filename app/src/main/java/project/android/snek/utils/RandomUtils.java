package project.android.snek.utils;

import java.util.Random;

public final class RandomUtils {

    private static final Random random = new Random();

    public static int nextInt(int bound) {
        return random.nextInt(bound);
    }

}
