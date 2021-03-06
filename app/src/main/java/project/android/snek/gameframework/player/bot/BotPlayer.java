package project.android.snek.gameframework.player.bot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;

import project.android.snek.gameframework.controller.Grid;
import project.android.snek.gameframework.player.Player;
import project.android.snek.utils.Vector;

@SuppressLint("ViewConstructor")
public class BotPlayer extends Player {

    private final Brain brain;
    private int phi = 0;

    public BotPlayer(Context context, String name, Brain brain) {
        super(context, randomPosition(), name);
        this.brain = brain;

        initView(context);
        tail.updateSize(12);
    }

    @Override
    public void update() {
        super.update();

        if (++phi % 24 == 0) {
            direction = brain.predict(this);
            phi = 0;
        }
        bounce();

        final Vector v = direction.apply(d -> d * speed);

        absolutePosition = absolutePosition.apply(Double::sum, v);
        tail.onBeforeDraw(relativePosition, Grid.getInstance().dV().apply(Player::scale), scale(radius), visible || isVisible());

        if(visible) {
            calcRelativePosition();
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isVisible()) {
            canvas.drawCircle((float) relativePosition.x,(float) relativePosition.y,
                    (float) (scale(radius)), circlePaint);

            canvas.drawText(name, (float) relativePosition.x, (float) (relativePosition.y - scale(radius) - textPaint.descent()), textPaint);
            canvas.drawText(String.valueOf((int) radius),
                    (float) relativePosition.x, (float) relativePosition.y - (textPaint.ascent() / 2),textPaint);
        }

        super.onDraw(canvas);
    }

}
