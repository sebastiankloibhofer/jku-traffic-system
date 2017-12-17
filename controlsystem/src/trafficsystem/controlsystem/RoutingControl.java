package trafficsystem.controlsystem;

import trafficsystem.controlsystem.model.Path;
import trafficsystem.controlsystem.trafficparticipants.street.GraphPart;

/**
 * Interface that is exposed for subsystems that require routing information (e.g. maintenance).
 */
public interface RoutingControl {
    Path getRoute(GraphPart first, GraphPart then, GraphPart... next);
}
