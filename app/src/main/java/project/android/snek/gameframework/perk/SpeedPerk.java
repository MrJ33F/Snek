package project.android.snek.gameframework.perk;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import project.android.androidprojectsnek.R;
import project.android.snek.gameframework.GameConstants;
import project.android.snek.gameframework.controller.GameEngine;
import project.android.snek.gameframework.player.Player;

public class SpeedPerk extends PerkObject {

    private int framesLeft = GameEngine.FPS * 10; // 10 seconds

    private Drawable skin;
    private Paint paint;

    public SpeedPerk(Context context) {
        super(context, randomPosition());

        initView(context);
    }

    @Override
    public void update() {
        boolean wasVisible = visible;

        if (isVisible() || wasVisible) {
            calcRelativePosition();
            skin.setBounds(calcBounds(GameConstants.PERK_SIZE, .8));
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isVisible()) {
            canvas.drawCircle((float) relativePosition.x, (float) relativePosition.y, (float) scale(GameConstants.PERK_SIZE), paint);
            skin.draw(canvas);
        }

        super.onDraw(canvas);
    }

    @Override
    protected void initView(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(ResourcesCompat.getColor(getResources(), R.color.orange, context.getTheme()));

        skin = context.getDrawable(R.drawable.ic_runner);
        skin.setBounds(calcBounds(GameConstants.PERK_SIZE, .75));
    }

    @Override
    public void apply(Player player) {
        if (framesLeft == 1 && player.getSpeed() > GameConstants.INIT_SPEED)
            player.setSpeed(player.getSpeed() / 2);
        else if(player.getSpeed() < 3 * GameConstants.INIT_SPEED)
            player.setSpeed(player.getSpeed() * 2);

        framesLeft--;
    }

    @Override
    public void attach(Player player) {

    }

    @Override
    public boolean isActive() {
        return framesLeft > 0;
    }
}

