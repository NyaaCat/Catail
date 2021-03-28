package cat.nyaa.catail.event;

import cat.nyaa.catail.common.Junctions;
import cat.nyaa.catail.common.Player;

public class PlayerArrivedJunctionsEvent {
    private final Player player;
    private final Junctions junctions;

    public PlayerArrivedJunctionsEvent(Player player, Junctions junctions) {
        this.player = player;
        this.junctions = junctions;
    }

    public Junctions getJunctions() {
        return junctions;
    }

    public Player getPlayer() {
        return player;
    }
}
