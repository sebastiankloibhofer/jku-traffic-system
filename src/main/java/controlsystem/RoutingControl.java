package controlsystem;

import controlsystem.model.Route;

/**
 * Interface that is exposed for subsystems that require routing information (e.g. maintenance).
 */
public interface RoutingControl {
    Route getRoute(int firstId, int thenId, int... nextIds);
    double getLaneDensity(int laneId);
}
