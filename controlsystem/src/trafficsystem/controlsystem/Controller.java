package trafficsystem.controlsystem;

import trafficsystem.controlsystem.model.Path;
import trafficsystem.controlsystem.persistence.ArchiveStore;
import trafficsystem.controlsystem.scheduling.RoutePlanner;
import trafficsystem.controlsystem.trafficparticipants.street.Crossing;
import trafficsystem.controlsystem.trafficparticipants.street.GraphPart;
import trafficsystem.controlsystem.trafficparticipants.street.Lane;
import trafficsystem.controlsystem.util.Tuple.T2;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static trafficsystem.controlsystem.util.Tuple.t2;

public class Controller implements RoutingControl, ControlSystem {
    /** Thread pool for concurrently working scheduler instances. */
    private final ExecutorService schedulers = Executors.newFixedThreadPool(10);

    public final Set<Crossing> crossing;
    public final Set<Lane> lanes;
    private final ArchiveStore rep;

    public Controller(Set<Crossing> crossing, Set<Lane> lanes, ArchiveStore rep) {
        this.crossing = crossing;
        this.lanes = lanes;
        this.rep = rep;
    }

    @Override
    public Path getRoute(GraphPart first, GraphPart then, GraphPart... next) {
        List<T2<GraphPart>> subRoutes = new LinkedList<>();
        subRoutes.add(t2(first, then));
        GraphPart last = then;

        for (GraphPart curr : next) {
            subRoutes.add(t2(last, curr));
            last = curr;
        }

        List<Future<Path>> pathPromise = subRoutes.stream()
                .map(p -> schedulers.submit(new RoutePlanner(p._0, p._1)))
                .collect(Collectors.toList());

        List<Path> paths = pathPromise.stream()
                .map(p -> {
                    try {
                        return p.get(RoutePlanner.TIMEOUT, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        // TODO
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());

        return Path.of(paths);
    }

    @Override
    public void updateLaneInformation(Lane l, Object info) {
        // TODO
    }
}
