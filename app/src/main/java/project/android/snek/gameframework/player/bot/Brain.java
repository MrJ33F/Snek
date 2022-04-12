package project.android.snek.gameframework.player.bot;

import project.android.snek.gameframework.player.Player;
import project.android.snek.utils.Vector;

public interface Brain {

    Vector predict(Player bot);

}
