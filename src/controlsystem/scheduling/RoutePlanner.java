package controlsystem.scheduling;

import controlsystem.model.Route;
import controlsystem.trafficparticipants.street.GraphPart;

import java.util.concurrent.Callable;

public class RoutePlanner implements Callable<Route> {

    // TODO approximate better / check in testing
    /** Default timeout for this kind of task. */
    public static final long TIMEOUT = 30000;

    private final GraphPart src;
    private final GraphPart dst;

    public RoutePlanner(GraphPart src, GraphPart dst) {
        this.src = src;
        this.dst = dst;
    }

    @Override
    public Route call() throws Exception {
        // TODO calculate path from current traffic situation
        return null;
    }
}
