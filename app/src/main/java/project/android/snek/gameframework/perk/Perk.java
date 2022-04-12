package project.android.snek.gameframework.perk;

import project.android.snek.gameframework.player.Player;

public interface Perk {

    void apply(Player player);

    void attach(Player player);

    boolean isActive();

}
