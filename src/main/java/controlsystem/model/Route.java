package controlsystem.model;

import com.sun.deploy.util.StringUtils;
import trafficParticipants.street.Crossing;
import trafficParticipants.street.Lane;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.EAGER;

/**
 * Represents a calculated route between two crossings.
 */
@Entity
@Table(name = "routes")
public class Route {

    @ManyToMany(fetch = EAGER)
    @JoinTable(name = "route_lanes")
    private List<Lane> lanes;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "start", nullable = false)
    private Crossing start;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "end", nullable = false)
    private Crossing end;

    public Route(List<Lane> lanes) {
        if (lanes.isEmpty())
            throw new IllegalArgumentException("Cannot create empty route");

        this.lanes = Collections.unmodifiableList(lanes);
        start = lanes.get(0).getStart();
        end = lanes.get(lanes.size() - 1).getEnd();
    }

    public Route(Lane... lanes) {
        this(Arrays.asList(lanes));
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

        return "Route from " + start + " to " + end + ":" + System.lineSeparator() +
                StringUtils.join(this.lanes.stream().map(Lane::toString).collect(Collectors.toList()), System.lineSeparator());
    }

    public List<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(List<Lane> lanes) {
        this.lanes = lanes;
    }

    public Crossing getStart() {
        return start;
    }

    private void setStart(Crossing start) {
        this.start = start;
    }

    public Crossing getEnd() {
        return end;
    }

    private void setEnd(Crossing end) {
        this.end = end;
    }
}
