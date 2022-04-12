package project.android.snek.multiplayer.model;

import project.android.snek.utils.Vector;

public class DirectionChangedMessage {

    private final String playerId;
    private final Vector direction;

    public DirectionChangedMessage(String playerId, Vector direction) {
        this.playerId = playerId;
        this.direction = direction;
    }

}