package project.android.snek.gameframework.perk;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import project.android.snek.R;
import project.android.snek.gameframework.GameConstants;
import project.android.snek.gameframework.player.Player;

public class SizePerk extends PerkObject {

    private Drawable skin;
    private Paint paint;
    private boolean applied = false;

    public SizePerk(Context context) {
        super(context, randomPosition());

        initView(context);
    }

    @Override
    public void update() {
        boolean wasVisible = visible;

        if (isVisible() || wasVisible) {
            calcRelativePosition();
            skin.setBounds(calcBounds(GameConstants.PERK_SIZE, 1.0));
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

        skin = context.getDrawable(R.drawable.ic_health_care);
        skin.setBounds(calcBounds(GameConstants.PERK_SIZE, 0.75));
    }

    @Override
    public void apply(Player player) {
        player.setRadius(player.getRadius() + 10);

        applied = true;
    }

    @Override
    public void attach(Player player) {

    }

    @Override
    public boolean isActive() {
        return !applied;
    }
}
