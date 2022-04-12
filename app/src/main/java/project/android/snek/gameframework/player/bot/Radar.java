package project.android.snek.gameframework.player.bot;

import android.util.Log;

import java.util.List;

import project.android.snek.gameframework.food.*;
import project.android.snek.gameframework.player.Player;
import project.android.snek.gameframework.player.PlayerProvider;

import project.android.snek.utils.Vector;

public class Radar {

    private static final int N = 360 / ModelConstants.THETA;

    private final FoodProvider foodProvider;
    private final PlayerProvider playerProvider;

    public Radar(FoodProvider foodProvider, PlayerProvider playerProvider) {
        this.foodProvider = foodProvider;
        this.playerProvider = playerProvider;
    }

    public double[] input(Player bot) {
        return calcFoodInput(bot.getAbsolutePosition());
    }

    private double[] calcFoodInput(Vector abs) {
        final double[] foodInput = new double[N];

        for (int i = 0; i < N; i++) {
            foodInput[i] = -1;
        }

        List<Food> food = foodProvider.foodWithinRadius(abs, ModelConstants.VISIBILITY_RADIUS);

        Log.d("COunt", String.valueOf(food.size()));

        food.forEach(f-> {
            int segment = segment(abs, f.getAbsolutePosition());
            double input = normalizedDistance(abs, f.getAbsolutePosition());

            if (foodInput[segment] == -1 || foodInput[segment] > input)
                foodInput[segment] = input;
        });

        return foodInput;
    }

    private double[] calcPlayerInput(Player bot) {
        final double[] input = new double[2 * N];

        for (int i = 0; i < 360 / ModelConstants.THETA; i++) {
            input[i] = -1;
            input[N + i] = 0;
        }

        List<Player> players = playerProvider.playersWithinRadius(bot, ModelConstants.VISIBILITY_RADIUS * bot.getMagnitude());

        players.forEach(player -> {
            int segment = segment(bot.getAbsolutePosition(), player.getAbsolutePosition());
            double distance = normalizedDistance(bot.getAbsolutePosition(), player.getAbsolutePosition());
            double ratio = normalizedRatio(player.getRadius(), bot.getRadius());

            if (input[segment] == -1 || input[segment] > distance) {
                input[segment] = distance;
                input[segment + N ] = ratio;
            }
        });

        return input;
    }

    private double normalizedRatio(double radiusA, double radiusB) {
        double val = 1 - (radiusA / radiusB);

        if (val < -1)
            val = -1;

        return val;
    }

    private double normalizedDistance(Vector a, Vector b) {
        return a.distance(b) / ModelConstants.VISIBILITY_RADIUS;
    }

    private int segment(Vector center, Vector pos) {
        double angle = (angle(center, pos) + Math.PI) * 180 / Math.PI;

        return (int) (angle / ModelConstants.THETA);
    }

    private double angle(Vector center, Vector abs) {
        final double dy = center.y - abs.y;
        final double dx = center.x - abs.x;

        return Math.atan2(dy, dx);
    }

}
