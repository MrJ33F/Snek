package project.android.snek.gameframework;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import java.util.Random;

import project.android.snek.gameframework.controller.Grid;
import project.android.snek.utils.Vector;

public abstract class GameObject extends View {

    protected final static Random random = new Random();

    protected Vector absolutePosition;
    protected Vector relativePosition;
    protected boolean visible = false;

    public GameObject(Context context) {super(context);}

    public GameObject(Context context, Vector initialAbsolutePosition) {
        this(context);
        this.absolutePosition = initialAbsolutePosition;
        calcRelativePosition();
    }

    //Trebuie apelata la fiecare frame
    public abstract void update();
    protected abstract void initView(Context context);

    protected void calcRelativePosition(){relativePosition = Grid.getInstance().calcDimensions(absolutePosition);}

    public boolean isVisible() {
        visible = Grid.getInstance().visible(absolutePosition);
        return visible;
    }

    //Utils
    public boolean withinCircle(Vector center, double radius) {
        double distance = absolutePosition.distance(center);

        return distance < radius;
    }

    protected Rect calcBounds(double radius, double factor) {
        double phi = scale(factor * radius);

        return new Rect(
                (int) (relativePosition.x - phi),
                (int) (relativePosition.y - phi),
                (int) (relativePosition.x + phi),
                (int) (relativePosition.y + phi));
    }

    protected static Vector randomPosition() {
        return Grid.randomPosition();
    }

    protected static double scale(double v) {
        return Grid.getInstance().scale(v);
    }

    //Getters
    public Vector getAbsolutePosition() {
        return absolutePosition;
    }
    public double getAbsoluteX() {
        return absolutePosition.x;
    }
    public double getAbsoluteY() {
        return absolutePosition.y;
    }
}
