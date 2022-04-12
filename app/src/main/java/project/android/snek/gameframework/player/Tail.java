package project.android.snek.gameframework.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import project.android.snek.R;
import project.android.snek.utils.Vector;


@SuppressLint("ViewConstructor")
public class Tail extends View {

    private int maxSize = 64;
    private Paint paint;
    private Queue<Path> paths;

    public Tail(Context context) {
        super(context);

        paths = new LinkedBlockingDeque<>(maxSize);

        initView(context);
    }

    private void initView(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(ResourcesCompat.getColor(context.getResources(), R.color.player, null));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(4.0f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void updateSize(int newSize) {
        if (newSize == maxSize)
            return;

        Queue<Path> pathQueue = new LinkedBlockingDeque<>(newSize);

        while (!paths.isEmpty() && pathQueue.size() < newSize) {
            pathQueue.add(paths.poll());
        }

        paths = pathQueue;
        maxSize = newSize;
    }

    public void destroy() {
        paths = new LinkedList<>();

        postInvalidate();
    }

    public void onBeforeDraw(Vector center, Vector v, double scaledRadius, boolean draw) {
        while (paths.size() >= maxSize)
            paths.poll();

        appendPath(center, v, scaledRadius);
        paths.forEach(p -> p.offset((float) -v.x, (float) -v.y));

        if (draw)
            postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        @SuppressLint("DrawAllocation") AtomicInteger i = new AtomicInteger(1);
        paths.forEach(path -> {
            paint.setAlpha((int) ((i.getAndIncrement() / (double) paths.size()) * 200)); // * 255
            canvas.drawPath(path, paint);
        });

    }

    private void appendPath(Vector center, Vector v, double scaledRadius) {
        Vector dv = v.normalize();

        Vector scaledV = v.scale(scaledRadius);
        Vector scaledN = Vector.create(dv.y, -dv.x).scale(scaledRadius);

        Vector a = center.add(scaledN);
        Vector b = center.subtract(scaledN);

        Vector d = center.subtract(scaledV);
        Vector e = d.subtract(scaledV);

        final Path path = new Path();

        path.moveTo((float) a.x, (float) a.y);
        path.quadTo((float) e.x, (float) e.y, (float) b.x, (float) b.y);
        path.quadTo((float) d.x, (float) d.y, (float) a.x, (float) a.y);

        paths.add(path);
    }
}
