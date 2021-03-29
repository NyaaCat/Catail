package cat.nyaa.catail.controller;

import cat.nyaa.catail.common.BlockGroupState;
import cat.nyaa.catail.common.Junctions;
import cat.nyaa.catail.common.Player;
import cat.nyaa.catail.common.Route;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JunctionController {

    Map<Player, Junctions> playerJunctionsMap = new HashMap<>();
    Map<Junctions, Player> junctionsPlayerMap = new HashMap<>();

    public void resetJunctionState(Junctions junction) {
        String defaultState = junction.getDefaultState();
        setJunctionState(junction, defaultState);
    }

    public void setJunctionState(Junctions junction, String state) {
        BlockGroupState blockGroupState = junction.getJunctionStates().get(state);
        if (Objects.isNull(blockGroupState)) {
            throw new IllegalStateException(String.format("State %s not found in %s", state, junction.getId()));
        }
        blockGroupState.applyAllBlockState();
    }

    public void setJunctionStateByRoute(Junctions junction, Route route) {
        String state = route.getJunctionState(junction);
        if (Objects.isNull(state)) {
            throw new IllegalStateException(
                String.format("Junctions State %s not found in %s", junction.getId(), route.getId())
            );
        }
        setJunctionState(junction, state);
    }

    public Junctions getPlayerJunctions(Player player) {
        return playerJunctionsMap.get(player);
    }

    public Player getJunctionsPlayer(Junctions junctions) {
        return junctionsPlayerMap.get(junctions);
    }

    public void setPlayerJunctions(Player player, Junctions junctions) {
        if (!Objects.isNull(player)) {
            playerJunctionsMap.put(player, junctions);
        }
        if (!Objects.isNull(junctions)) {
            junctionsPlayerMap.put(junctions, player);
        }
    }

    public void clearPlayerJunctions(Player player, Junctions junctions) {
        setPlayerJunctions(null, junctions);
        setPlayerJunctions(player, null);
    }
}
