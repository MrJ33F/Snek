package project.android.snek.gameframework.player;

import java.util.List;

public interface PlayerProvider {

    List<Player> playersWithinRadius(Player bot, double radius);

}

