package cat.nyaa.catail.event.handlers;

import cat.nyaa.catail.common.Junctions;
import cat.nyaa.catail.common.Player;
import cat.nyaa.catail.common.Route;
import cat.nyaa.catail.controller.JunctionController;
import cat.nyaa.catail.controller.RouteController;
import cat.nyaa.catail.event.PlayerArrivedJunctionsEvent;
import cat.nyaa.catail.event.PlayerLeftJunctionsEvent;
import cat.nyaa.catail.event.PlayerStopTravelEvent;

import java.util.ConcurrentModificationException;
import java.util.Objects;

public class CommonEventHandler {
    private final JunctionController junctionController;
    private final RouteController routeController;

    public CommonEventHandler(JunctionController junctionController, RouteController routeController) {
        this.junctionController = junctionController;
        this.routeController = routeController;
    }

    public void handlerPlayerArrivedJunctionsEvent(PlayerArrivedJunctionsEvent event) {
        Junctions junctions = event.getJunctions();
        Player junctionsPlayer = junctionController.getJunctionsPlayer(junctions);
        if (!Objects.isNull(junctionsPlayer)) {
            throw new ConcurrentModificationException();
        }

        junctionController.setPlayerJunctions(event.getPlayer(), junctions);
        Route playerRoute = routeController.getPlayerRoute(event.getPlayer());
        if (Objects.isNull(playerRoute)) {
            return;
        }
        String junctionState = playerRoute.getJunctionState(junctions);
        if (Objects.isNull(junctionState)) {
            return;
        }
        junctionController.setJunctionState(junctions, junctionState);
    }

    private void leftJunctions(Player player, Junctions junctions) {
        junctionController.clearPlayerJunctions(player, junctions);
        junctionController.resetJunctionState(junctions);
    }

    public void handlerPlayerLeftJunctionsEvent(PlayerLeftJunctionsEvent event) {
        leftJunctions(event.getPlayer(), event.getJunctions());
    }

    public void handlerPlayerStopTravel(PlayerStopTravelEvent event) {
        Player player = event.getPlayer();
        Junctions junctions = junctionController.getPlayerJunctions(player);
        if (Objects.isNull(junctions)) {
            return;
        }
        leftJunctions(player, junctions);
    }
}
