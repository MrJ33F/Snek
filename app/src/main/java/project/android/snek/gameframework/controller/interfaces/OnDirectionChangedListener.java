package project.android.snek.gameframework.controller.interfaces;

import java.util.function.Consumer;

import project.android.snek.utils.Vector;

public interface OnDirectionChangedListener {
    void setOnDirectionChanged(Consumer<Vector> onDirectionChanged);

}