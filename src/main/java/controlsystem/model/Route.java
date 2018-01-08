package controlsystem.model;

import com.sun.deploy.util.StringUtils;
import trafficParticipants.street.Crossing;
import trafficParticipants.street.Lane;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a calculated route between two crossings.
 */
public class Route {
    public final List<Lane> lanes;

    public Route(List<Lane> lanes) {
        if (lanes.isEmpty())
            throw new IllegalArgumentException("Cannot create empty route");

        this.lanes = Collections.unmodifiableList(lanes);
    }

    public Route(Lane... lanes) {
        this(Arrays.asList(lanes));
    }

    public Crossing start() {
        return lanes.get(0).getStart();
    }

    public Crossing end() {
        return lanes.get(lanes.size() - 1).getEnd();
    }

    public static Route of(List<Route> routes) {
        if (routes.isEmpty())
            return null;

        if (routes.size() == 1)
            return routes.get(0);

        List<Lane> lanes = routes.stream()
                .flatMap(p -> p.lanes.stream())
                .collect(Collectors.toList());

        return new Route(lanes);
    }

    @Override
    public String toString() {

        return  "Route from "+start()+" to "+end() + ":" + System.lineSeparator() +
                StringUtils.join(this.lanes.stream().map(Lane::toString).collect(Collectors.toList()), System.lineSeparator());
    }
}
