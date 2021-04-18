package cat.nyaa.catail.event;

import cat.nyaa.catail.common.Player;

public class PlayerStopTravelEvent {

    private final Player player;

    public PlayerStopTravelEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
