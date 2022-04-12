package project.android.snek.gameframework.food;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import project.android.snek.gameframework.GameConstants;
import project.android.snek.gameframework.GameObject;
import project.android.snek.utils.ColorUtils;

public class Food extends GameObject {

    private double radius;
    private Paint circlePaint;

    public Food(Context context) {
        super(context);

        radius = random.nextInt(5) + GameConstants.FOOD_RADIUS;
        absolutePosition = randomPosition();

        initView(context);
    }

    @Override
    protected void initView(Context context) {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(ColorUtils.rainbowColor());
    }

    @Override
    public void update() {
        calcRelativePosition();
    }

    public void drawItself(Canvas canvas) {
        canvas.drawCircle((float) relativePosition.x, (float) relativePosition.y, (float) (scale(radius)), circlePaint);
    }

    public void relocate() {
        absolutePosition = randomPosition();
    }

    public int getColor() {
        return circlePaint.getColor();
    }

    public double getRadius() {
        return radius;
    }
}
