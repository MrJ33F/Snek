package project.android.snek.gameframework.player;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import java.util.LinkedList;
import java.util.List;

import project.android.snek.R;
import project.android.snek.customization.Config;
import project.android.snek.customization.SkinDrawable;
import project.android.snek.gameframework.GameConstants;
import project.android.snek.gameframework.GameConstants;
import project.android.snek.gameframework.GameObject;
import project.android.snek.gameframework.controller.GameViewController;
import project.android.snek.gameframework.perk.Perk;
import project.android.snek.utils.Vector;

public abstract class Player extends GameObject {

    protected Vector direction = Vector.UP;
    protected String name;
    protected double radius = GameConstants.PLAYER_INIT_RADIUS;
    protected double speed = GameConstants.INIT_SPEED;
    protected List<Perk> perks;

    protected Paint circlePaint;
    protected Paint textPaint;

    protected Tail tail;
    protected Drawable skin;

    public Player(Context context, Vector initPosition, String name) {
        super(context, initPosition);

        this.name = name;
        this.perks = new LinkedList<>();

        calcRelativePosition();
    }

    @Override
    protected void initView(Context context) {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(ResourcesCompat.getColor(getResources(), R.color.player, context.getTheme()));

        textPaint = new Paint();
        textPaint.setColor(0xffffffff);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.luckiest_guy));
        textPaint.setTextAlign(Paint.Align.CENTER);

        skin = context.getDrawable(SkinDrawable.valueOf(Config.getInstance(context).getSkinName()).resId);
        tail = new Tail(context);

        GameViewController.getInstance().attachView(tail);

        adjustTextSize();
        setBoundsOnDrawable();
    }

    @Override
    public void update() {
        adjustSpeed();
        perks.removeIf(perk -> !perk.isActive());
        perks.forEach(perk -> perk.apply(this));

        bounce();
    }

    public void changeColor(int color) {
        circlePaint.setColor(color);
        if (tail != null)
            tail.setColor(color);
    }

    public void grow(double r) {
        radius = Math.sqrt(Math.pow(r / 2, 2) + Math.pow(radius, 2));

        adjustTextSize();
        setBoundsOnDrawable();
        adjustTailSize();
    }

    public void onDeath() {
        if (tail != null)
            tail.destroy();
    }

    public boolean intersects(GameObject o) {
        double distance = absolutePosition.distance(o.getAbsolutePosition());

        return radius >= distance;
    }

    protected void setBoundsOnDrawable() {
        skin.setBounds(calcBounds(radius, 1.0));
    }

    protected void bounce() {
        if (absolutePosition.x <= 0)
            direction = Vector.RIGHT;
        else if (absolutePosition.y <= 0)
            direction = Vector.DOWN;
        else if (absolutePosition.x >= GameConstants.WIDTH)
            direction = Vector.LEFT;
        else if (absolutePosition.y >= GameConstants.HEIGHT)
            direction = Vector.UP;
    }

    protected void adjustTextSize() {
        int spSize = (int) Math.sqrt(radius) + 12;
        textPaint.setTextSize((float) (scale(spSize * getContext().getResources().getDisplayMetrics().scaledDensity)));
    }

    private void adjustTailSize() {
        /*
        if (radius > 150)
            tail.updateSize(50);
        else if (radius > 350)
            tail.updateSize(15);
        else if (radius > 500)
            tail.updateSize(5);
        */
    }

    private void adjustSpeed() {
        /*
        if (radius < 100) {
            speed = GameConstants.INIT_SPEED * 3/2;
        } else if (radius < 200) {
            speed = GameConstants.INIT_SPEED;
        } else
            speed = GameConstants.INIT_SPEED * 4/5;
        */
    }

    public void addPerk(Perk perk) {
        perks.add(perk);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double newSpeed) {
        speed = newSpeed;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public double getMagnitude() {
        return radius / GameConstants.PLAYER_INIT_RADIUS;
    }

    public String getName() {
        return name;
    }
    
}
