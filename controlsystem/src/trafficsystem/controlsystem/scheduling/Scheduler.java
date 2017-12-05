package trafficsystem.controlsystem.scheduling;

import trafficsystem.controlsystem.RoutingControl;
import trafficsystem.controlsystem.model.Edge;
import trafficsystem.controlsystem.model.GraphPart;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scheduler implements RoutingControl {
    /** Thread pool for concurrently working scheduler instances. */
    private final ExecutorService schedulers = Executors.newFixedThreadPool(10);

    @Override
    public List<Edge> getRoute(GraphPart src, GraphPart... dst) {
        // TODO
        return null;
    }
}
