package project.android.snek.gameframework.player.human;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;

import project.android.snek.gameframework.controller.Grid;
import project.android.snek.gameframework.controller.interfaces.OnDirectionChangedListener;
import project.android.snek.gameframework.player.Player;
import project.android.snek.utils.Vector;

@SuppressLint("ViewConstructor")
public class HumanPlayer extends Player {

    public HumanPlayer(Context context, OnDirectionChangedListener listener, Vector initPosition, String name) {
        super(context, initPosition, name);
        this.relativePosition = Grid.getInstance().relativeCenter();

        listener.setOnDirectionChanged(vector -> direction = vector);

        initView(context);
    }

    @Override
    public void update() {
        super.update();

        final Vector v = direction.apply(d -> d * speed);

        absolutePosition = absolutePosition.apply(Double::sum, v);
        tail.onBeforeDraw(relativePosition, v.apply(Player::scale), scale(radius), true);

        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle((float) relativePosition.x,(float) relativePosition.y,
                (float) (scale(radius)), circlePaint);

        skin.draw(canvas);

        canvas.drawText(name, (float) relativePosition.x, (float) (relativePosition.y - scale(radius) - (textPaint.descent() / 2)), textPaint);
        canvas.drawText(String.valueOf((int) radius),
                (float) relativePosition.x, (float) relativePosition.y - (textPaint.ascent() / 2), textPaint);

        super.onDraw(canvas);
    }

}
