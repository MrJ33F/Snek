package project.android.snek.multiplayer.game;



import java.util.Random;

import project.android.snek.gameframework.GameConstants;
import project.android.snek.utils.Vector;


public class Grid {

    private static final Random random = new Random();
    private static Grid instance;

    public int screenWidth;
    public int screenHeight;

    private double scale = 1.0; // >= 1 (zooming out)
    private Vector center; // absolute



    private Vector scaledTopLeft;
    private Vector scaledBottomRight;
    private Vector dV;



    private Grid(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.center = absoluteCenter();

        update(center, 1.0);
    }

    public static Grid init(int screenWidth, int screenHeight) {
        instance = new Grid(screenWidth, screenHeight);

        return instance;
    }

    public static Grid getInstance() {
        if (instance == null)
            throw new AssertionError("Grid should be initialized first!");

        return instance;
    }


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


    public boolean visible(Vector abs) {
        return abs.x >= scaledTopLeft.x && abs.y >= scaledTopLeft.y &&
                abs.x <= scaledBottomRight.x && abs.y <= scaledBottomRight.y;
    }


    public Vector calcDimensions(Vector abs) {
        final Vector scaledRelative = abs.apply((a, b) -> a - b, scaledTopLeft);

        return scaledRelative.apply(x -> x / scale);
    }


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
