package controlsystem;

import controlsystem.model.Path;
import controlsystem.trafficparticipants.street.GraphPart;

/**
 * Interface that is exposed for subsystems that require routing information (e.g. maintenance).
 */
public interface RoutingControl {
    Path getRoute(GraphPart first, GraphPart then, GraphPart... next);
}
