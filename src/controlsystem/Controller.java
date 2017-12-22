package controlsystem;

import controlsystem.model.Route;
import controlsystem.persistence.ArchiveStore;
import controlsystem.scheduling.RoutePlanner;
import trafficparticipants.street.Crossing;
import trafficparticipants.street.Lane;
import controlsystem.util.Tuple.T2;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static controlsystem.util.Tuple.t2;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Controller implements RoutingControl, ControlSystem {

    public static final long INIT_DELAY       = 10000;
    public static final long CONGESTION_DELAY = 30000;

    /** Thread pool for concurrently working scheduler instances. */
    private final ExecutorService exec = Executors.newFixedThreadPool(10);

    /** Thread pool for periodically running congestion detection. */
    private final ScheduledExecutorService schedExec = Executors.newSingleThreadScheduledExecutor();

    public final Map<Long, Crossing> crossing;
    public final Map<Long, Lane> lanes;
    private final ArchiveStore rep;

    public Controller(Map<Long, Crossing> crossing, Map<Long, Lane> lanes, ArchiveStore rep) {
        this.crossing = crossing;
        this.lanes = lanes;
        this.rep = rep;

        schedExec.scheduleWithFixedDelay(() ->
                        lanes.values().forEach(l -> {
                            double lvl = l.getUsageLevel();

                            if (lvl >= Lane.CONGESTION_LVL)
                                System.out.println("Detected congestion at lane " + l);
                        }),
                INIT_DELAY,
                CONGESTION_DELAY,
                MILLISECONDS
        );
    }

    @Override
    public Route getRoute(long firstId, long thenId, long... nextIds) {
        Lane first = lanes.get(firstId);
        Lane then = lanes.get(thenId);

        if (first == null || then == null)
            return null;

        List<T2<Lane>> subRoutes = new LinkedList<>();
        subRoutes.add(t2(first, then));
        Lane last = then;

        for (long currId : nextIds) {
            Lane curr = lanes.get(currId);
            if (curr == null)
                return null;

            subRoutes.add(t2(last, curr));
            last = curr;
        }

        List<Future<Route>> pathPromise = subRoutes.stream()
                .map(p -> exec.submit(new RoutePlanner(p._0, p._1)))
                .collect(Collectors.toList());

        List<Route> routes = pathPromise.stream()
                .map(p -> {
                    try {
                        return p.get(RoutePlanner.TIMEOUT, MILLISECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        // TODO
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());

        return Route.of(routes);
    }

    @Override
    public double getLaneDensity(long laneId) {
        if (lanes.containsKey(laneId))
            return lanes.get(laneId).getUsageLevel();
        else
            return -1d;
    }

    @Override
    public void updateParticipantCount(long laneId, int count) {
        if (lanes.containsKey(laneId))
            lanes.get(laneId).setParticipants(count);
    }

    @Override
    public void reportDamage(long laneId, int severity) {
        // TODO notify road maintenance
    }

    @Override
    public void reportAccident(long laneId) {
        // TODO inform emergencies
        // TODO notify road maintenance
    }
}
