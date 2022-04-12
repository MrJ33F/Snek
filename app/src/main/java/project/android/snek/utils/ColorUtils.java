package project.android.snek.utils;

import android.graphics.Color;

import java.util.Random;

public final class ColorUtils {

    private static final Random random = new Random();

    public static int rainbowColor(){
        final float hue = random.nextFloat() * 360;
        final float saturation = 0.9f;
        final float luminance = 1.0f;

        return Color.HSVToColor(new float[] {hue, saturation, luminance});
    }

}
