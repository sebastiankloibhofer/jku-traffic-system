package trafficsystem.controlsystem;

import trafficsystem.controlsystem.model.Edge;
import trafficsystem.controlsystem.model.GraphPart;

import java.util.List;

public interface RoutingControl {
    List<Edge> getRoute(GraphPart src, GraphPart... dst);
}
