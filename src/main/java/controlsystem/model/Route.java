package controlsystem.model;

import trafficParticipants.street.Crossing;
import trafficParticipants.street.Lane;

import javax.persistence.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.Instant.now;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

/**
 * Represents a calculated route between two crossings.
 */
@Entity
@Table(name = "routes")
public class Route {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = AUTO)
    private long id;

    @ManyToMany(fetch = EAGER)
    @JoinTable(
            name = "route_lanes",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "lane_id"))
    private List<Lane> lanes;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "start_id", nullable = false)
    private Crossing start;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "end_id", nullable = false)
    private Crossing end;

    @Column(name = "created_at")
    private Instant created;

    @Column(name = "modified_at")
    private Instant modified;

    public Route(List<Lane> lanes) {
        if (lanes.isEmpty())
            throw new IllegalArgumentException("Cannot create empty route");

        this.lanes = Collections.unmodifiableList(lanes);
        start = lanes.get(0).getStart();
        end = lanes.get(lanes.size() - 1).getEnd();

        this.created = now();
        this.modified = now();
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
                this.lanes.stream().map(Lane::toString).collect(Collectors.joining(System.lineSeparator()));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Instant getCreated() {
        return created;
    }

    private void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    private void setModified(Instant modified) {
        this.modified = modified;
    }
}
