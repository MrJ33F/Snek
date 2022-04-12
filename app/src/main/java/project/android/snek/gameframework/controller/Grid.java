package project.android.snek.gameframework.controller;

import java.util.Random;

import project.android.snek.gameframework.GameConstants;
import project.android.snek.gameframework.GameObject;
import project.android.snek.utils.Vector;

/**
  Clasa se ocupa cu scalarea si calculul coordonatelor pasate la {@link GameObject} pe canvas
**/


public class Grid {

    private static final Random random = new Random();
    private static Grid instance;//singleton

    public int screenWidth;
    public int screenHeight;

    private double scale = 1.0; //pt valori >= 1 (se da zoom out)
    private Vector center; //poz absoluta

    //Optimizari
    private Vector scaledTopLeft;
    private Vector scaledBottomRight;
    private Vector dV;

    private Grid(int screenWidth, int screenHeight, Vector center) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.center = center;

        update(center, 1.0);
    }


    /**
     * !!Initializam cand ne conectam la server {@link Grid} prima data deoarece aceasta poate fi apelata de {@link GameObject} si poate cauza bugg-uri.
     */
    public static Grid init(int screenWidth, int screenHeight, Vector center) { //Singleton
        instance = new Grid(screenWidth, screenHeight, center);

        return instance;
    }
    public static Grid getInstance() {
        if (instance == null)
            throw new AssertionError("Grid trebuie initializat prima data!");

        return instance;
    }

    /**
     * GameEngine
     * @param ratio in circumstante normale ar trebui sa fie <code> player.radius / player.initRadius </code>
     * @param center pozitia absoluta a player-ului
     */

    public void update(Vector center, double ratio) {
        updateCenter(center);
        updateScale(ratio);
    }

    private void updateCenter(Vector center) {
        dV = center.subtract(this.center);
        this.center = center;
    }

    private void updateScale(double ratio) {
        scale = 0.25 + Math.sqrt(ratio);

        final Vector scaledScreen = Vector.create(screenWidth, screenHeight)
                .apply(d -> d * scale)
                .apply(d -> d / 2);

        scaledTopLeft = center.apply((a, b) -> a - b, scaledScreen);
        scaledBottomRight = center.apply(Double::sum, scaledScreen);
    }

    //pozitia absoluta a view-ului
    public boolean visible(Vector abs) {
        return abs.x >= scaledTopLeft.x && abs.y >= scaledTopLeft.y &&
                abs.x <= scaledBottomRight.x && abs.y <= scaledBottomRight.y;
    }

    public Vector calcDimensions(Vector abs) {
        final Vector scaledRelative = abs.apply((a, b) -> a - b, scaledTopLeft);

        return scaledRelative.apply(x -> x / scale);
    }

    //Utils
    public double scale(double v) {
        return v / scale;
    }

    public Vector relativeCenter() {
        return Vector.create(screenWidth, screenHeight).apply(v -> v / 2);
    }

    public Vector dV() {
        return dV;
    }

    public static Vector absoluteCenter() {
        return Vector.create(GameConstants.WIDTH, GameConstants.HEIGHT).apply(v -> v / 2);
    }

    public static Vector randomPosition() {
        return Vector.create(
                random.nextInt(GameConstants.WIDTH),
                random.nextInt(GameConstants.HEIGHT));
    }
}
