package project.android.snek.gameframework.player.bot;

import project.android.snek.gameframework.player.Player;
import project.android.snek.utils.Vector;

public class NeuralNet implements Brain {

    private final Radar radar;

    public NeuralNet(Radar radar) {
        this.radar = radar;
    }

    @Override
    public Vector predict(Player bot) {
        return Vector.randomVersor();
    }
}
